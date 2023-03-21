package com.api.automation.enums;

import lombok.Getter;

public enum FileExtensions {
  PDF(".pdf"),
  CSV(".csv"),
  XLSX(".xlsx"),
  XLS(".xls"),
  PNG(".png"),
  JPG(".jpg"),
  JPEG(".jpeg"),
  DOC(".doc"),
  GIF(".gif");

  @Getter private final String fileExtension;

  FileExtensions(String fileExtension) {
    this.fileExtension = fileExtension;
  }
}
