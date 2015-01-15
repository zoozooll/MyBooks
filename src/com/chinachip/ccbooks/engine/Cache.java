package com.chinachip.ccbooks.engine;

import java.util.ArrayList;

public class Cache
{
  private ArrayList<Integer> paragraphIndex = null;

  private String data = null;

  private long start = 0L;

  private int blockId = -1;

  public int getBlockId()
  {
    return this.blockId;
  }

  public void setBlockId(int blockId) {
    this.blockId = blockId;
  }

  public long getStart() {
    return this.start;
  }

  public void setStart(long start) {
    this.start = start;
  }

  public String getData()
  {
    return this.data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public ArrayList<Integer> getParagraphIndex() {
    return this.paragraphIndex;
  }

  public void setParagraphIndex(ArrayList<Integer> paragraphIndex) {
    this.paragraphIndex = paragraphIndex;
  }
}
