package com.ccbooks.fullscreen.content;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.ccbooks.view.R;
import com.ccbooks.view.TextReader;
import com.chinachip.TextReader.Line;
import com.chinachip.ccbooks.engine.LineIndex;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Scroller;
import android.widget.Toast;

public class CommentView extends View {
	private Context mContext;
	private Paint touchPaint;
	private Paint mBitmapPaint;
	private Bitmap mBitmap;
	private Path mPath;
	private int orientation = 0;
	private int screenWidth;
	private int screenHeight;
	private float lastX;
	private float lastY;
	private long lastTime;
	private boolean mIsLongPressed = false;
	private Paint restorePaint;
	
	public static final String COMMENTPATH = "/sdcard/cctemp/";
	  
	private PointF globalPoint = new PointF();

	public static final int TOUCH_TOLERANCE = 8;//移动大于4则记录， touchmove用

//	public static final int X = 0;
//	public static final int Y = 1;

	TextReader mTextReader;
	
	private Line line;
	private List<Line> lineList = new ArrayList<Line>();//存储各条连续线
	private HashMap<String, List<Line>> lineListFont;
	private static final String TAG = "CommentView";
	
	
	public List<Line> getLineList() {
		return lineList;
	}
	
	public void setLineList(List<Line> readXml) {
		// TODO Auto-generated method stub
		lineList = readXml;
		
		lineListFont = new HashMap<String, List<Line>>();
		initlineListFont();
		return;
	}

	public void initlineListFont() {
		
		if(lineListFont == null){
			lineListFont = new HashMap<String, List<Line>>();
		}
		
		for(int fontSize = 18;fontSize < 38;fontSize=fontSize+2){
			ArrayList<Line> tempLineList = new ArrayList<Line>();
			for(Line tempLine :lineList){
				if(fontSize == tempLine.getFontSize()){
					tempLineList.add(tempLine);
				}
			}
			if(tempLineList.size() != 0){
				lineListFont.put(fontSize+"", tempLineList);
				tempLineList = null;
			}
		}
	}


	public CommentView(Context myContext){
		super(myContext);
		mContext = myContext;
		mTextReader = (TextReader)mContext;
		
		initPaint();
		initTouchView();
	}
	
