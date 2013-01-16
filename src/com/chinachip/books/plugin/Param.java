package com.chinachip.books.plugin;

import java.util.ArrayList;

import com.chinachip.tree.BackList;

public class Param {
	public int i = 0, j = 0;
	private static String str = null;
	private byte[] barray = null;
	public BackList bltree = new BackList();
	public ArrayList<Long> blockIndex = new ArrayList<Long>();

	public static void setStr(String str) {
		Param.str = str;
	}

	public static String getStr() {
		return str;
	}

	public void setByteBuffer(byte[] data) {
		barray = data;
	}

	public byte[] getByteBuffer() {
		return barray;
	}
	
	public ArrayList<Long> getBlockIndex(){
		return blockIndex;
	}
	
	
	public void setBlockIndex(int index){
		Long index1 = Long.valueOf((long)index);
		System.out.println("setBlockIndex +"+index);
		blockIndex.add(index1);
	}

}