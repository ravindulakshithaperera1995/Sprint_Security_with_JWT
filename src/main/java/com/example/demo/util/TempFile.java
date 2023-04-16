/*
 * Copyright (C) 2020 Lumity, Inc. - All Rights Reserved
 *
 * CONFIDENTIAL
 *
 * All information contained herein is, and remains the property of Lumity, Inc. and its partners, if any.
 * The intellectual and technical concepts contained herein are proprietary to Lumity, Inc. and its partners
 * and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or
 * copyright law. Dissemination of this information or reproduction of this material is strictly forbidden unless
 * prior written permission is obtained from Lumity, Inc.
 */

package com.example.demo.util;

import lombok.Data;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

/* TempFile Implementation */
@Data
public class TempFile implements AutoCloseable {
  private File file;
  private String exposedFileName;
  private String contentType;

  /** Temp file Constructor */
  public TempFile() {
    try {
      this.file = File.createTempFile("billing", ".tmp");
      this.file.deleteOnExit();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public TempFile(MultipartFile multipartFile) {
    this();
    try {
      multipartFile.transferTo(file);
      this.exposedFileName = multipartFile.getOriginalFilename();
      this.contentType = multipartFile.getContentType();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public TempFile(InputStream inputStream, String fileName, String contentType) {
    this();
    try {
      IOUtils.copy(inputStream, new FileOutputStream(file));
      this.exposedFileName = fileName;
      this.contentType = contentType;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** close file */
  @Override
  public void close() throws IOException {
    if (file != null && file.exists()) {
      Files.delete(file.toPath());
    }
  }

  /**
   * Get file
   *
   * @return (@link File)
   */
  public File getFile() {
    return file;
  }

  /**
   * Get input stream
   *
   * @return {@link FileInputStream}
   */
  public FileInputStream createFileInputStream() {
    try {
      return new FileInputStream(file);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
