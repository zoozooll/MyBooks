package com.chinachip.ccbooks.engine;

import android.os.Environment;
import java.io.File;

public class utils
{
  public static final String DEBUG = "aiRead";

  public static String getSDPath()
  {
    File sdDir = null;
    boolean sdCardExist = Environment.getExternalStorageState().equals(
      "mounted");
    if (sdCardExist) {
      sdDir = Environment.getExternalStorageDirectory();
    }

    return sdDir.toString();
  }
}

