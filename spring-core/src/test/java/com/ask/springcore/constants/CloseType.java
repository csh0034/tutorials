package com.ask.springcore.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CloseType {

  public static final int ABNORMAL = 1;
  public static final int NORMAL = 2;
  public static final int BATCH = 3;

}