	public CommentView(Context myContext, AttributeSet attrs, int defStyle) {
		super(myContext, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = myContext;
		mTextReader = (TextReader)mContext;
		
		initPaint();
		initTouchView();
		
	}

	public CommentView(Context myContext, AttributeSet attrs) {
		super(myContext, attrs);
		// TODO Auto-generated constructor stub
		
		mContext = myContext;
		mTextReader = (TextReader)mContext;
		
		initPaint();
		initTouchView();
		
	}
	
	/**
	 * 获得屏幕的宽高
	 * 
	 * Created on 2011-5-11  上午11:22:46
	 */
	public void getScreenWH() {
		WindowManager windowManager = mTextReader.getWindowManager();    
    	Display display = windowManager.getDefaultDisplay();    	
    	screenWidth = display.getWidth();    
    	screenHeight = display.getHeight();
	}


	private void initTouchView(){
//		recycleBitmap();
		getScreenWH();

		if(orientation != this.getResources().getConfiguration().orientation || mBitmap == null){
			recycleBitmap();
			mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_4444);
			orientation = this.getResources().getConfiguration().orientation;
		}	
		mTextReader.transpixel= new int[screenWidth*screenHeight];
		mBitmap.setPixels(mTextReader.transpixel, 0, screenWidth, 0, 0, screenWidth, screenHeight);


		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

	}
	


	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(mBitmap, 0, 0,mBitmapPaint);
		canvas.drawPath(mPath, touchPaint);
	}


	/**
	 * isLongPressed是检测长按事件
	 * @author lamkaman
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();
		TextReader mTextReader = (TextReader)mContext;
		if(mTextReader.getMenu().isMenuShowed()){	//当打开menu时把事件传到mTextReader隐藏批注
			mTextReader.onTouchEvent(event);
			mIsLongPressed = false;
			return true;
		}
		
		if(!mTextReader.getIsComment()){ //当不是批注时将事件传到下层的view
			if(mTextReader.getReadMode() == 1){
				mTextReader.getScrollerMain().onTouchEvent(event);
			}else{
				mTextReader.threeDimMain.getPageViews().onTouchEvent(event);
			}
			return true;
		}
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				lastX = x;
				lastY = y;
				lastTime = event.getDownTime();
				touch_start(x, y);
				postInvalidate();
				mTextReader.stopCount();
				break;
			case MotionEvent.ACTION_MOVE:
				if(!mIsLongPressed){
					mIsLongPressed = isLongPressed(lastX,lastY,x,y,lastTime,event.getEventTime(),500);
					touch_move(x, y);
					postInvalidate();
				}else{
					if(!mTextReader.getMenu().isMenuShowed()){
						if(line != null){
							line =null;
						}
						mTextReader.getMenu().show();
						
					}
				}
	
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				postInvalidate();
				if(!mTextReader.getMenu().isMenuShowed()){
					mTextReader.startCount();
				}	
				mIsLongPressed = false;
				break;
		}
		return true;
	}

	
	private boolean isLongPressed(float lastX2, float lastY2, float x, float y,
			float lastTime2, long currentTimeMillis, int i) {
		// TODO Auto-generated method stub
		float offsetX = Math.abs(x - lastX2);
		float offsetY = Math.abs(y - lastY2);
		long intervalTime = (long) (currentTimeMillis - lastTime2);
		if(offsetX <= 7 && offsetY <= 7 && intervalTime >= i){
			Log.i("garmen", "true");
			return true;
		}
		Log.i("garmen", "false,offsetX="+offsetX+",offsetY="+offsetY+",intervalTime="+intervalTime);
		return false;
	}

	private void touch_start(float x,float y){
		mPath.reset();
		mPath.moveTo(x, y);
		globalPoint.x = x;
		globalPoint.y = y;
		saveCoordinate(globalPoint);
	}

	private void touch_move(float x, float y){
		float dx = Math.abs(x - globalPoint.x);
		float dy = Math.abs(y - globalPoint.y);
		if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
			mPath.quadTo(globalPoint.x, globalPoint.y, (x+globalPoint.x)/2, (y+globalPoint.y)/2);
			globalPoint.x = x;
			globalPoint.y = y;
			saveCoordinate(globalPoint);
//			mCount++;
		}
	}
	
	private void touch_up(){
		Canvas mCanvas = new Canvas(mBitmap);
		
		saveCoordinate(globalPoint);
		if(mTextReader.getScrollerMain().getVisibility() == View.VISIBLE){
			int size = mTextReader.getScrollerMain().getCurLinesIndex().size()-1;
			if(size ==0){
				LineIndex tempLineIndex = new LineIndex();
				tempLineIndex.setStart(Long.MIN_VALUE);
				tempLineIndex.setTopIndex(Integer.MIN_VALUE);
				line.setStartLineIndex(tempLineIndex);
				line.setEndLineIndex(tempLineIndex);
			}else{
				line.setStartLineIndex(mTextReader.getScrollerMain().getCurLinesIndex().get(0));
				line.setEndLineIndex(mTextReader.getScrollerMain().getCurLinesIndex().get(size-1));
			}
		}else{
			int size = mTextReader.threeDimMain.lineIndexs.size() - 1;
			line.setStartLineIndex(mTextReader.threeDimMain.lineIndexs.get(0));
			line.setEndLineIndex(mTextReader.threeDimMain.lineIndexs.get(size));
		}
		line.setLinePaintOffset(mTextReader.getLinePrintOffset());
		line.setFontSize(mTextReader.getTextSize());
		line.setFile(mTextReader.file);
		Line tempLine = line;

		if(lineList == null){
			lineList = new ArrayList<Line>();
		}
		lineList.add(tempLine);
		/*HashMap添加*/
		if(lineListFont == null){
			lineListFont = new HashMap<String, List<Line>>();
		}
		if(lineListFont.get(tempLine.getFontSize()+"") != null){
			lineListFont.get(tempLine.getFontSize()+"").add(tempLine);
		}else{
			ArrayList<Line> lineList = new ArrayList<Line>();
			lineList.add(tempLine);
			lineListFont.put(tempLine.getFontSize()+"", lineList);
		}

		
		line = null;
		mPath.lineTo(globalPoint.x, globalPoint.y);
		mCanvas.drawPath(mPath, touchPaint);
		mPath.reset();
	}
	
	private void restore_start(float x, float y){
		mPath.reset();
		mPath.moveTo(x, y);
		globalPoint.x = x;
		globalPoint.y = y;
	}
	
	private void restore_up(){
		Canvas mCanvas = new Canvas(mBitmap);
		
		mPath.lineTo(globalPoint.x, globalPoint.y);
		mCanvas.drawPath(mPath, restorePaint);
		mPath.reset();
	}
	
	private void restore_move(float x,float y){
		mPath.quadTo(globalPoint.x, globalPoint.y, (x+globalPoint.x)/2, (y+globalPoint.y)/2);
		globalPoint.x = x;
		globalPoint.y = y;
	}
	
	private void initPaint(){
		touchPaint = new Paint();
		touchPaint.setAntiAlias(true);
		touchPaint.setDither(true);
		touchPaint.setColor(Color.RED);
		touchPaint.setStyle(Paint.Style.STROKE);
		touchPaint.setStrokeCap(Paint.Cap.ROUND);
		touchPaint.setStrokeJoin(Paint.Join.ROUND);
		touchPaint.setStrokeWidth(Line.INITWITDH);
		restorePaint = new Paint();
		restorePaint.setAntiAlias(true);
		restorePaint.setDither(true);
		restorePaint.setColor(Color.RED);
		restorePaint.setStyle(Paint.Style.STROKE);
		restorePaint.setStrokeCap(Paint.Cap.ROUND);
		restorePaint.setStrokeJoin(Paint.Join.ROUND);
		restorePaint.setStrokeWidth(Line.INITWITDH);
	}
	
	public void clearView(){
		Log.i(TAG, "ClearView");
		initTouchView();
		postInvalidate();

	}

	private void recycleBitmap() {
		if(mBitmap != null && !mBitmap.isRecycled()){
			mBitmap.recycle();
		}
		System.gc();
	}
	
	public void clearLineList(){
		lineList = new ArrayList<Line>();
	}
	
	/**
	 * 删除整本书的批注
	 * 
	 * Created on 2011-4-20  下午02:25:21
	 */
	public void deleteAllComment(){
		if(lineList == null){
			return;
		}
		lineList = null;
		lineListFont = null;
		
		File commentFile1 = new File(COMMENTPATH+mTextReader.getBook().bookname+Configuration.ORIENTATION_LANDSCAPE+".xml");
		File commentFile2 = new File(COMMENTPATH+mTextReader.getBook().bookname+Configuration.ORIENTATION_PORTRAIT+".xml");
		if(commentFile1.exists()){
			commentFile1.delete();
		}
		if(commentFile2.exists()){
			commentFile2.delete();
		}
		Toast.makeText(mTextReader, R.string.delcomplete, Toast.LENGTH_LONG).show();
		scrollPaint();
		
	}
	
	/**
	 * 删除屏幕上的批注
	 * 
	 * Created on 2011-4-8  上午08:56:54
	 */
	public void deleteComment(){
		if(lineList == null){
			return;
		}
		List<Line> deleteList = new ArrayList<Line>();
		Long offsetY = 0L;
		
		for(Line line:lineList){
			if(mTextReader.getTextSize() == line.getFontSize()){
				int newLine = mTextReader.calculateLinesOffsetByIndex(line.getStartLineIndex());
				if(newLine < 0){
					newLine = mTextReader.calculateLinesOffsetByIndex(line.getEndLineIndex());
					if(newLine>=0){
						newLine = newLine - mTextReader.bc.getLineCount();
						offsetY = (long)(newLine*
							(mTextReader.getTextSize()+mTextReader.bc.getLineSpace())+mTextReader.getLinePrintOffset());
					}else{
						continue;
					}
				}else{
					offsetY = (long) ((newLine)*
							(mTextReader.getTextSize()+mTextReader.bc.getLineSpace())+mTextReader.getLinePrintOffset());
				}
			}
			
			if(line.getMaxY()+offsetY-line.getLinePaintOffset() > 0 && line.getMaxY()+offsetY-line.getLinePaintOffset()<screenHeight
					|| line.getMinY()+offsetY-line.getLinePaintOffset() > 0 && line.getMinY()+offsetY-line.getLinePaintOffset()<screenHeight){
				deleteList.add(line);
			}
		}
		if(deleteList.size() == 0){
			Toast.makeText(mContext, R.string.nocomment, Toast.LENGTH_SHORT).show();
		}else{
			for(int i=0;i<deleteList.size();i++){
				lineList.remove(deleteList.get(i));
			}
			Toast.makeText(mTextReader, R.string.delcomplete, Toast.LENGTH_LONG).show();
		}	
		lineListFont = null;
		initlineListFont();
		
		mTextReader.CommentPaint();
		if(mTextReader.getIsComment()){
			scrollPaint();
		}
	}
	/**
	 * 画线轨迹保存为图片
	 * 
	 * Created on 2011-3-23  下午04:38:24
	 */
	public void saveView(){
		if(mBitmap == null){
			return;
		}
		
		ByteArrayOutputStream  baos = new ByteArrayOutputStream();
		mBitmap.compress(CompressFormat.PNG, 100, baos);
		File file = new File("/sdcard/test.png");
		FileOutputStream stream;
		try {
			stream = new FileOutputStream(file);
			baos.writeTo(stream);
			baos = null;
			stream.flush();
			stream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public Paint getTouchPaint(){
		return touchPaint;
	}
	
	public void setTouchPaint(Paint inPaint){
		touchPaint = inPaint;
	}
	
	/**
	 * 保存坐标
	 * @param x
	 * @param y
	 * Created on 2011-3-23  下午04:39:02
	 */
	private void saveCoordinate(PointF globalPoint){
		if(line == null){
			line = new Line(touchPaint.getColor(),(int)touchPaint.getStrokeWidth());
		}
		PointF tempPoint = new PointF();
		tempPoint.x = globalPoint.x;
		tempPoint.y = globalPoint.y;
		if(line.getMaxY()<tempPoint.y){
			line.setMaxY(tempPoint.y);
		}
		if(line.getMinY()>tempPoint.y){
			line.setMinY(tempPoint.y);
		}
		line.getCoordinateList().add(tempPoint);
	}
	
	
	public void scrollPaint(){
		clearView();
		paintView();
	}
	
	public void clearCoordinateView(){
		clearView();
		line = null;
		lineList = null;
	}
	
	/**
	 * 画线
	 * 
	 * Created on 2011-4-8  上午09:05:44
	 * @author Lam Ka Man
	 */
	public void paintView(){
		Log.i(TAG, "PaintView");
		if(lineList == null){
			return;
		}
		if(lineListFont == null){
			return;
		}
		
		PointF getPoint = new PointF();
		int systemTextSize = mTextReader.getTextSize();
		int filenum = mTextReader.file;
		
		ArrayList<Line> templineListFont = (ArrayList<Line>) lineListFont.get(systemTextSize+"");
		
		if(templineListFont == null){
			return;
		}
		
		for(int n = 0;n<templineListFont.size();n++){	//这里是遍历每条线
			line = templineListFont.get(n);
			if(line.getFile() == filenum){
				if(mTextReader.getTextSize() == line.getFontSize()){   //字体相等才画线
					restorePaint.setColor(line.getColor());
					restorePaint.setStrokeWidth(line.getWidth());
					long offsetY;
					
					int newLine = mTextReader.calculateLinesOffsetByIndex(line.getStartLineIndex());//不断变化的
					if(newLine < 0){
						newLine = mTextReader.calculateLinesOffsetByIndex(line.getEndLineIndex())+1;
						if(newLine <= 0){
							//线不在屏幕上，跳过
							continue;
						}
						newLine = newLine - mTextReader.bc.getLineCount();
						offsetY = (long)(newLine*
							(mTextReader.getTextSize()+mTextReader.getLineSpace())+mTextReader.getLinePrintOffset());
						
					}else{
						offsetY = (long) ((newLine)*
							(mTextReader.getTextSize()+mTextReader.getLineSpace())+mTextReader.getLinePrintOffset());
	
					}	
					
					
					for(int i = 0; i < line.getCoordinateList().size();i++){	
						//这里开始画线
						if(i == 0){
							getPoint.x = line.getCoordinateList().get(i).x;
							getPoint.y = line.getCoordinateList().get(i).y-line.getLinePaintOffset()+offsetY;
							restore_start(getPoint.x, getPoint.y);
							postInvalidate();
						}else if(i == line.getCoordinateList().size()-1){
							globalPoint.x = line.getCoordinateList().get(i).x;
							globalPoint.y = line.getCoordinateList().get(i).y-line.getLinePaintOffset()+offsetY;
							restore_up();
							postInvalidate();
						}else{
							getPoint.x = line.getCoordinateList().get(i).x;
							getPoint.y = line.getCoordinateList().get(i).y-line.getLinePaintOffset()+offsetY;
							restore_move(getPoint.x, getPoint.y);
							postInvalidate();
						}
					}
				}	
			}
		}	
		line=null;
	}

/**
 * 	public static final String LINE = "line";
	public static final String COLOR = "color";
	public static final String MAXY = "maxY";
	public static final String MINY = "minY";
	public static final String COORDINATE = "coordinate";
	public static final String X = "x";
	public static final String Y = "y";
	public static final String COMMENT = "comment";
	public static final String WIDTH = "width";
	public static final String STARTPOS = "startPos";
	public static final String ENDPOS = "endPos";
	public static final String STARTLINEINDEX_START = "startlineindex_start";
	public static final String STARTLINEINDEX_TOP = "startlineindex_top";
	public static final String ENDLINEINDEX_START = "endlineindex_start";
	public static final String ENDLINEINDEX_TOP = "endlineindex_top";
	public static final String LINEPAINTOFFSET = "linepaintoffset";
	public static final String FONTSIZE = "fontsize";
	public static final String FILE = "file";
 */
	
	/**
	 * 生成xml文件
	 * 	<comment name="" orientation="" fontsize="">
	 *		<line color="" width="" maxY="" minY="">
	 *			<coordinate x="" y="" />
	 *		</line>	
	 *	</comment>
	 * 
	 * Created on 2011-3-31  下午05:08:59
	 */
	public void writeXml(Writer writer,int orientation){
		
		if(lineList == null){
			return;
		}
		XmlSerializer xmlSer = Xml.newSerializer();
		
		try {
			xmlSer.setOutput(writer);
			xmlSer.startDocument("UTF-8", true);
			xmlSer.text("\n");
			xmlSer.startTag(null, "comment");
			for(Line templine :lineList){
				xmlSer.text("\n\t");
				xmlSer.startTag(null, "line");
				xmlSer.attribute(null, "color", templine.getColor()+"");
				xmlSer.attribute(null, "width", templine.getWidth()+"");
				xmlSer.attribute(null, "maxY", templine.getMaxY()+"");
				xmlSer.attribute(null, "minY", templine.getMinY()+"");
				xmlSer.attribute(null, "startlineindex_start", templine.getStartLineIndex().getStart()+"");
				xmlSer.attribute(null, "startlineindex_top", templine.getStartLineIndex().getTopIndex()+"");
				xmlSer.attribute(null, "endlineindex_start", templine.getEndLineIndex().getStart()+"");
				xmlSer.attribute(null, "endlineindex_top", templine.getEndLineIndex().getTopIndex()+"");
				xmlSer.attribute(null, "linepaintoffset", templine.getLinePaintOffset()+"");
				xmlSer.attribute(null, "fontsize", templine.getFontSize()+"");
				xmlSer.attribute(null, "file", templine.getFile()+"");
				for(int i = 0;i < templine.getCoordinateList().size();i++){
					xmlSer.text("\n\t\t");
					xmlSer.startTag(null, "coordinate");
					xmlSer.attribute(null, "x", templine.getCoordinateList().get(i).x+"");
					xmlSer.attribute(null, "y", templine.getCoordinateList().get(i).y+"");
					xmlSer.endTag(null, "coordinate");
				}
				xmlSer.endTag(null, "line");
				
			}
			xmlSer.endTag(null, "comment");
			xmlSer.endDocument();
			writer.flush();
			writer.close();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 用pull读xml
	 * @param inStream 文件的InputStream
	 * @return List<LIne>
	 * Created on 2011-4-2  上午10:36:41
	 */
	public List<Line> readXml(InputStream inStream) throws NumberFormatException{
		List<Line> resultLineList = null;
		Line currentLine = null;
//		float[] xy = null;
		PointF readPoint = new PointF();
		
		XmlPullParser xmlPull = Xml.newPullParser();
		
		try {
			xmlPull.setInput(inStream, "UTF-8");
			
			int eventCode = xmlPull.getEventType();
			
			while (eventCode != XmlPullParser.END_DOCUMENT){
				switch (eventCode){
				case XmlPullParser.START_DOCUMENT:
					resultLineList = new ArrayList<Line>();
				break;
				
				case XmlPullParser.START_TAG:
					String name = xmlPull.getName();
					if(name.equalsIgnoreCase("line")){
						currentLine = new Line();
						currentLine.setColor(new Integer(xmlPull.getAttributeValue(null, "color")));
						currentLine.setWidth(new Integer(xmlPull.getAttributeValue(null, "width")));
						currentLine.setMaxY(new Float(xmlPull.getAttributeValue(null, "maxY")));
						currentLine.setMinY(new Float(xmlPull.getAttributeValue(null, "minY")));
						currentLine.setStartLineIndex_start(new Long(xmlPull.getAttributeValue(null, "startlineindex_start")));
						currentLine.setStartLineIndex_top(new Integer(xmlPull.getAttributeValue(null, "startlineindex_top")));
						currentLine.setEndLineIndex_start(new Long(xmlPull.getAttributeValue(null, "endlineindex_start")));
						currentLine.setEndLineIndex_top(new Integer(xmlPull.getAttributeValue(null, "endlineindex_top")));
						currentLine.setLinePaintOffset(new Float(xmlPull.getAttributeValue(null, "linepaintoffset")));
						currentLine.setFontSize(new Integer(xmlPull.getAttributeValue(null, "fontsize")));
						currentLine.setFile(new Integer(xmlPull.getAttributeValue(null, "file")));
					}else if(name.equalsIgnoreCase("coordinate")){
						//PointF tempPoint = new PointF();
						if(readPoint == null){
							readPoint = new PointF();
						}
						readPoint.x=new Float(xmlPull.getAttributeValue(null, "x"));
						readPoint.y=new Float(xmlPull.getAttributeValue(null, "y"));
						//currentLine.getCoordinateList().add(tempPoint);
					}
				break;
				
				case XmlPullParser.END_TAG:
					if(currentLine !=null && xmlPull.getName().equalsIgnoreCase("coordinate")){
						PointF tempPoint = readPoint;
						currentLine.getCoordinateList().add(tempPoint);
						readPoint = null;
					}else if(resultLineList !=null && xmlPull.getName().equalsIgnoreCase("line")){
						Line tempLine = currentLine;
						resultLineList.add(tempLine);
						currentLine = null;
					}
				break;
				}
				eventCode = xmlPull.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}catch (NullPointerException e){
			e.printStackTrace();
		}
		
		return resultLineList;
	}


	public Bitmap getmBitmap() {
		return mBitmap;
	}

//	@Override
//	public int compare(Object object1, Object object2) {
//		// TODO Auto-generated method stub
//		Line index1 = (Line)object1;
//		Line index2 = (Line)object2;
//		
//		
//		int flag; 
//		if(index1.getStartLineIndex().getStart() > index2.getStartLineIndex().getStart()){
//			flag = 1;
//		}else if(index1.getStartLineIndex().getStart() < index2.getStartLineIndex().getStart()){
//			flag = -1;
//		}else{
//			if(index1.getStartLineIndex().getTopIndex() > index2.getStartLineIndex().getTopIndex()){
//				flag = 1;
//			}else if(index1.getStartLineIndex().getTopIndex() < index2.getStartLineIndex().getTopIndex()){
//				flag = -1;
//			}else{
//				flag = 0;
//			}
//		}
//		return flag;
//	}

	public HashMap<String, List<Line>> getLineListFont() {
		return lineListFont;
	}


	
	
}
