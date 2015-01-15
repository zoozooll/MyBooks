 package com.chinachip.ccbooks.engine;
 
 import com.chinachip.books.plugin.Plugin;
 import com.chinachip.books.plugin.PluginMgr;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.io.PrintStream;
 import java.io.RandomAccessFile;
 import java.io.UnsupportedEncodingException;
 import java.nio.ByteBuffer;
 import java.util.ArrayList;
 
 public class CutParagraph
 {
   public PluginMgr pm = null;
   public Plugin plugin = null;
 
   private ByteBuffer mbb = null;
   private int MAX_BUF_SIZE = 51200;
   private String MBCS = "GBK,GB2312,CP936,CP949,CP932,CP950,BIG5,EUC-CN,EUC-JP,EUC-KR,SHIFT-JIS";
 
   private long CACHE_BLOCK_SIZE = 5120L;
   private long CACHE_BLOCK_MIN = 1024L;
 
   public final short DT_SINGLEBYTE = 1;
   public final short DT_MULTIBYTES = 2;
   public final short DT_UNICODEBE = 3;
   public final short DT_UNICODELE = 4;
   public final short DT_UTF8 = 5;
 
   private short datatype_default = 1;
 
   public long start = 0L;
 
   private ArrayList<Long> blockIndex = null;
   private RandomAccessFile raf = null;
   private String charSet = null;
   private final String slash = "/";
   private String verMsg = "ccbooksv0001--------";
 
   int handle = 0;
 
   public void setHandle(int handle) {
     this.handle = handle;
   }
 
   public CutParagraph(String encode, PluginMgr pm, int handle)
   {
     this.charSet = encode;
     this.pm = pm;
     this.plugin = pm.getDefaultPlugin();
     this.blockIndex = new ArrayList();
     this.handle = handle;
   }
 
   public void setBlockIndex(ArrayList<Long> blockIndex)
   {
     this.blockIndex = blockIndex;
   }

 public ArrayList<Long> getBlockIndex() {
   return this.blockIndex;
 }

 public void run(String charSet, String path, String tempEX)
 {
   this.charSet = charSet;

   calcDataBlock(this.pm, this.handle);
 }

 public void run(String charSet, String path, String tempEX, String fileName)
 {
   this.charSet = charSet;

   String name = fileName
     .substring(fileName.lastIndexOf("/") + 1, 
     fileName.lastIndexOf(".")) + 
     tempEX;
   creatFile(name, path, tempEX);
   calcDataBlock(this.pm, this.handle);
   appendMethodA(path + name, transArrayToString(this.blockIndex).toString());
 }

 public void setDataType(short type) {
   this.datatype_default = type;
 }

 public short getDataType()
 {
   return this.datatype_default;
 }

 private long getLastReturnUnicode(ByteBuffer slice, long start) {
   int loop = slice.capacity() - 1; int i = 0;
   byte[] c = new byte[2];
   while (loop > 0)
   {
     c[0] = slice.get(loop--);
     c[1] = slice.get(loop--);
       i += 2;
       if (isReturn(c)) {
         loop++;
         loop += 2;
 
         i -= 2;
         break;
       }
     }
 
     if (i >= this.CACHE_BLOCK_MIN) {
       loop = (int)calcCacheTail(slice, loop);
     }
 
     start += loop;
 
     this.blockIndex.add(Long.valueOf(start));
     return start;
   }
 
   private long calcCacheTail(ByteBuffer slice, long loop) {
     int size = slice.capacity();
     int max = size - 1;
     long num = 0L;
     switch (this.datatype_default)
     {
     case 1:
     case 3:
     case 4:
       return size;
     case 2:
     }
 
     while (max >= loop)
     {
       byte c = slice.get(max--);
       if (isMinus(c))
         continue;
       max += 2;
       break;
     }
 
     if (max < 0)
       max = size;
     else if (max < loop)
       max = (int)loop;
     return max;
   }
 
   private boolean isReturn(byte c)
   {
     return c == 10;
   }
 
   public boolean isReturn(byte[] c)
   {
     if ((c[0] == 10) && (c[1] == 0))
       return true;
     if ((c[1] == 10) && (c[0] == 0))
       return true;
     if (c[0] == 10) {
       return true;
     }
     return c[1] == 10;
   }
 
   public boolean isMinus(byte c)
   {
     return c < 0;
   }
 
   private long getLastReturnANSI(ByteBuffer slice, long start)
   {
     long loop = slice.capacity() - 1; long i = 0L;
 
     while (loop > 0L) {
       byte c = slice.get((int)loop);
 
       if (isReturn(c)) {
         loop += 1L;
         i -= 1L;
         break;
       }
       loop -= 1L;
       i += 1L;
     }
 
     if (i >= this.CACHE_BLOCK_MIN) {
       loop = (int)calcCacheTail(slice, loop);
     }
 
     if (loop < 0L)
       loop = 0L;
     start += loop;
     this.blockIndex.add(Long.valueOf(start));
     return start;
   }
 
   public long calcDataBlock(PluginMgr pm, int handle, long start, long length)
   {
     long len = pm.length(this.plugin, handle);
     if (start + length > len)
       length = len - start - 1L;
     else {
       length = this.CACHE_BLOCK_SIZE;
     }
     if (this.mbb != null) {
       this.mbb.flip();
       this.mbb.clear();
     }
 
     this.mbb = pm.map(this.plugin, handle, start, length);
 
     if (start == 0L)
     {
       byte a1 = this.mbb.get(0);
       byte a2 = this.mbb.get(1);
       byte a3 = this.mbb.get(2);
       if ((a2 == -1) && (a1 == -2)) {
         this.datatype_default = 3;
         start += 2L;
       } else if ((a1 == -1) && (a2 == -2)) {
         this.datatype_default = 4;
         start += 2L;
       } else if ((a1 == -17) && (a2 == -69) && 
         (a3 == -65)) {
         this.datatype_default = 5;
         start += 3L;
       }
       else if (this.charSet.equals("UTF-8")) {
         this.datatype_default = 5;
       } else if (this.charSet.equals("UTF-16BE")) {
         this.datatype_default = 3;
       } else if (this.charSet.equals("UTF-16LE")) {
         this.datatype_default = 4;
       } else if (this.MBCS.indexOf(this.charSet) >= 0) {
         this.datatype_default = 2;
       } else {
         this.datatype_default = 1;
       }
       this.blockIndex.add(Long.valueOf(start));
       this.mbb.position((int)start);
     }
 
     if ((this.datatype_default == 3) || 
       (this.datatype_default == 4))
     {
       return getLastReturnUnicode(this.mbb, start);
     }
 
     return getLastReturnANSI(this.mbb, start);
   }
 
   public long calcDataBlock(PluginMgr pm, int handle)
   {
     long laststart = 0L;
     long len = pm.length(this.plugin, handle);
     if (len < this.CACHE_BLOCK_SIZE) {
       this.blockIndex.add(Long.valueOf(this.start));
       this.blockIndex.add(Long.valueOf(len));
       return 0L;
     }
     if (this.blockIndex.isEmpty())
       this.start = 0L;
     else {
       this.start = ((Long)this.blockIndex.get(this.blockIndex.size() - 1)).longValue();
     }
     laststart = this.start;
     while (len - (this.start - laststart) > this.CACHE_BLOCK_SIZE) {
       this.start = (int)calcDataBlock(pm, handle, this.start, this.CACHE_BLOCK_SIZE);
     }
     this.blockIndex.add(Long.valueOf(len));
     return this.start;
   }
 
   private void creatFile(String fileName, String path, String tempEX)
   {
     boolean bFile = false;
     try
     {
       File dirFile = new File(path);
       bFile = dirFile.exists();
 
       if (bFile)
       {
         System.out.println("The folder exists.");
       }
       else {
         System.out
           .println("The folder do not exist,now trying to create a one...");
         bFile = dirFile.mkdir();
         if (bFile)
           System.out.println("Create successfully!");
         else {
           System.out
             .println("Disable to make the folder,please check the disk is full or not.");
         }
 
       }
 
       System.out.println("Now we put files in to the folder...");
 
       fileName = fileName.substring(fileName.lastIndexOf("/") + 1, 
         fileName.lastIndexOf("."));
       String sFileName = new String(path);
       sFileName = sFileName + fileName;
       File tempFile = new File(sFileName + tempEX);
       bFile = tempFile.createNewFile();
     }
     catch (IOException localIOException)
     {
     }
 
     if (bFile)
       System.out.println("Successfully put files in to the folder!");
     else
       System.out.println("Sorry sir,i don't finish the job!");
   }
 
   private void appendMethodA(String fileName, String content)
   {
     try
     {
       RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
       File file = new File(fileName);
 
       boolean bFile = file.exists();
       if (bFile) {
         randomFile.seek(0L);
         randomFile.writeBytes(content);
       }
 
       randomFile.close();
     } catch (IOException e) {
       e.printStackTrace();
     }
   }
 
   public boolean isCurrVersion(String tmpFile)
   {
     InputStreamReader isr = null;
     try {
       isr = new InputStreamReader(
         new FileInputStream(tmpFile), "GBK");
     }
     catch (UnsupportedEncodingException e) {
       e.printStackTrace();
     }
     catch (FileNotFoundException e) {
       e.printStackTrace();
     }
     char[] bs = new char[this.MAX_BUF_SIZE];
     int readNum = -1;
     StringBuffer sb = new StringBuffer(this.MAX_BUF_SIZE);
     String str = "";
     while (true) {
       try {
         readNum = isr.read(bs, 0, bs.length);
       }
       catch (IOException e) {
         e.printStackTrace();
       }
       if (readNum == -1) {
         str = str + new String(sb);
         break;
       }
       sb.append(bs, 0, bs.length);
       if (sb.length() > this.MAX_BUF_SIZE * 100) {
         str = str + new String(sb);
         sb.delete(0, sb.length());
       }
     }
     try
     {
       isr.close();
     }
     catch (IOException e) {
       e.printStackTrace();
     }
     String tempverMsg = str.substring(0, 20);
     if (tempverMsg.contains(this.verMsg))
     {
       return true;
     }
     File file = new File(tmpFile);
     file.delete();
 
     return false;
   }
 
   public ArrayList<Long> getCacheList(String tmpFile)
     throws IOException
   {
     InputStreamReader isr = new InputStreamReader(
       new FileInputStream(tmpFile), "GBK");
 
     char[] bs = new char[this.MAX_BUF_SIZE];
     int readNum = -1;
     StringBuffer sb = new StringBuffer(this.MAX_BUF_SIZE);
     String str = "";
     while (true) {
       readNum = isr.read(bs, 0, bs.length);
       if (readNum == -1) {
         str = str + new String(sb);
         break;
       }
       sb.append(bs, 0, bs.length);
       if (sb.length() > this.MAX_BUF_SIZE * 100) {
         str = str + new String(sb);
         sb.delete(0, sb.length());
       }
     }
 
     str = str.substring(20);
     String[] indexestrs = str.split("&");
     Long[] indexes = new Long[indexestrs.length - 1];
 
     for (int i = 0; i < indexestrs.length - 1; i++) {
       try {
         indexes[i] = Long.valueOf(indexestrs[i]);
       }
       catch (NumberFormatException e) {
         e.printStackTrace();
       }
     }
 
     ArrayList blockindex = new ArrayList();
     for (int i = 0; i < indexestrs.length - 1; i++) {
       blockindex.add(i, indexes[i]);
     }
 
     return blockindex;
   }
 
   private StringBuffer transArrayToString(ArrayList<Long> blockIndex) {
     StringBuffer sb = new StringBuffer(this.MAX_BUF_SIZE);
     sb.append(this.verMsg);
     for (int i = 0; i < blockIndex.size(); i++) {
       sb.append(blockIndex.get(i) + "&");
     }
     return sb;
   }
 
   public void setCharSet(int handle) throws IOException
   {
     long start = 0L;
     long length = 10L;
     long len = this.pm.length(this.plugin, handle);
     if (start + length > len) {
       length = len - start - 1L;
     }
 
     if (this.mbb != null) {
       this.mbb.flip();
       this.mbb.clear();
     }
 
     this.mbb = this.pm.map(this.plugin, handle, start, length);
 
     if (start == 0L)
     {
       byte a1 = this.mbb.get(0);
       byte a2 = this.mbb.get(1);
       byte a3 = this.mbb.get(2);
       if ((a2 == -1) && (a1 == -2)) {
         this.datatype_default = 3;
         start += 2L;
       } else if ((a1 == -1) && (a2 == -2)) {
         this.datatype_default = 4;
         start += 2L;
       } else if ((a1 == -17) && (a2 == -69) && 
         (a3 == -65)) {
         this.datatype_default = 5;
         start += 3L;
       }
       else if (this.charSet.equals("UTF-8")) {
         this.datatype_default = 5;
       } else if (this.charSet.equals("UTF-16BE")) {
         this.datatype_default = 3;
       } else if (this.charSet.equals("UTF-16LE")) {
         this.datatype_default = 4;
       } else if (this.MBCS.indexOf(this.charSet) >= 0) {
         this.datatype_default = 2;
       } else {
         this.datatype_default = 1;
       }
     }
   }
 
   public int GetUtf8ByteNumForWord(byte firstCh)
   {
     byte temp = -128;
     int num = 0;
 
     while ((temp & firstCh) == temp) {
       num++;
       temp = (byte)(temp >> 1);
     }
     if (num == 0) {
       return 1;
     }
     return num;
   }
 }
