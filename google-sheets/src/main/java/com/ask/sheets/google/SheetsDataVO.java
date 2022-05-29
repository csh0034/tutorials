package com.ask.sheets.google;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SheetsDataVO {

  private String language;
  private Map<String, String> dataMap = new LinkedHashMap<>();

}
