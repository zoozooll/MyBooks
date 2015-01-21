package com.chinachip.ccbooks.engine;

import com.chinachip.books.plugin.Plugin;
import com.chinachip.books.plugin.PluginMgr;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class CacheManager {
	PluginMgr pm = null;
	int filehandle = 0;

	private Cache[] cache = null;
	private int currentCacheID;
	private static ByteBuffer mbb = null;
	private ArrayList<Long> blockIndex = null;

	protected String enc = "GBK";

	private byte[] data = null;

	public Cache[] getCache() {
		return this.cache;
	}

	public void setCache(Cache[] cache) {
		this.cache = cache;
	}

	public String getEnc() {
		return this.enc;
	}

	public void setEnc(String enc) {
		this.enc = enc;
	}

	public CacheManager(PluginMgr pm, int size, String enc, int filehandle) {
		this.cache = new Cache[size];
		for (int i = 0; i < size; i++) {
			this.cache[i] = new Cache();
		}
		this.enc = enc;
		this.filehandle = filehandle;
		this.pm = pm;
	}

	public ArrayList<Long> getBlockIndex() {
		return this.blockIndex;
	}

	public PluginMgr getPluginMgr() {
		return this.pm;
	}

	public void setPluginMgr(PluginMgr pm) {
		this.pm = pm;
	}

	public void setBlockIndex(ArrayList<Long> blockIndex) {
		this.blockIndex = blockIndex;
	}

	public int getCurrentCacheID() {
		return this.currentCacheID;
	}

	public int getCurrentBlockID() {
		return this.cache[this.currentCacheID].getBlockId();
	}

	public boolean isLastBlock(int cacheID) {
		if ((cacheID >= 0) && (cacheID <= this.cache.length)) {
			if (this.cache[cacheID].getBlockId() >= this.blockIndex.size() - 1)
				return true;
		}
		return false;
	}

	public boolean isFirstBlock(int cacheID) {
		if ((cacheID >= 0) && (cacheID <= this.cache.length)) {
			if (this.cache[cacheID].getBlockId() <= 1)
				return true;
		}
		return false;
	}

	public Cache getCacheFromID(int id) {
		if ((id >= 0) && (id < this.cache.length))
			return this.cache[id];
		return null;
	}

	public int getCacheSize() {
		return this.cache.length;
	}

	public int getNextDataBlock() throws IOException {
		int i = 0;
		int id = this.cache[this.currentCacheID].getBlockId() + 1;
		if (id == this.blockIndex.size()) {
			return -2;
		}

		int tempID = this.currentCacheID;
		for (i = 0; i < this.cache.length; i++) {
			if (this.cache[i].getBlockId() == -1) {
				tempID = i;
			} else if (this.cache[i].getBlockId() == id) {
				return i;
			}

		}

		if (tempID != this.currentCacheID) {
			loadDataBlock(this.pm,
					getNextDataBlockStart(this.cache[this.currentCacheID]
							.getBlockId()), tempID);
			return tempID;
		}

		return loadNextDataBlock(this.pm, id);
	}

	public int getPrevDataBlock() throws IOException {
		int i = 0;
		int id = this.cache[this.currentCacheID].getBlockId() - 1;
		if (id < 1) {
			return -1;
		}
		int tempID = this.currentCacheID;
		for (i = 0; i < this.cache.length; i++) {
			if (this.cache[i].getBlockId() == -1) {
				tempID = i;
			} else if (this.cache[i].getBlockId() == id) {
				return i;
			}

		}

		if (tempID != this.currentCacheID) {
			loadDataBlock(this.pm,
					getPrevDataBlockStart(this.cache[this.currentCacheID]
							.getBlockId()), tempID);
			return tempID;
		}
		return loadPrevDataBlock(this.pm, id);
	}

	public int getCacheFromOffset(long offset) throws IOException {
		int i = 0;

		for (i = 0; i < this.cache.length; i++) {
			if (this.cache[i].getBlockId() == -1) {
				this.currentCacheID = i;
			} else if (isHas(i, offset)) {
				this.currentCacheID = i;
				return this.currentCacheID;
			}
		}

		if (i >= this.cache.length) {
			if (this.cache[this.currentCacheID].getBlockId() != -1) {
				this.currentCacheID = ChooseOptimize(this.cache[this.currentCacheID]
						.getStart());
			}

			loadDataBlock(this.pm, offset, this.currentCacheID);
		}

		return this.currentCacheID;
	}

	private boolean isHas(int i, long offset) {
		long nextStart = 0L;
		if (this.cache[i].getBlockId() >= this.blockIndex.size() - 1) {
			nextStart = this.blockIndex.get(this.blockIndex.size() - 1)
					.longValue();
		} else {
			nextStart = this.blockIndex.get(this.cache[i].getBlockId())
					.longValue();
		}

		return (this.cache[i].getStart() <= offset) && (nextStart >= offset);
	}

	private void loadDataBlock(PluginMgr pm, long offset, int cacheId)
			throws IOException {
		long start = 0L;
		long length = 0L;
		Plugin plugin = pm.getDefaultPlugin();
		long filelength = pm.length(plugin, this.filehandle);
		if (offset >= filelength) {
			start = this.blockIndex.get(this.blockIndex.size() - 2)
					.longValue();
			length = filelength - start;
			mbb = pm.map(plugin, this.filehandle, start, length);

			this.data = new byte[(int) length];

			mbb.get(this.data);
			String text = new String(this.data, this.enc);
			this.cache[cacheId].setBlockId(this.blockIndex.size() - 1);

			this.cache[cacheId].setData(text);
			this.cache[cacheId].setStart(start);
			getParagraphIndex(this.cache[cacheId]);
			return;
		}
		for (int k = 1; k < this.blockIndex.size(); k++) {
			if (this.blockIndex.get(k).longValue() <= offset)
				continue;
			start = this.blockIndex.get(k - 1).longValue();
			length = this.blockIndex.get(k).longValue()
					- this.blockIndex.get(k - 1).longValue();
			mbb = pm.map(plugin, this.filehandle, start, length);
			this.data = new byte[(int) length];
			mbb.get(this.data);
			String text = new String(this.data, this.enc);
			this.cache[cacheId].setBlockId(k);
			this.cache[cacheId].setData(text);
			this.cache[cacheId].setStart(this.blockIndex.get(k - 1)
					.longValue());
			getParagraphIndex(this.cache[cacheId]);
			return;
		}
	}

	private int loadNextDataBlock(PluginMgr pm, int blockId) throws IOException {
		int i = ChooseOptimize(this.blockIndex.get(blockId - 1)
				.longValue());

		Plugin plugin = pm.getDefaultPlugin();
		long filelength = pm.length(plugin, this.filehandle);
		long start = this.blockIndex.get(blockId - 1).longValue();
		long length = 0L;
		if (blockId < this.blockIndex.size() - 1)
			length = this.blockIndex.get(blockId).longValue() - start;
		else
			length = filelength - start;
		mbb = pm.map(plugin, this.filehandle, start, length);
		this.data = new byte[(int) length];
		mbb.get(this.data);

		String text = new String(this.data, this.enc);
		this.cache[i].setBlockId(blockId);
		this.cache[i].setData(text);
		this.cache[i].setStart(start);
		getParagraphIndex(this.cache[i]);
		return i;
	}

	private int loadPrevDataBlock(PluginMgr pm, int blockId) throws IOException {
		int i = ChooseOptimize(getPrevDataBlockStart(blockId));
		Plugin plugin = pm.getDefaultPlugin();
		long filelength = pm.length(plugin, this.filehandle);
		long start = this.blockIndex.get(blockId - 1).longValue();
		long length = 0L;
		if (blockId < this.blockIndex.size())
			length = this.blockIndex.get(blockId).longValue() - start;
		else
			length = filelength - start;
		mbb = pm.map(plugin, this.filehandle, start, length);
		this.data = new byte[(int) length];
		mbb.get(this.data);
		String text = new String(this.data, this.enc);
		this.cache[i].setBlockId(blockId);
		this.cache[i].setData(text);
		this.cache[i].setStart(start);
		getParagraphIndex(this.cache[i]);
		return i;
	}

	private int ChooseOptimize(long offset) {
		long distance = Math.abs(offset - this.cache[0].getStart());
		int j = 0;
		long temp = 0L;
		int count = 0;

		for (j = 0; j < this.cache.length; j++) {
			temp = Math.abs(offset - this.cache[j].getStart());
			if (temp > distance) {
				distance = temp;
				count = j;
			}
		}
		return count;
	}

	private long getNextDataBlockStart(int blockId) throws IOException {
		if (blockId >= this.blockIndex.get(this.blockIndex.size() - 2)
				.longValue()) {
			return this.blockIndex.get(this.blockIndex.size() - 2)
					.longValue();
		}
		return this.blockIndex.get(blockId).longValue();
	}

	private long getPrevDataBlockStart(int blockId) throws IOException {
		if (blockId <= 1) {
			return this.blockIndex.get(0).longValue();
		}
		return this.blockIndex.get(blockId - 2).longValue();
	}

	public void setCurrentCacheID(int currentCacheID) {
		this.currentCacheID = currentCacheID;
	}

	private void getParagraphIndex(Cache cache) {
		ArrayList<Integer> paragraphIndex = new ArrayList<Integer>();
		String data = cache.getData();
		int strLen = data.length();
		int indexAt = 0;
		paragraphIndex.add(0, Integer.valueOf(0));
		int temp;
		do {
			temp = data.indexOf("\n") + 1;

			data = data.substring(temp);
			indexAt += temp;
			paragraphIndex.add(Integer.valueOf(indexAt));
		} while (temp != 0);

		cache.setParagraphIndex(paragraphIndex);
	}

	public long getFileLength() {
		long fileLength = 0L;
		Plugin plugin = this.pm.getDefaultPlugin();
		fileLength = this.pm.length(plugin, this.pm.filehandle);

		if (fileLength > 0L)
			return fileLength;
		return 0L;
	}

	public void resetCharset() {
		for (int i = 0; i < this.cache.length; i++)
			if (this.cache[i].getBlockId() != -1)
				this.cache[i].setBlockId(-1);
	}
}
