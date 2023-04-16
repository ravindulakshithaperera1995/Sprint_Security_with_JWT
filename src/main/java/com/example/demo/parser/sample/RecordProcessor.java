package com.example.demo.parser.sample;

import java.util.List;

@FunctionalInterface
public interface RecordProcessor<INPUT, OUTPUT> {
  List<OUTPUT> processRecord(INPUT record);
}
