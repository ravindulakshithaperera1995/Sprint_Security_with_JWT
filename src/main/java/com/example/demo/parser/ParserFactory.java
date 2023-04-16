package com.example.demo.parser;

import com.example.demo.parser.excel.ExcelParserContext;
import com.example.demo.parser.sample.SampleParser;

/* Interface to select bill parser */
public interface ParserFactory {

  /**
   * Select corresponding parsers
   *
   * @param context Parser Context
   * @return {@link FileParser}
   */
  static FileParser getParser(final ParserContext context) {
    ExcelParserContext parserContext = new ExcelParserContext(context);
    return new SampleParser(parserContext);
  }
}
