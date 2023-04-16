package com.example.demo.parser.excel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.example.demo.parser.ParserContext;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

/* Parser context to excel files. */
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class ExcelParserContext extends ParserContext {

  private Map<String, Integer> headerIndexMap = new HashMap<>();
  private DataFormatter formatter = new DataFormatter();

  public ExcelParserContext(ParserContext context) {
    super(context);
  }

  int getColumnIndex(String header) {
    if (!this.headerIndexMap.containsKey(header)) {
      log.trace("Unable to locate header '{}' in context", header);
      return -1;
    }
    return this.headerIndexMap.get(header);
  }

  DataFormatter getDataFormatter() {
    return formatter;
  }

  public void configureHeaders(Row headerRow) {
    Iterator<Cell> cellIterator = headerRow.cellIterator();

    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      addHeaderColumn(cell.getStringCellValue().trim(), cell.getColumnIndex());
    }
  }

  private void addHeaderColumn(final String header, final int columnIndex) {
    this.headerIndexMap.put(header, columnIndex);
  }
}
