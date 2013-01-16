
package cc.software.books.effect;


public class PageTurn
{
   public int handle = 0;
   public Page pg1,pg2,pg3,pg4;
   public Rect rect1,rect2;
   private Point pos;
   
   public int onCreate(int x,int y){
	      pos.x = x;
	      pos.y = y;
          handle = pt_create(pg1,pg2,pg3,pg4,rect1,rect2,pos); 
          return handle;
          };

   public int[] pageTurn(int x,int y,int flag){
	      pos.x = x;
	      pos.y = y;
          return pt_pageturn(handle,pos,flag);
          };

   public void onDestroy(){
          pt_destroy(handle);
          };

   //--------------------------------------------------------------------- native api
   private native int pt_create(Page pg1,Page pg2,Page pg3,Page dst,Rect draw_rect,Rect rect,Point mouse);
   private native int[] pt_pageturn(int h,Point mouse,int flag);
   private native void pt_destroy(int h);
   
	
	public class Point
	{
	   public int x,y;
	   public Point(int x,int y){
	     this.x = x;
	     this.y = y;
	   }
	}

   static {
	System.loadLibrary("pt");
   };
};
