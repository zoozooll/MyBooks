/*    */ package com.chinachip.ccbooks.engine;
/*    */ 
/*    */ public class LineIndex
/*    */ {
/*    */   private long start;
/*    */   private int topIndex;
/*    */ 
/*    */   public long getStart()
/*    */   {
/*  7 */     return this.start;
/*    */   }
/*    */   public void setStart(long start) {
/* 10 */     this.start = start;
/*    */   }
/*    */   public int getTopIndex() {
/* 13 */     return this.topIndex;
/*    */   }
/*    */   public void setTopIndex(int topIndex) {
/* 16 */     this.topIndex = topIndex;
/*    */   }
/*    */ 
/*    */   public boolean compareTo(LineIndex lineIndex) {
/* 20 */     return (getStart() == lineIndex.getStart()) && (getTopIndex() == lineIndex.getTopIndex());
/*    */   }
/*    */ }

/* Location:           I:\Workspaces\eclipse3.4\CCBookPro\lib\bookcore.jar
 * Qualified Name:     com.chinachip.ccbooks.engine.LineIndex
 * JD-Core Version:    0.6.0
 */