package cc.software.books.effect;

public class Page
{
   public int[] buf;
   int w,h;
   public Page(int[] ibuf,int w,int h){
          this.buf = ibuf;
          this.w = w;
          this.h = h;
          }
}