package com.example.demo.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/* ParserContext builder */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ParserContext {

  private String fileId;

  public ParserContext(ParserContext ctx) {
    this.fileId = ctx.fileId;
  }
}
