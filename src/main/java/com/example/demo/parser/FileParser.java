package com.example.demo.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/* Parser interface */
public interface FileParser<T> {

  List<T> parseEntries(InputStream inputStream) throws IOException;
}
