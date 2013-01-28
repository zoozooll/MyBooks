package com.aaron.reader.util;

/**
 * 
 * @author Aaron Lee writed at 下午12:08:01 2013-1-21
 */
public class FileUtil {

	/**  扩展名
	 * @param uri
	 * @return
	 * @author writed by Aaron Lee
	 * writed at 下午12:09:05 2013-1-21
	 */
	public static String getFileEx(String uri) {
			String ex = uri.substring(uri.lastIndexOf(".") + 1, uri.length())
				.toLowerCase();
			if(ex.equals("zip")){
				if(uri.indexOf("fb2.zip")>0){
					return "fb2.zip";
				}
			} 
			return ex;
		}

		/** 不带路径不带扩展名的文件名
		 * @param uri
		 * @return
		 * @author writed by Aaron Lee
		 * writed at 下午12:09:16 2013-1-21
		 */
		public static String getFileNa(String uri) {
			return uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf("."));
		}
		
		/** 带路径不带扩展名的文件名
		 * @param uri
		 * @return
		 * @author writed by Aaron Lee
		 * writed at 下午12:10:12 2013-1-21
		 */
		public static String getFileRealname(String uri){
			return uri.substring(0, uri.lastIndexOf("/"));
		}
}
