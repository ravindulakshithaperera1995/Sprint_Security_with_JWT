package com.example.demo.parser.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.example.demo.parser.sample.RecordProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

@Slf4j
/* Class with common excel methods */
public class ExcelSupport {

  private ExcelSupport() {}

  public static Iterable<Row> getRows(final InputStream stream, final ExcelParserContext context)
      throws IOException {
    log.debug("Reading file...");
    Iterable<Row> rows;

    try (Workbook workbook = WorkbookFactory.create(stream)) {
      int firstRow = 1;
      int header = 0;
      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
        if (!workbook.isSheetHidden(i)) {
          Sheet sheet = workbook.getSheetAt(i);
          Iterator<Row> rowIterator =
              IteratorUtils.filteredIterator(
                  sheet.iterator(),
                      row -> {
                    // Remove conditional formats in cells
                    if (row.getRowNum() == header) {
                      context.configureHeaders(row);
                    }

                    if (row.getRowNum() == firstRow
                        && (row.getCell(0) == null
                            || CellType.BLANK.equals(row.getCell(0).getCellType()))) {
                      throw new IllegalArgumentException("No data rows found");
                    }

                    Cell cell = row.getCell(0);
                    return (cell != null && !CellType.BLANK.equals(cell.getCellType()))
                        && (!CellType.STRING.equals(cell.getCellType())
                            || !cell.getStringCellValue().isEmpty());
                  });

          rows = () -> rowIterator;
          log.debug("Workbook parsed.");

          return rows;
        }
      }
    }
    return null;
  }

  public static Row getHeaderRow(final Iterable<Row> rows) {
    return StreamSupport.stream(rows.spliterator(), false)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Header row not found"));
  }

  public static void checkHeader(
      final Row headerRow, String[] headers, ExcelParserContext context) {

    context.configureHeaders(headerRow);
    Iterator<Cell> cellIterator = headerRow.cellIterator();
    List<String> headerList = Arrays.asList(headers);
    List<String> rowHeaders = new ArrayList<>();
    int i = 0;

    while (cellIterator.hasNext()) {
      rowHeaders.add(i, cellIterator.next().getStringCellValue());
      i++;
    }

    List<String> filteredHeaders = rowHeaders
            .stream()
            .filter(header -> !StringUtils.isBlank(header))
            .collect(Collectors.toList());

    headerList.stream().filter(name -> !filteredHeaders.contains(name)).forEach(name -> {
      log.error("Invalid header {}", name);
      throw new IllegalArgumentException(
              "Header " + name + " is not found in the file.");
    });
  }

  public static <O> List<O> parseContent(
          ExcelParserContext context,
          Iterable<Row> rows,
          int rowsToSkipForData,
          RecordProcessor processor) {

    long time = System.currentTimeMillis();

    try {
      return (List<O>) StreamSupport.stream(rows.spliterator(), false)
          .skip(rowsToSkipForData) // Do not parse the header rows
          .map(row -> new ExcelRecord(context, row))
          .flatMap(
              record ->
                  // Process Row
                  processor.processRecord(record).stream())
          .collect(Collectors.toList());
    } finally {
      log.debug("Parsing completed in {} ms", System.currentTimeMillis() - time);
    }
  }

  public static Row getHeaderRow(final Iterable<Row> rows, int rowsToSkip) {
    try {
      return StreamSupport.stream(rows.spliterator(), false)
          .skip(rowsToSkip) // Skip any uninteresting rows to reach the header row
          .findFirst()
          .orElseThrow(() -> new Exception("Header row not found"));
    } catch (Exception e) {
      log.error(e.getMessage());
      return null;
    }
  }
}
