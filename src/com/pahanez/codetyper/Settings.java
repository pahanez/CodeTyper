package com.pahanez.codetyper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

class Settings {
	private SharedPreferences sp;
	private static Settings sInstance;
	
	private Settings(){
		Log.e("p37td8" , " " + CodeTyperApplication.getAppContext());
		sp = CodeTyperApplication.getAppContext().getSharedPreferences("codetyper", Context.MODE_PRIVATE);
	}
	
	public static Settings getInstance(){
		return sInstance == null ? sInstance = new Settings() : sInstance;
	}
	
	public int getSourceId(){
		return sp.getInt(Constants.SOURCE_ID, 0);
	}
	
	public void setSourceId(int id){
		sp.edit().putInt(Constants.SOURCE_ID, id).commit();
	}
	
	public String getSourceName(int id){
		switch (id) {
		case 0:
			return "kexec.c";
		case 1:
			return "kexec.c";
		case 2:
			return "Activity.java";

		default:
			throw new IllegalStateException("Only 3 items accepted");
		}
	}
	
}
