package com.meitao.common.util;

import java.util.Random;

public class UserCodeUtil
{
  public static final int getUserCode()
  {
    Random random = new Random();
    int i = random.nextInt(2000);
    return i;
  }
}