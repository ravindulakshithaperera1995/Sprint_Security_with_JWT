package com.example.demo.parser;

import java.math.BigDecimal;
import java.time.LocalDate;

/* Interface for Record class */
public interface Record {

  default String getString(String field) {
    return getString(field, false);
  }

  String getString(String field, boolean isOptional);

  default BigDecimal getBigDecimal(String field) {
    return getBigDecimal(field, false);
  }

  BigDecimal getBigDecimal(String field, boolean isOptional);

  default Long getLong(String field) {
    return getLong(field, false);
  }

  Long getLong(String field, boolean isOptional);

  Integer getInt(String field, boolean isOptional);

  LocalDate getDate(String field, boolean isOptional);
}
