package com.ask.javacore.util;

import java.text.Normalizer;

public final class NormalizeUtils {

  private NormalizeUtils() {
  }

  public static String normalizeNfc(String value) {
    if (!Normalizer.isNormalized(value, Normalizer.Form.NFC)) {
      return Normalizer.normalize(value, Normalizer.Form.NFC);
    }
    return value;
  }

}
