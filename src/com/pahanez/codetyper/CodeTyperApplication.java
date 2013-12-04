package com.pahanez.codetyper;

import android.app.Application;
import android.content.Context;

public class CodeTyperApplication extends Application{

	private static Application sInstance;
	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}
	
	public static Context getAppContext(){
		return sInstance;
	}

}
