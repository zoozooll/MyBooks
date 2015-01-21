package com.chinachip.ccbooks.engine;

public class LineIndex {
	private long start;
	private int topIndex;

	public long getStart() {
		return this.start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public int getTopIndex() {
		return this.topIndex;
	}

	public void setTopIndex(int topIndex) {
		this.topIndex = topIndex;
	}

	public boolean compareTo(LineIndex lineIndex) {
		return (getStart() == lineIndex.getStart())
				&& (getTopIndex() == lineIndex.getTopIndex());
	}
}
