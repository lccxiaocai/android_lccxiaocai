package com.xiaocai_.romaunt_1;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	
	private static Context context;
	
	public void onCreate() {
		context = getApplicationContext();
	}
	
	/*
	 * SlideAdapter类中    mInflater = (LayoutInflater)MyApplication.getContext()
	 *.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 *不报错 但无法运行
	 */
	/*
	 * SlideAdapter类中    slideView = new SlideView(MyApplication.getContext());
	 *不报错 但无法运行
	 */
	public static Context getContext() {
		return context;
	}
}
