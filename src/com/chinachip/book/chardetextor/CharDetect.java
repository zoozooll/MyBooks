package com.chinachip.book.chardetextor;

/**
 *  file:    CharDetect.java
 *  author:  vrix.yan
 *  date:    2011.03.25
 */




import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.mozilla.universalchardet.UniversalDetector;

public class CharDetect {
	
	private static UniversalDetector detector = new UniversalDetector(null);
	private static int last = 0;
	
	/**
	 * 函  数: start
	 * 功  能: 初始化识别引擎
	 * 参  数: 无
	 * 返  回: 无
	 * 作  者: vrix.yan 2011.03.25 11:28 
	 */	
	public void start()
	{
		if(last == 1)
			stop();
		last = 0;
		detector.reset();
	}
	
	/**
	 * 函  数: detect
	 * 功  能: 传入数据识别字符集
	 * 参  数: data [in]: 数据; offset [in]: 其实偏移; len [in]: 要识别的数据长度;
	 * 返  回: 已经识别成功了返回 true,否则返回 false
	 * 作  者: vrix.yan 2011.03.25 11:28 
	 */
	public boolean detect(byte[] data,int offset,int len) throws IOException
	{
		if(last == 2)
			throw new IOException();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(data,offset,len);
		
		byte[] buf = new byte[5120];
		
		int nread;
        while ((nread = bais.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        
        bais.close();
        last = 1;
        return detector.isDone();
	}
	
	/**
	 * 函  数: stop
	 * 功  能: 获取识别的字符集
	 * 参  数: 无
	 * 返  回: 如果识别成功，返回字符集，否则返回null
	 * 作  者: vrix.yan 2011.03.25 11:28 
	 */
	public String stop()
	{
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();

        last = 2;
        if(encoding !=null){
        	if(encoding.equals("GB18030")){
            	return "GBK";
            }
        }
        
       return encoding;
	}
}
