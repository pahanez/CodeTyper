package com.pahanez.codetyper;

import android.content.Context;
import android.content.SharedPreferences;

import com.pahanez.codertyper.R;

class Settings {
	private SharedPreferences sp;
	private static Settings sInstance;
	private final String [] mData;
	
	private Settings(){
		
		sp = CodeTyperApplication.getAppContext().getSharedPreferences("codetyper", Context.MODE_PRIVATE);
		mData = CodeTyperApplication.getAppContext().getResources().getStringArray(R.array.extra_source_names);
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
	
	public int getSpeed(){
		return sp.getInt(Constants.SPEED, 4);
	}
	
	public void setSpeed(int speed){
		sp.edit().putInt(Constants.SPEED, speed).commit();
	}
	
	public int getColor(){
		return sp.getInt(Constants.COLOR, CodeTyperApplication.getAppContext().getResources().getColor(R.color.typer_color_green));
	}
	
	public void setColor(int color){
		sp.edit().putInt(Constants.COLOR, color).commit();
	}
	
}
