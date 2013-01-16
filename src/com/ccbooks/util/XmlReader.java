package com.ccbooks.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XmlReader {
	String strUrl=null;
	public XmlReader(String strUrl){
		this.strUrl = strUrl;
	}
	
	/**
	 * 获得属性值
	 * @param tagName 标签
	 * @param num 属性值的位置
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	@SuppressWarnings("static-access")
	public List<String> getAttribute(String tagName) throws XmlPullParserException, IOException{
		URL url = new URL(strUrl);
		String myAttribute=null;
		List<String> listatt = new ArrayList<String>();
		XmlPullParserFactory xmlParesrFactory = XmlPullParserFactory.newInstance();
		XmlPullParser xmlPullParser = xmlParesrFactory.newPullParser();
		xmlPullParser.setInput(url.openConnection().getInputStream(),"UTF-8");
		
		int eventType=xmlPullParser.getEventType();
		
		while(eventType !=xmlPullParser.END_DOCUMENT){
			String nodeName=xmlPullParser.getName();
			switch(eventType){
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if(tagName.equals(nodeName)){
					//myAttribute=xmlPullParser.getAttributeValue(num);
					//Log.i("garmen", "取得的值为"+myAttribute);
					int count=xmlPullParser.getAttributeCount();
					for(int i=0;i<count;i++){
						listatt.add(xmlPullParser.getAttributeValue(i));
					}
					listatt.add(xmlPullParser.nextText());
					eventType = xmlPullParser.END_DOCUMENT;
				}else{
					
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			if(eventType !=xmlPullParser.END_DOCUMENT)
				eventType = xmlPullParser.next();			
		}

		return listatt;
	}
}
