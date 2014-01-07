package com.pahanez.codetyper;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

import com.pahanez.codertyper.R;

class Settings {
	private SharedPreferences sp;
	private static Settings sInstance;
	private final String [] mData;
	private static final Set<String> DEFAULT_COMPLETE = new HashSet<String>(){{
		add("0");
	}};
	
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
	
	public void addLevelComplete(int pos){
		String position = String.valueOf(pos);
		Set<String> levels_complete = sp.getStringSet(Constants.COMPLETE_LEVELS, DEFAULT_COMPLETE);
		levels_complete.add(position);
		android.util.Log.e("p37td8", "+++ " +position);
		sp.edit().putStringSet(Constants.COMPLETE_LEVELS, levels_complete).commit();
	}
	
	public boolean isAvailable(int pos){
		String position = String.valueOf(pos);
		Set<String> levels_complete = sp.getStringSet(Constants.COMPLETE_LEVELS, DEFAULT_COMPLETE);
		android.util.Log.e("p37td8", " ++ " + levels_complete.contains(position));
		return levels_complete.contains(position);
	}
	
	
	
}
