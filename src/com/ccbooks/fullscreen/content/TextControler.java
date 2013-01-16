package com.ccbooks.fullscreen.content;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.ccbooks.fullscreen.bookcore.BookCore;
import com.chinachip.ccbooks.engine.LineIndex;

public class TextControler {
		public class TextCache {
			int id;
			long pos;
			int topIndex;
			int curCacheID;
			ArrayList<String> text;
			ArrayList<LineIndex> lineIndex;
			LineIndex topLineIndex;
			LineIndex endLineIndex;
		}
		
		int cacheNum;
		ArrayList<TextCache> cacheList;
		ArrayList<String> curTextArray;
		ArrayList<LineIndex> curLineIndex;
		int minReachCacheID;
		int maxReachCacheID;
		int curCacheID;
		int curLine;
		TextCache curCache;
		BookCore engine;
		boolean fgTextChang;
		
		public TextControler (BookCore engine){
			this.engine = engine;
			initCacheList();
		}
		
		void initCacheList() {
			cacheList = new ArrayList<TextCache>();
			TextCache cache = null;
			cacheNum = 3;
			minReachCacheID = 0;
			maxReachCacheID = 1;
			curCacheID = 1;
			curLine = 0;
			fgTextChang = true;
			for(int i=0;i<cacheNum;i++){
				cache = new TextCache();
				cache.id = i;
				cache.pos = -1;
				cache.text = new ArrayList<String>();
				cache.lineIndex = new ArrayList<LineIndex>();
				cacheList.add(cache);
			}
			try {
				initCache();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		void initCache() throws IOException {
			cacheList.get(curCacheID).text = engine.getCurrPage();
			cacheList.get(curCacheID).pos = engine.getCurrIndexAt();
			cacheList.get(curCacheID).topIndex = engine.getCurrTopIndexAt();
			cacheList.get(curCacheID).curCacheID = engine.getCurrTopCacheID();
			cacheList.get(curCacheID).lineIndex = engine.getLineIndex();
			cacheList.get(curCacheID).topLineIndex = engine.getCurrLindIndex();
			cacheList.get(curCacheID).endLineIndex = engine.getNextLindIndex();
			for(int i=0;i<curCacheID;i++) {
				cacheList.get(curCacheID - i - 1).text = engine.getPrevPage();
				cacheList.get(curCacheID - i - 1).pos = engine.getCurrIndexAt();	
				cacheList.get(curCacheID - i - 1).topIndex = engine.getCurrTopIndexAt();
				cacheList.get(curCacheID - i - 1).curCacheID = engine.getCurrTopCacheID();
				cacheList.get(curCacheID - i - 1).lineIndex = engine.getLineIndex();
				cacheList.get(curCacheID - i - 1).topLineIndex = engine.getCurrLindIndex();
				cacheList.get(curCacheID - i - 1).endLineIndex = engine.getNextLindIndex();
			}
			engine.getCurrInsidePage(cacheList.get(curCacheID).topIndex, cacheList.get(curCacheID).curCacheID);
			for(int i=curCacheID+1;i<cacheList.size();i++){
				cacheList.get(i).text = engine.getNextPage();
				cacheList.get(i).pos = engine.getCurrIndexAt();
				cacheList.get(i).topIndex = engine.getCurrTopIndexAt();
				cacheList.get(i).curCacheID = engine.getCurrTopCacheID();
				cacheList.get(i).lineIndex = engine.getLineIndex();
				cacheList.get(i).topLineIndex = engine.getCurrLindIndex();
				cacheList.get(i).endLineIndex = engine.getNextLindIndex();
			}
			fgTextChang = true;
		}

		void reloadCache() throws IOException {
			if(cacheList.get(curCacheID).pos >= 0)
				cacheList.get(curCacheID).text = engine.getCurrPage(cacheList.get(curCacheID).pos);
			else
				cacheList.get(curCacheID).text = engine.getCurrPage();
			cacheList.get(curCacheID).pos = engine.getCurrIndexAt();
			cacheList.get(curCacheID).topIndex = engine.getCurrTopIndexAt();
			cacheList.get(curCacheID).curCacheID = engine.getCurrTopCacheID();
			cacheList.get(curCacheID).lineIndex = engine.getLineIndex();
			cacheList.get(curCacheID).topLineIndex = engine.getCurrLindIndex();
			cacheList.get(curCacheID).endLineIndex = engine.getNextLindIndex();
			for(int i=0;i<curCacheID;i++) {
				cacheList.get(curCacheID - i - 1).text = engine.getPrevPage();
				cacheList.get(curCacheID - i - 1).pos = engine.getCurrIndexAt();			
				cacheList.get(curCacheID - i - 1).topIndex = engine.getCurrTopIndexAt();
				cacheList.get(curCacheID - i - 1).curCacheID = engine.getCurrTopCacheID();
				cacheList.get(curCacheID - i - 1).lineIndex = engine.getLineIndex();
				cacheList.get(curCacheID - i - 1).topLineIndex = engine.getCurrLindIndex();
				cacheList.get(curCacheID - i - 1).endLineIndex = engine.getNextLindIndex();
			}
			engine.getCurrInsidePage(cacheList.get(curCacheID).topIndex, cacheList.get(curCacheID).curCacheID);
			for(int i=curCacheID+1;i<cacheList.size();i++){
				cacheList.get(i).text = engine.getNextPage();
				cacheList.get(i).pos = engine.getCurrIndexAt();
				cacheList.get(i).topIndex = engine.getCurrTopIndexAt();
				cacheList.get(i).curCacheID = engine.getCurrTopCacheID();
				cacheList.get(i).lineIndex = engine.getLineIndex();
				cacheList.get(i).topLineIndex = engine.getCurrLindIndex();
				cacheList.get(i).endLineIndex = engine.getNextLindIndex();
			}
			fgTextChang = true;
		}
		
		void reloadCache(long pos) throws IOException {
			if(pos >= 0)
				cacheList.get(curCacheID).text = engine.getCurrPage(pos);
			else
				cacheList.get(curCacheID).text = engine.getCurrPage();
			cacheList.get(curCacheID).pos = engine.getCurrIndexAt();
			cacheList.get(curCacheID).topIndex = engine.getCurrTopIndexAt();
			cacheList.get(curCacheID).curCacheID = engine.getCurrTopCacheID();
			cacheList.get(curCacheID).lineIndex = engine.getLineIndex();
			cacheList.get(curCacheID).topLineIndex = engine.getCurrLindIndex();
			cacheList.get(curCacheID).endLineIndex = engine.getNextLindIndex();
			for(int i=0;i<curCacheID;i++) {
				cacheList.get(curCacheID - i - 1).text = engine.getPrevPage();
				cacheList.get(curCacheID - i - 1).pos = engine.getCurrIndexAt();		
				cacheList.get(curCacheID - i - 1).topIndex = engine.getCurrTopIndexAt();
				cacheList.get(curCacheID - i - 1).curCacheID = engine.getCurrTopCacheID();
				cacheList.get(curCacheID - i - 1).lineIndex = engine.getLineIndex();
				cacheList.get(curCacheID - i - 1).topLineIndex = engine.getCurrLindIndex();
				cacheList.get(curCacheID - i - 1).endLineIndex = engine.getNextLindIndex();
			}
			engine.getCurrInsidePage(cacheList.get(curCacheID).topIndex, cacheList.get(curCacheID).curCacheID);
			for(int i=curCacheID+1;i<cacheList.size();i++){
				cacheList.get(i).text = engine.getNextPage();
				cacheList.get(i).pos = engine.getCurrIndexAt();
				cacheList.get(i).topIndex = engine.getCurrTopIndexAt();
				cacheList.get(i).curCacheID = engine.getCurrTopCacheID();
				cacheList.get(i).lineIndex = engine.getLineIndex();
				cacheList.get(i).topLineIndex = engine.getCurrLindIndex();
				cacheList.get(i).endLineIndex = engine.getNextLindIndex();
			}
			fgTextChang = true;
		}
		
		ArrayList<String> getCurTextArray() {
			if(!fgTextChang)return curTextArray;
			curTextArray = new ArrayList<String>();
			if(curCacheID>=minReachCacheID && curCacheID<=maxReachCacheID) {
				ArrayList<String> arrStr = cacheList.get(curCacheID).text;
				for(int i=curLine;i<arrStr.size();i++) {
					curTextArray.add(arrStr.get(i));
				}
				arrStr = cacheList.get(curCacheID+1).text;
				for(int i=0;i<curLine+1;i++) {
					if(i >= arrStr.size()) break;
					curTextArray.add(arrStr.get(i));
				}
			}
			return curTextArray;
		}

		ArrayList<LineIndex> getCurLinesIndex() {
			if(!fgTextChang)return curLineIndex;
			curLineIndex = new ArrayList<LineIndex>();
			if(curCacheID>=minReachCacheID && curCacheID<=maxReachCacheID) {
				ArrayList<LineIndex> arrStr = cacheList.get(curCacheID).lineIndex;
				for(int i=curLine;i<arrStr.size();i++) {
					curLineIndex.add(arrStr.get(i));
				}
				arrStr = cacheList.get(curCacheID+1).lineIndex;
				for(int i=0;i<curLine+1;i++) {
					if(i >= arrStr.size()) break;
					curLineIndex.add(arrStr.get(i));
				}
			}
			return curLineIndex;
		}
		
		public long getCurPos() {
			return cacheList.get(curCacheID).pos;
		}

		public long getNextPos() {
			return cacheList.get(curCacheID+1).pos;
		}
		
		public int getCurLine() {
			return curLine;
		}
		
		public void setCurLine(int line) {
			curLine = line;
		}		
		
		public void reSetCurTextArray() {
			curTextArray.removeAll(curTextArray);
		}
		
		boolean nextLineRequest() throws IOException {
			upDateCache();
			if(curLine >= cacheList.get(curCacheID).text.size() -1) {
				//����һ��Cache�Ƿ�Ϊ�գ���Ϊ�����Ϸ���ʧ��
				if(curCacheID == maxReachCacheID) {
					return false;
				}
				else {
					if(cacheList.get(curCacheID+1).text.size() != 0) {
						curCacheID++;
						curLine =0;
					}
					else
						return false;
				}	
			}
			else {
				curLine++;
			}
			fgTextChang = true;
			return true;
		}
		
		boolean preLineRequest() throws IOException {	
			upDateCache();
			if(curLine <= 0) {
				//����һ��Cache�Ƿ�Ϊ�գ���Ϊ�����·���ʧ��
				if(curCacheID == minReachCacheID) {
					//the curCacheID is the first cache,you should get a new page of data.
					return false;
				}
				else
				{
					if(cacheList.get(curCacheID-1).text.size() != 0) {
						curCacheID--;
						curLine = cacheList.get(curCacheID).text.size() - 1;
					}
					else
						return false;
				}
			}
			else {
				curLine--;
			}
			fgTextChang = true;
			return true;
		}
		
	void upDateCache() {
		if(curCacheID == minReachCacheID && curLine <= (cacheList.get(curCacheID).text.size()+1)/2) {
			try {
				cacheArrayDown(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			curCacheID = minReachCacheID+1;
		}
		else if(curCacheID == maxReachCacheID && curLine > (cacheList.get(curCacheID).text.size()+1)/2) {
			try {
				cacheArrayUp(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			curCacheID = maxReachCacheID-1;
		}
	}
	
	boolean cacheArrayUp(int moveDistance) throws IOException {
		int i = 0;
		int retDistance;
		retDistance = 0;
		if(moveDistance > cacheList.size()) {
			return false;
		}
		else {
			for(i=0;i<cacheList.size() - moveDistance;i++){
				cacheList.get(i).text = cacheList.get(i+moveDistance).text;
				cacheList.get(i).pos = cacheList.get(i+moveDistance).pos;
				cacheList.get(i).topIndex = cacheList.get(i+moveDistance).topIndex;
				cacheList.get(i).curCacheID = cacheList.get(i+moveDistance).curCacheID;
				cacheList.get(i).lineIndex = cacheList.get(i+moveDistance).lineIndex;
				cacheList.get(i).topLineIndex = cacheList.get(i+moveDistance).topLineIndex;
				cacheList.get(i).endLineIndex = cacheList.get(i+moveDistance).endLineIndex;
			}
			engine.getCurrInsidePage(cacheList.get(cacheList.size() - moveDistance).topIndex, 
																	cacheList.get(cacheList.size() - moveDistance).curCacheID);
			for(i=cacheList.size() - moveDistance;i<cacheList.size();i++){
				cacheList.get(i).text = engine.getNextPage();
				cacheList.get(i).pos = engine.getCurrIndexAt();
				cacheList.get(i).topIndex = engine.getCurrTopIndexAt();
				cacheList.get(i).curCacheID = engine.getCurrTopCacheID();
				cacheList.get(i).lineIndex = engine.getLineIndex();
				cacheList.get(i).topLineIndex = engine.getCurrLindIndex();
				cacheList.get(i).endLineIndex = engine.getNextLindIndex();
			}
			fgTextChang = true;
			return true;
		}
	}
	
	boolean cacheArrayDown(int moveDistance) throws IOException {
		int i;
		if(moveDistance > cacheList.size()) {
			return false;
		}
		else {
			for(i=0;i<cacheList.size() - moveDistance;i++){
				cacheList.get(cacheList.size() - i - 1).text = cacheList.get(cacheList.size() - i - 2).text;
				cacheList.get(cacheList.size() - i - 1).pos = cacheList.get(cacheList.size() - i - 2).pos;
				cacheList.get(cacheList.size() - i - 1).topIndex = cacheList.get(cacheList.size() - i - 2).topIndex;
				cacheList.get(cacheList.size() - i - 1).curCacheID = cacheList.get(cacheList.size() - i - 2).curCacheID;
				cacheList.get(cacheList.size() - i - 1).lineIndex = cacheList.get(cacheList.size() - i - 2).lineIndex;
				cacheList.get(cacheList.size() - i - 1).topLineIndex = cacheList.get(cacheList.size() -i - 2).topLineIndex;
				cacheList.get(cacheList.size() - i - 1).endLineIndex = cacheList.get(cacheList.size() -i - 2).endLineIndex;
			}
			engine.getCurrInsidePage(cacheList.get(moveDistance).topIndex, cacheList.get(moveDistance).curCacheID);
			for(i=cacheList.size() - moveDistance;i<cacheList.size();i++) {
				cacheList.get(cacheList.size() - i - 1).text = engine.getPrevPage();
				cacheList.get(cacheList.size() - i - 1).pos = engine.getCurrIndexAt();		
				cacheList.get(cacheList.size() - i - 1).topIndex = engine.getCurrTopIndexAt();
				cacheList.get(cacheList.size() - i - 1).curCacheID = engine.getCurrTopCacheID();
				cacheList.get(cacheList.size() - i - 1).lineIndex = engine.getLineIndex();
				cacheList.get(cacheList.size() - i - 1).topLineIndex = engine.getCurrLindIndex();
				cacheList.get(cacheList.size() - i - 1).endLineIndex = engine.getNextLindIndex();
			}
			fgTextChang = true;
			return true; 
		}
	}	
}
