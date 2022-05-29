package com.ask.sheets.google;

import java.util.SortedMap;
import java.util.TreeMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SheetsDataVO {

  private String language;
  private SortedMap<String, String> dataMap = new TreeMap<>();

}
