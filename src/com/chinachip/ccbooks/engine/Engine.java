package com.chinachip.ccbooks.engine;

import android.util.Log;
import com.chinachip.books.plugin.Param;
import com.chinachip.books.plugin.Plugin;
import com.chinachip.books.plugin.PluginMgr;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public abstract class Engine extends Parse {
	private ArrayList<Long> blockIndex = new ArrayList();

	private String temppath = getTempPath();

	public int filehandle = 0;

	private String codePage = "GBK,GB2312,CP936,CP949,CP932,CP950,BIG5,EUC-CN,EUC-JP,EUC-KR,SHIFT-JIS";

	private CacheManager cacheManager = null;

	private int topCacheID = 0;
	private int nextCacheID = 0;

	private int topIndex = 0;
	private int endIndex = 0;
	private CutParagraph orfb = null;

	private int lineCount = getLineCount();
	private String tempEX = getTempEX();

	private int cacheCount = getCacheCount();

	private final String slash = "/";

	long duration = 0L;

	public PluginMgr pm = new PluginMgr();

	private ArrayList<LineIndex> lineIndexList = null;

	public abstract String getTempEX();

	public abstract int getCacheCount();

	public abstract void pluginInit();

	public Engine() {
		pluginInit();
	}

	public PluginMgr getPluginMgr() {
		return this.pm;
	}

	public ArrayList<String> getCurrPage(long offset) throws IOException {
		ArrayList contentList = new ArrayList();

		setPosition(offset);

		this.topCacheID = this.cacheManager.getCurrentCacheID();

		Cache a = this.cacheManager.getCacheFromID(this.topCacheID);
		Plugin plugin = this.pm.getDefaultPlugin();
		long filelength = this.pm.length(plugin, this.filehandle);
		if (offset == filelength) {
			this.nextCacheID = this.topCacheID;
			this.topIndex = a.getData().length();
			this.endIndex = this.topIndex;

			return contentList;
		}

		this.topIndex = (int) (offset - a.getStart());
		long nextBlockIndex = ((Long) this.cacheManager.getBlockIndex().get(
				a.getBlockId())).longValue();
		int longth = (int) (nextBlockIndex - a.getStart());
		float persent = this.topIndex / longth * 100.0F;
		this.topIndex = (int) (persent * a.getData().length() / 100.0F);

		if (this.topIndex < 0) {
			this.topIndex = 0;
		}
		int reTopIndex = getLastReturn(a, this.topIndex);

		ArrayList tempLineIndex = new ArrayList();

		if (this.topIndex > 0) {
			this.endIndex = run(a, reTopIndex, this.topIndex, tempLineIndex, 0);
		} else {
			this.endIndex = 0;
		}
		this.nextCacheID = this.topCacheID;
		contentList = getNextPage();

		return contentList;
	}

	public ArrayList<String> getCurrInsidePage(int topIndex, int topCacheID)
			throws IOException {
		ArrayList contentList = new ArrayList();
		this.cacheManager.setCurrentCacheID(this.topCacheID);
		this.nextCacheID = topCacheID;
		this.nextCacheID = topCacheID;
		this.endIndex = topIndex;
		contentList = getNextPage();
		return contentList;
	}

	public int getCurrTopIndexAt() {
		return this.topIndex;
	}

	public int getCurrTopCacheID() {
		return this.cacheManager.getCurrentCacheID();
	}

	public abstract String getTempPath();

	public ArrayList<String> getCurrPage() throws IOException {
		ArrayList contentList = new ArrayList();

		this.nextCacheID = this.topCacheID;
		this.endIndex = this.topIndex;

		contentList = getNextPage();

		return contentList;
	}

	public long getOffsetByPercent(float per) {
		Plugin plugin = this.pm.getDefaultPlugin();
		long filelength = this.pm.length(plugin, this.filehandle);
		long len = filelength;
		float alllen = 1.0F;
		if (per > 0.0F) {
			if (per > 100.0F)
				per = 100.0F;
			alllen = 100.0F;
		} else if (per < -1.0F) {
			per = -1.0F;
		}
		alllen = Math.abs(alllen);
		len = (long) ((float) len * per / alllen);

		return len;
	}

	public ArrayList<String> getNextPage() throws IOException {
		ArrayList contentList = new ArrayList();
		this.lineIndexList = new ArrayList();
		Cache a = this.cacheManager.getCacheFromID(this.nextCacheID);

		if (a == null) {
			throw new IOException();
		}
		if ((this.cacheManager.isLastBlock(this.nextCacheID))
				&& (this.endIndex >= a.getData().length())) {
			return contentList;
		}

		this.topIndex = this.endIndex;
		this.topCacheID = this.nextCacheID;

		this.cacheManager.setCurrentCacheID(this.topCacheID);

		if (this.topIndex == a.getData().length()) {
			int tempCacheID = this.cacheManager.getNextDataBlock();
			if (tempCacheID == -2) {
				this.nextCacheID = this.cacheManager.getCurrentCacheID();
				return contentList;
			}
			this.topCacheID = tempCacheID;
			a = this.cacheManager.getCacheFromID(this.topCacheID);

			this.topIndex = 0;
			this.endIndex = this.topIndex;
			this.nextCacheID = this.topCacheID;
		}

		int lineCount = getLineCount();

		this.endIndex = run(a, this.topIndex, 0, contentList, lineCount);
		LineIndex lineIndex = new LineIndex();
		int tempIndex = this.topIndex;
		if (contentList.size() > 0) {
			lineIndex.setStart(a.getStart());
			lineIndex.setTopIndex(this.topIndex);
			this.lineIndexList.add(lineIndex);

			for (int i = 0; i < contentList.size() - 1; i++) {
				lineIndex = new LineIndex();
				lineIndex.setStart(a.getStart());
				tempIndex += ((String) contentList.get(i)).length();
				lineIndex.setTopIndex(tempIndex);
				this.lineIndexList.add(lineIndex);
			}

		}

		if (contentList.size() < getLineCount()) {
			this.nextCacheID = this.cacheManager.getNextDataBlock();
			if (this.nextCacheID == -2) {
				this.nextCacheID = this.cacheManager.getCurrentCacheID();
				return contentList;
			}

			Cache b = this.cacheManager.getCacheFromID(this.nextCacheID);
			this.endIndex = run(b, 0, 0, contentList, getLineCount());

			if (contentList.size() > 0) {
				int temp = this.lineIndexList.size();
				lineIndex = new LineIndex();
				lineIndex.setStart(b.getStart());
				tempIndex = 0;
				lineIndex.setTopIndex(tempIndex);
				this.lineIndexList.add(lineIndex);
				for (int i = temp; i < contentList.size() - 1; i++) {
					lineIndex = new LineIndex();
					lineIndex.setStart(b.getStart());
					tempIndex += ((String) contentList.get(i)).length();
					lineIndex.setTopIndex(tempIndex);
					this.lineIndexList.add(lineIndex);
				}
			}
		}

		Log.i("sqli", "lineIndex ------size-----" + this.lineIndexList.size());
		Log.i("sqli",
				"lineIndex ------list-----" + this.lineIndexList.toString());
		Log.i("sqli", "contentList ------size-----" + contentList.size());
		for (int i = 0; i < contentList.size(); i++) {
			contentList.set(i, trimTail((String) contentList.get(i)));
		}
		return contentList;
	}

	private boolean BOF(long topIndex) {
		if ((this.enc == "UTF-8") && (topIndex == 3L))
			return true;
		if (((this.enc == "UTF-16LE") || (this.enc == "UTF-16BE"))
				&& (topIndex <= 2L)) {
			return true;
		}
		return topIndex <= 0L;
	}

	public ArrayList<String> getPrevPage() throws IOException {
		ArrayList contentList = new ArrayList();
		this.lineIndexList = new ArrayList();
		Cache a = this.cacheManager.getCacheFromID(this.topCacheID);
		if ((this.cacheManager.isFirstBlock(this.topCacheID))
				&& (this.topIndex <= 0)) {
			return contentList;
		}
		this.nextCacheID = this.topCacheID;
		this.endIndex = this.topIndex;

		this.cacheManager.setCurrentCacheID(this.topCacheID);
		String data = a.getData();

		int lineCount = getLineCount();

		int reTopIndex = 0;
		int blockId = this.cacheManager.getCurrentBlockID();

		if (this.topIndex == 0) {
			int tempCacheID = this.cacheManager.getPrevDataBlock();
			if (tempCacheID == -1) {
				return contentList;
			}
			this.topCacheID = tempCacheID;
			a = this.cacheManager.getCacheFromID(this.topCacheID);
			data = a.getData();
			this.topIndex = data.length();
			this.endIndex = this.topIndex;
			this.nextCacheID = this.topCacheID;
		}
		this.cacheManager.setCurrentCacheID(this.topCacheID);
		while (true) {
			reTopIndex = getLastReturn(a, this.topIndex);

			ArrayList tempLineIndex = new ArrayList();

			if (this.topIndex == 0) {
				break;
			}
			run(a, reTopIndex, -this.topIndex, tempLineIndex, lineCount);

			if (tempLineIndex.size() > 0) {
				this.topIndex = reTopIndex;

				if (contentList.size() != 0) {
					for (int i = 0; i < contentList.size(); i++) {
						tempLineIndex.add((String) contentList.get(i));
					}
				}

				contentList = (ArrayList) tempLineIndex.clone();
			}

			if (contentList.size() == lineCount) {
				break;
			}
			if (contentList.size() > lineCount) {
				tempLineIndex = contentList;
				contentList = new ArrayList();
				for (int i = 0; i < tempLineIndex.size() - getLineCount(); i++) {
					this.topIndex += ((String) tempLineIndex.get(i)).length();
				}
				for (int i = 0; i < getLineCount(); i++) {
					contentList.add((String) tempLineIndex.get(tempLineIndex
							.size() - getLineCount() + i));
				}
			}

		}

		LineIndex lineIndex = new LineIndex();
		int tempIndex = this.topIndex;
		if (contentList.size() > 0) {
			lineIndex.setStart(a.getStart());
			lineIndex.setTopIndex(this.topIndex);
			this.lineIndexList.add(lineIndex);

			for (int i = 0; i < contentList.size() - 1; i++) {
				lineIndex = new LineIndex();
				lineIndex.setStart(a.getStart());
				tempIndex += ((String) contentList.get(i)).length();
				lineIndex.setTopIndex(tempIndex);
				this.lineIndexList.add(lineIndex);
			}
		}

		if (contentList.size() < getLineCount()) {
			int tempCacheID = this.cacheManager.getPrevDataBlock();

			if (tempCacheID == -1) {
				return getCurrPage(0L);
			}
			this.topCacheID = tempCacheID;

			this.cacheManager.setCurrentCacheID(this.topCacheID);

			Cache b = this.cacheManager.getCacheFromID(this.topCacheID);

			data = b.getData();

			this.topIndex = data.length();
			while (true) {
				reTopIndex = getLastReturn(b, this.topIndex);
				ArrayList tempContentList = new ArrayList();
				if (this.topIndex == 0) {
					break;
				}

				run(b, reTopIndex, -this.topIndex, tempContentList, lineCount);

				if (tempContentList.size() > 0) {
					this.topIndex = reTopIndex;
					if (contentList.size() != 0) {
						for (int i = 0; i < contentList.size(); i++) {
							tempContentList.add((String) contentList.get(i));
						}
					}

					contentList = (ArrayList) tempContentList.clone();
				}

				if (contentList.size() == lineCount) {
					break;
				}
				if (contentList.size() > lineCount) {
					tempContentList = contentList;
					contentList = new ArrayList();
					for (int i = 0; i < tempContentList.size() - getLineCount(); i++) {
						this.topIndex += ((String) tempContentList.get(i))
								.length();
					}
					for (int i = 0; i < getLineCount(); i++) {
						contentList.add((String) tempContentList
								.get(tempContentList.size() - getLineCount()
										+ i));
					}

				} else if (reTopIndex == 0) {
					this.topIndex = 0;
				}

			}

			if (contentList.size() > 0) {
				ArrayList tempLineIndex = new ArrayList();
				int lineIndexSize = this.lineIndexList.size();
				lineIndex = new LineIndex();
				lineIndex.setStart(b.getStart());
				tempIndex = this.topIndex;
				lineIndex.setTopIndex(tempIndex);
				tempLineIndex.add(lineIndex);
				for (int i = 0; i < contentList.size() - lineIndexSize - 1; i++) {
					lineIndex = new LineIndex();
					lineIndex.setStart(b.getStart());
					tempIndex += ((String) contentList.get(i)).length();
					lineIndex.setTopIndex(tempIndex);
					tempLineIndex.add(lineIndex);
				}
				for (int i = 0; i < this.lineIndexList.size(); i++) {
					tempLineIndex.add((LineIndex) this.lineIndexList.get(i));
				}

				this.lineIndexList = ((ArrayList) tempLineIndex.clone());
			}
		}
		Log.i("sqli", "lineIndex ------size-----" + this.lineIndexList.size());
		Log.i("sqli",
				"lineIndex ------list-----" + this.lineIndexList.toString());
		Log.i("sqli", "contentList ------size-----" + contentList.size());
		for (int i = 0; i < contentList.size(); i++) {
			contentList.set(i, trimTail((String) contentList.get(i)));
		}
		return contentList;
	}

	public void setCharset(String enc) {
		this.enc = enc;
		if (this.cacheManager != null) {
			this.cacheManager.enc = enc;
			this.cacheManager.resetCharset();
		}
	}

	public void setDataSource(String charSet, String url) throws IOException {
		setCharset(charSet);

		this.orfb = new CutParagraph(this.enc, this.pm, this.filehandle);
		Class s = this.pm.getDefaultPlugin().getClass();
		String calseeName = s.getName();
		if (calseeName.equals("com.chinachip.books.plugin.PluginTxt")) {
			String name = url.substring(url.lastIndexOf("/") + 1,
					url.lastIndexOf("."))
					+ this.tempEX;

			File dirFiles = new File(this.temppath + name);
			boolean bFiles = dirFiles.exists();
			if (dirFiles.length() == 0L)
				dirFiles.delete();
			else if (bFiles) {
				this.orfb.isCurrVersion(this.temppath + name);
			}

			File dirFile = new File(this.temppath + name);

			boolean bFile = dirFile.exists();
			if (bFile) {
				long startt = System.currentTimeMillis();
				this.blockIndex = this.orfb.getCacheList(this.temppath + name);
				long endt = System.currentTimeMillis();
				System.out.println("getCacheList time used" + (endt - startt));
			} else {
				this.orfb.setBlockIndex(this.blockIndex);
				this.orfb.run(charSet, this.temppath, this.tempEX, url);
			}
		} else if (calseeName.equals("com.chinachip.books.plugin.PluginCtxt")) {
			Param param = new Param();
			this.pm.get(this.pm.getDefaultPlugin(), 80, param);
			this.blockIndex = param.getBlockIndex();
			this.orfb.setCharSet(this.filehandle);
		} else {
			this.orfb.setBlockIndex(this.blockIndex);
			this.orfb.run(charSet, this.temppath, this.tempEX);
		}

		this.dataType = this.orfb.getDataType();

		if (this.dataType == 3) {
			this.enc = "UTF-16BE";
		} else if (this.dataType == 4) {
			this.enc = "UTF-16LE";
		} else if (this.dataType == 5) {
			this.enc = "UTF-8";
		}

		this.cacheManager = new CacheManager(this.pm, this.cacheCount,
				this.enc, this.filehandle);

		this.cacheManager.setBlockIndex(this.blockIndex);
	}

	public int getFilehandle() {
		return this.filehandle;
	}

	public void setFilehandle(int filehandle) {
		this.filehandle = filehandle;
	}

	public void resetFile() {
		this.blockIndex = new ArrayList();
		this.orfb.setHandle(this.filehandle);
		this.orfb.setBlockIndex(this.blockIndex);
		this.orfb.start = 0L;
		this.orfb.run(this.enc, this.temppath, this.tempEX);
		this.cacheManager = new CacheManager(this.pm, this.cacheCount,
				this.enc, this.filehandle);

		this.cacheManager.setBlockIndex(this.blockIndex);
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public CacheManager getCacheManager() {
		return this.cacheManager;
	}

	public ArrayList<Long> getBlockIndex() {
		return this.blockIndex;
	}

	public void setBlockIndex(ArrayList<Long> blockIndex) {
		this.blockIndex = blockIndex;
	}

	public void setPosition(long offset) throws IOException {
		this.cacheManager.getCacheFromOffset(offset);
	}

	public ArrayList<LineIndex> getLineIndex() {
		return this.lineIndexList;
	}

	public int getHandle() {
		return this.filehandle;
	}

	public void setHandle(int filehandle) {
		this.filehandle = filehandle;
	}

	private String trimTail(String str) {
		int pos = str.indexOf("\r\n");
		if (pos < 0)
			pos = str.indexOf("\n");
		if (pos > 0)
			str = str.substring(0, pos);
		else if (pos == 0)
			str = "";
		return str;
	}

	public LineIndex getCurrLindIndex() {
		LineIndex lineIndex = new LineIndex();
		Cache a = this.cacheManager.getCacheFromID(this.topCacheID);
		lineIndex.setStart(a.getStart());
		lineIndex.setTopIndex(this.topIndex);
		return lineIndex;
	}

	public LineIndex getNextLindIndex() {
		LineIndex lineIndex = new LineIndex();
		Cache a = this.cacheManager.getCacheFromID(this.nextCacheID);
		lineIndex.setStart(a.getStart());
		lineIndex.setTopIndex(this.endIndex);
		return lineIndex;
	}

	public long getCurrIndexAt() throws UnsupportedEncodingException {
		long currIndexAt = 0L;
		Cache a = this.cacheManager.getCacheFromID(this.topCacheID);
		if ((a.getStart() <= 3L) && (this.topIndex == 0)) {
			return this.topIndex;
		}
		long nextBlockIndex = ((Long) this.cacheManager.getBlockIndex().get(
				a.getBlockId())).longValue();
		int longth = (int) (nextBlockIndex - a.getStart());
		float persent = this.topIndex / a.getData().length() * 100.0F;

		int temptopIndex = (int) (persent * longth / 100.0F);

		currIndexAt = a.getStart() + temptopIndex + 1L;

		return currIndexAt;
	}

	public long getNextIndexAt() throws UnsupportedEncodingException {
		long currIndexAt = 0L;
		Cache a = this.cacheManager.getCacheFromID(this.nextCacheID);
		long nextBlockIndex = ((Long) this.cacheManager.getBlockIndex().get(
				a.getBlockId())).longValue();
		int longth = (int) (nextBlockIndex - a.getStart());
		float persent = this.endIndex / a.getData().length() * 100.0F;

		int tempendIndex = (int) (persent * longth / 100.0F);
		currIndexAt = a.getStart() + tempendIndex + 1L;
		Plugin plugin = this.pm.getDefaultPlugin();
		long filelength = this.pm.length(plugin, this.filehandle);

		if (tempendIndex > filelength) {
			return filelength;
		}
		return currIndexAt;
	}

	private int getLastReturnANSIUTF8(ByteBuffer data, int loop) {
		loop -= 300;
		while (loop > 0) {
			byte c = data.get(loop);
			if (c == 10) {
				loop++;
				break;
			}
			loop--;
		}

		if (loop < 0)
			return 0;
		return loop;
	}

	private int getLastReturnUnicode(ByteBuffer data, int loop) {
		int i = 0;

		if (loop % 2 != 0) {
			loop--;
		}
		loop -= 200;
		while (loop > 0) {
			byte c1 = data.get(loop);
			byte c2 = data.get(loop - 1);

			if (((c1 == 0) && (c2 == 10)) || ((c1 == 10) && (c2 == 0))) {
				loop += 2;
				break;
			}
			loop -= 2;
		}
		if (loop < 0) {
			return 0;
		}
		return loop;
	}

	private int getLastReturn(ByteBuffer data, int loop) {
		if ((this.dataType == 2) || (this.dataType == 1)
				|| (this.dataType == 5)) {
			return getLastReturnANSIUTF8(data, loop);
		}
		return getLastReturnUnicode(data, loop);
	}

	private int getLastReturn(Cache cache, int loop) {
		ArrayList parageaphIndex = cache.getParagraphIndex();
		for (int i = 1; i < parageaphIndex.size(); i++) {
			if (((Integer) parageaphIndex.get(i)).intValue() >= loop)
				return ((Integer) parageaphIndex.get(i - 1)).intValue();
		}
		return 0;
	}
}
