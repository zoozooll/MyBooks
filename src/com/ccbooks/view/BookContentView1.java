package com.ccbooks.view;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.ccbooks.bo.BooksBo;
import com.ccbooks.bo.ContentBo;
import com.ccbooks.dialog.CharsetDialog;
import com.ccbooks.dialog.FontDialog;
import com.ccbooks.dialog.LightBarDialog;
import com.ccbooks.listener.contentView1.BtnBackColorListener;
import com.ccbooks.util.StringConfig;
import com.ccbooks.vo.Book;

import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BookContentView1 extends Activity {


	// TranslateAnimation showAction, hideAction;
	Animation showAction1, hideAction1, showAction2, hideAction2;
	FrameLayout fltBookContent1;
	LinearLayout menuTop, menuButtom;
	public TextView tvMainContent, tvBookNameContent, tvBookPercent;
	ScrollView svView;
	Button btnBookStore;
	ImageButton btnBackColor, btnFontSize, btnBackLight, btnBookCatalog,
			btnMore;
	SeekBar sbrMainContent, btnLightBar;

	/** 是否显示菜单 */
	public boolean menuShowed = true;
	/** 内容页最大高度 */
	public int contentMeasuredHeight;
	/** 背景颜色 1 白天 0 黑夜 */
	public short dayNight = 1;
	/** 提供输出内容 */
	String str;
	/** 提供书对象 */
	public Book book;

	public ContentBo cbo;
	
	String str1;
	public int  count ;
	/**编码格式数组索引*/
	public int charsetsIndex;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_content_1);

		menuTop = (LinearLayout) findViewById(R.id.menuTop);
		menuButtom = (LinearLayout) findViewById(R.id.menuButtom);
		tvMainContent = (TextView) findViewById(R.id.tvMainContent);
		tvBookNameContent = (TextView) findViewById(R.id.tvBookNameContent);
		svView = (ScrollView) findViewById(R.id.svView);
		btnBookStore = (Button) findViewById(R.id.btnBookStore);
		sbrMainContent = (SeekBar) findViewById(R.id.sbrMainContent);
		tvBookPercent = (TextView) findViewById(R.id.tvBookPercent);
		btnBackColor = (ImageButton) findViewById(R.id.btnBackColor);
		btnFontSize = (ImageButton) findViewById(R.id.btnFontSize);
		btnBackLight = (ImageButton) findViewById(R.id.btnBackLight);
		btnBookCatalog = (ImageButton) findViewById(R.id.btnBookCatalog);
		fltBookContent1 = (FrameLayout) findViewById(R.id.fltBookContent1);
		btnMore = (ImageButton) findViewById(R.id.btnMore);

		Bundle bundle = this.getIntent().getExtras();
		int id = bundle.getInt("bookId");
		BooksBo bo = new BooksBo(this, false);
		book = bo.getOne(id);
		tvBookNameContent.setText(book.bookname);
		charsetsIndex = 4;
		tvMainContent.setText( getStringFromFile(StringConfig.CHARSETS[charsetsIndex]));
		ContentBo.flag= true;
		// tvMainContent.setText(getStringFromFile("GBK"));
		// getStringFromFile("GBK");

		// 这里是TranslateAnimation动画
		showAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		// 这里是ScaleAnimation动画
		// showAction = new ScaleAnimation(
		// 1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.0f);
		showAction1.setDuration(500);
		// 这里是TranslateAnimation动画
		hideAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
		// 这里是ScaleAnimation动画
		// hideAction = new ScaleAnimation(
		// 1.0f, 1.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.0f);
		hideAction1.setDuration(500);

		// 这里是TranslateAnimation动画
		showAction2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				2.0f, Animation.RELATIVE_TO_SELF, 1.0f);
		// 这里是ScaleAnimation动画
		// showAction = new ScaleAnimation(
		// 1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.0f);
		showAction2.setDuration(500);
		// 这里是TranslateAnimation动画
		hideAction2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 2.0f);
		// 这里是ScaleAnimation动画
		// hideAction = new ScaleAnimation(
		// 1.0f, 1.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		// Animation.RELATIVE_TO_SELF, 0.0f);
		hideAction2.setDuration(500);
		
		
		tvMainContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (menuShowed) {
					menuShowed = false;
					menuTop.startAnimation(hideAction1);
					menuTop.setVisibility(View.GONE);

					menuButtom.startAnimation(hideAction2);
					menuButtom.setVisibility(View.GONE);

					/** 全屏设置，隐藏窗口所有装饰 */
					getWindow().addFlags(
							WindowManager.LayoutParams.FLAG_FULLSCREEN);

				} else {
					menuShowed = true;
					menuTop.startAnimation(showAction1);
					menuTop.setVisibility(View.VISIBLE);

					menuButtom.startAnimation(showAction2);
					menuButtom.setVisibility(View.VISIBLE);

					/** 全屏设置，隐藏窗口所有装饰 */

					getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
				}
				
				
			}

		});

		btnBookStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});

		sbrMainContent
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						seekBar.setMax(tvMainContent.getMeasuredHeight());
						svView.scrollTo(0, progress);
						// System.out.println(svView.getScrollY());
						try {
							tvBookPercent.setText(100 * progress
									/ tvMainContent.getMeasuredHeight() + "%");
						} catch (ArithmeticException e) {
							e.printStackTrace();
							tvBookPercent.setText("0%");
						}
						// System.out.println(100*svView.getScrollY()/tvMainContent.getMeasuredHeight());
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						cbo = new ContentBo(BookContentView1.this,book.bookpath,"GBK");
						new Thread(cbo).start();
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

				});

		svView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					sbrMainContent.setMax(tvMainContent.getMeasuredHeight());
					sbrMainContent.setProgress(svView.getScrollY());
					tvBookPercent.setText(100 * (svView.getScrollY())
							/ tvMainContent.getMeasuredHeight() + "%");
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					 System.out.println("===X==="+event.getX());
					 System.out.println("===Y==="+event.getY());
				}
				cbo = new ContentBo(BookContentView1.this,book.bookpath,"GBK");
				new Thread(cbo).start();
				return false;
			}

		});

		btnBackColor.setOnClickListener(new BtnBackColorListener(this,
				fltBookContent1, tvMainContent));

		btnFontSize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new FontDialog(BookContentView1.this, tvMainContent).show();
			}

		});

		btnBackLight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new LightBarDialog(BookContentView1.this).show();
			}

		});

		btnBookCatalog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new CharsetDialog(BookContentView1.this, tvMainContent,charsetsIndex).show();
			}
		});

		btnMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(BookContentView1.this).setTitle(
						"关于CCBOOKS").setMessage("本软件目前版本属于1.0版本")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.cancel();
									}
								}).show();
			}

		});
	}
	

	public String getStringFromFile(String filenameString, String code) {
		try {
			StringBuffer sBuffer = new StringBuffer();
			FileInputStream fInputStream = new FileInputStream(filenameString);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fInputStream, code);
			BufferedReader in = new BufferedReader(inputStreamReader);
			if (!new File(filenameString).exists()) {
				return null;
			}
			while (in.ready()) {
				sBuffer.append(in.readLine() + "\n");
			}
			in.close();
			return sBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getStringFromFile(String code) {
		try {

			StringBuffer sBuffer = new StringBuffer(1024);
			BufferedReader localBufferedReader;
			DataInputStream localDataInputStream;
			FileInputStream fInputStream = new FileInputStream(book.bookpath);
			localDataInputStream = new DataInputStream(
					(InputStream) fInputStream);
			InputStreamReader inputStreamReader = new InputStreamReader(
					localDataInputStream, code);
			localBufferedReader = new BufferedReader((Reader) inputStreamReader);
			BufferedReader in = new BufferedReader(inputStreamReader);
			int i = 8192;
			char[] arrayOfChar = new char[i];
			while (true) {
				int j = localBufferedReader.read(arrayOfChar);
				i = -1;
				int s = 0;

				count += j;

				if (j == i) {
					break;
				}

				// String str = String.valueOf(arrayOfChar, 0, j);
				sBuffer.append(arrayOfChar, 0, j);
				if (count <= 8192) {
					if (sBuffer.length() >= 1024) {
						str = new String(sBuffer);

						// this.handler.sendEmptyMessage(0);
						return str;
					}
				}

				if (count > 8192) {

					return null;

				}
			}
			return null;
		}catch (FileNotFoundException e){
			e.printStackTrace();
			str = "\n\n\n电子书文件错误";
			Toast.makeText(this, str, Toast.LENGTH_LONG).show();
			return str;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 获得数据 */
/*	public String getStringFromFile(String code) {
		try {
			StringBuffer sBuffer = new StringBuffer();
			FileInputStream fInputStream = new FileInputStream(book.bookpath);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fInputStream, code);
			BufferedReader in = new BufferedReader(inputStreamReader);
			if (!new File(book.bookpath).exists()) {
				return null;
			}
			while (in.ready()) {
				sBuffer.append(in.readLine() + "\n");
			}
			in.close();
			return sBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/

/*	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// tvMainContent.setText(null);
			System.out.println("handler");
			String str =cbo.content;
			if(cbo.content!=null){
				
				tvMainContent.append(cbo.content);
				System.out.println("append");
			}	
			super.handleMessage(msg);
		}

	};*/
}