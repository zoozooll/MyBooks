package com.chinachip.books.plugin;

public class PluginUtil {
	final public static int BOOK_BACKLIST = 0x00000001; //获取书籍目录树
	final public static int BOOK_AUTHOR   = 0x00000002;   // 获取书籍作者
	final public static int BOOK_COVER    = 0x00000004;   // 获取数据封面
	final public static int BOOK_TYPE  = 0x00000008;     //获取书籍类型
	final public static int BOOK_TITLE = 0x00000010;  //书籍名称
	final public static int BOOK_CHARSET  = 0x00000020;  // 书籍默认编码类型
	final public static int BOOK_BACKLIST_CHARSET  = 0x00000040;  // 书籍默认编码类型

	
	final public static int BOOK_TYPE_TEXT = 0x00000001; //文件文件
	final public static int BOOK_TYPE_COMIC = 0x00000002; //漫画文件
	final public static int BOOK_TYPE_INVALID = 0x00000004; //不支持的文件类型
	
	final public static int BOOK_CCT_BLOCKINDEX = 0x00000050;//获取CCT分块信息

	
	final public static String UNICODE = "UTF-8,UTF-16LE,UTF-16BE,UNICODE";
	final public static String CJK = "GBK,GB2312,CP936,CP932,CP950,CP949";
	
	public static void PluginInit(PluginMgr pm)
	{
		pm.registerPlugin(new PluginTxt());
		pm.registerPlugin(new PluginJar());
		pm.registerPlugin(new PluginEpub());
		pm.registerPlugin(new PluginChm());
		pm.registerPlugin(new PluginPdb());
		pm.registerPlugin(new PluginMobi());
		pm.registerPlugin(new PluginHtm());
		pm.registerPlugin(new PluginHtml());
		pm.registerPlugin(new PluginUmd());
		pm.registerPlugin(new PluginFb2());
		pm.registerPlugin(new PluginCtxt());
		pm.registerPlugin(new PluginCbz());
		return;
		
	}

	public static boolean isUnicode(String charSet)
	{
		if(UNICODE.indexOf(charSet)>= 0){
			return true;
		}
		return false;
	}
}
