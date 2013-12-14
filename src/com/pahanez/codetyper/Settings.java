package com.pahanez.codetyper;

import com.pahanez.codertyper.R;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

class Settings {
	private SharedPreferences sp;
	private static Settings sInstance;
	private final String [] mData;
	
	private Settings(){
		
		sp = CodeTyperApplication.getAppContext().getSharedPreferences("codetyper", Context.MODE_PRIVATE);
		mData = CodeTyperApplication.getAppContext().getResources().getStringArray(R.array.source_names);
	}
	
	public static Settings getInstance(){
		return sInstance == null ? sInstance = new Settings() : sInstance;
	}
	
	public String getSourceId(){
		return sp.getString(Constants.SOURCE_ID, mData[0]);
	}
	
	public void setSourceId(String id){
		sp.edit().putString(Constants.SOURCE_ID, id).commit();
	}
	
}
