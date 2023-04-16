package com.example.demo.parser.excel;

import com.example.demo.parser.Record;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

@Slf4j
/* Excel Record model */
public class ExcelRecord implements Record {

  private ExcelParserContext context;
  private Row row;

  ExcelRecord(final ExcelParserContext context, final Row row) {
    this.context = context;
    this.row = row;
  }

  @Override
  public String getString(final String field, final boolean isOptional) {
    int colIndex = context.getColumnIndex(field);

    if (colIndex < 0) {
      if (isOptional) {
        // This means that the header was not found in the context. Ignore it.
        return null;
      } else {
        throw new IllegalArgumentException("Header " + field + " is not found");
      }
    }

    String formattedValue;
    Cell cell = row.getCell(colIndex);
    if (cell != null && cell.getCellType() == CellType.NUMERIC) {
      // Take maxmimum of 32767 decimal points to reduce rounding errors.
      DecimalFormat df = new DecimalFormat("#.####");
      df.setMaximumFractionDigits(Short.MAX_VALUE);
      formattedValue = df.format(cell.getNumericCellValue());
    } else {
      formattedValue = context.getDataFormatter().formatCellValue(cell);
    }

    if (StringUtils.isBlank(formattedValue)) {
      return null;
    } else {
      return formattedValue.trim();
    }
  }

  @Override
  public BigDecimal getBigDecimal(final String field, final boolean isOptional) {
    String strVal = getString(field, isOptional);

    try {
      Double value = Double.valueOf(strVal);
      return BigDecimal.valueOf(value);
    } catch (NumberFormatException ex) {
      log.debug(
          "Ignoring a NumberFormatException when attempting to read a Long value. Cell "
              + "value: {}, Column Header: {}",
          strVal,
          field);
      return null;
    }
  }

  @Override
  public Long getLong(String field, boolean isOptional) {
    String strVal = getString(field, isOptional);

    try {
      return Long.parseLong(strVal);
    } catch (NumberFormatException ex) {
      log.debug(
          "Ignoring a NumberFormatException when attempting to read a Long value. Cell "
              + "value: {}, Column Header: {}",
          strVal,
          field);
      return null;
    }
  }

  @Override
  public Integer getInt(String field, boolean isOptional) {
    String strVal = getString(field, isOptional);

    try {
      return Integer.parseInt(strVal);
    } catch (NumberFormatException ex) {
      log.debug(
              "Ignoring a NumberFormatException when attempting to read a integer value. Cell "
                      + "value: {}, Column Header: {}",
              strVal,
              field);
      return null;
    }
  }

  @Override
  public LocalDate getDate(String field, boolean isOptional) {
    int colIndex = context.getColumnIndex(field);

    if (colIndex < 0) {
      if (isOptional) {
        // This means that the header was not found in the context. Ignore it.
        return null;
      } else {
        throw new IllegalArgumentException("Header " + field + " is not found");
      }
    }

    Cell cell = row.getCell(colIndex);
    if (cell != null && cell.getCellType() == CellType.NUMERIC) {
      Date date = cell.getDateCellValue();
      return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    return LocalDate.now();
  }
}
