/*    */ package com.chinachip.ccbooks.engine;
/*    */ 
/*    */ import android.os.Environment;
/*    */ import java.io.File;
/*    */ 
/*    */ public class utils
/*    */ {
/*    */   public static final String DEBUG = "aiRead";
/*    */ 
/*    */   public static String getSDPath()
/*    */   {
/* 13 */     File sdDir = null;
/* 14 */     boolean sdCardExist = Environment.getExternalStorageState().equals(
/* 15 */       "mounted");
/* 16 */     if (sdCardExist) {
/* 17 */       sdDir = Environment.getExternalStorageDirectory();
/*    */     }
/*    */ 
/* 20 */     return sdDir.toString();
/*    */   }
/*    */ }

/* Location:           I:\Workspaces\eclipse3.4\CCBookPro\lib\bookcore.jar
 * Qualified Name:     com.chinachip.ccbooks.engine.utils
 * JD-Core Version:    0.6.0
 */