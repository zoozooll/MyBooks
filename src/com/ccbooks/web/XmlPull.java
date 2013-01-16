package com.ccbooks.web;

import java.io.IOException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ccbooks.vo.Book;



public class XmlPull {
	private static String strUrl = null;
	
	public XmlPull(String strUrl){
		this.strUrl = strUrl;
	}
	
	public static Book parse() throws XmlPullParserException, IOException{
		
		URL url = new URL(strUrl);
		Book book = new Book();
		XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
		XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
		xmlPullParser.setInput(url.openConnection().getInputStream(), "UTF-8");
		
		int eventType = xmlPullParser.getEventType();
		
		while(eventType != xmlPullParser.END_DOCUMENT){
			String nodeName = xmlPullParser.getName();
			
			switch(eventType){
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if(nodeName.equals("book-name")){
					book.bookname = xmlPullParser.nextText();
				}else if(nodeName.equals("bookpath")){
					book.downloadPath = xmlPullParser.nextText();
				}else if(nodeName.equals("book-id")){
					String str = xmlPullParser.nextText();
					if(!str.equals("")){					
						book.id = Integer.parseInt(str);
					}	
				}else if(nodeName.equals("size")){
					String str = xmlPullParser.nextText();
					if(!str.equals("")){
						book.fileLength = Integer.parseInt(str);
					}
				}else if(nodeName.equals("suffix")){
					book.booktype = xmlPullParser.nextText();
				}
				break;
			case XmlPullParser.END_TAG:
				break;

			}
			eventType = xmlPullParser.next();
		}
		
		return book;
	}

	public void setStrUrl(String strUrl) {
		this.strUrl = strUrl;
	}

	public String getStrUrl() {
		return strUrl;
	}
}
