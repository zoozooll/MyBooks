package com.chinachip.ccbooks.engine;

import android.graphics.Paint;
import java.util.ArrayList;

public abstract class Parse {
	public final short DT_SINGLEBYTE = 1;
	public final short DT_MULTIBYTES = 2;
	public final short DT_UNICODEBE = 3;
	public final short DT_UNICODELE = 4;
	public final short DT_UTF8 = 5;

	protected short dataType = 5;

	private int curLine = 0;
	private String codePage = "GBK,GB2312,CP936,CP949,CP932,CP950,BIG5,EUC-CN,EUC-JP,EUC-KR,SHIFT-JIS";
	protected String enc = "GBK";

	public abstract Paint getPaint();

	public abstract int getLineCount();

	public abstract int getPageWidth();

	private boolean isUnicodeLE() {
		return this.dataType == 4;
	}

	protected boolean isMultiByte() {
		return this.codePage.contains(this.enc);
	}

	public int GetUtf8ByteNumForWord(byte firstCh) {
		byte temp = -128;
		int num = 0;

		while ((temp & firstCh) == temp) {
			num++;
			temp = (byte) (temp >> 1);
		}
		if (num == 0) {
			return 1;
		}
		return num;
	}

	public int run(Cache cache, int beg, int end, ArrayList<String> al,
			int lineCount) {
		String text = cache.getData();
		CharSequence toMeasure = text;

		int length = toMeasure.length();

		int next = beg;

		int width = getPageWidth();

		float[] measuredWidth = { 0.0F };

		while (next < length) {
			int bPoint = getPaint().breakText(toMeasure, next, length, true,
					width, measuredWidth);

			int enterPosition = 0;

			enterPosition = text.substring(next, next + bPoint).indexOf('\n');

			if (enterPosition < 0) {
				al.add(text.substring(next, next + bPoint));
				next += bPoint;
			} else {
				al.add(text.substring(next, next + enterPosition + 1));

				next += enterPosition + 1;
			}

			if (end > 0) {
				if (next >= end) {
					if (next == Math.abs(end)) {
						return next;
					}
					if (next > Math.abs(end)) {
						next -= ((String) al.get(al.size() - 1)).length();

						return next;
					}
				}

			} else if (end == 0) {
				if (al.size() == lineCount) {
					return next;
				}

			} else if (next >= Math.abs(end)) {
				return next;
			}

		}

		return next;
	}

	public String getEnc() {
		return this.enc;
	}

	public void setEnc(String enc) {
		this.enc = enc;
	}
}
