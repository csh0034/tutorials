package com.ask.sheets.google;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SheetsVO {

  private String language;
  private final Map<String, String> data = new LinkedHashMap<>();

}
