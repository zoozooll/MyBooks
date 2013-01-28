/**
 * 
 */
package com.aaron.reader.act;

import com.aaron.reader.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author Aaron Lee
 *  writed at 上午11:48:12 2013-1-20
 */
public class WelcomeActivity extends Activity {

	/** (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * writed by Aaron Lee
	 * writed at 下午5:43:32 2013-1-20
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
	}

}
