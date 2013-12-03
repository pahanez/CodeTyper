package com.pahanez.codetyper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.pahanez.codertyper.R;

public class MainActivity extends FragmentActivity {
	
	private SlidingMenu mMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initMenuFrament();
		initSlidingMenu();
	}
	
	private void initSlidingMenu() {
        mMenu = new SlidingMenu(this);
        mMenu.setMode(SlidingMenu.LEFT);
        mMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mMenu.setFadeDegree(0.35f);
        mMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mMenu.setMenu(R.layout.menu);
        
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.menu_frame, new SlidingMenuFragment())
        .commit();
	}

	private void initMenuFrament() {
		if(getSupportFragmentManager().findFragmentById(R.id.inner_fragment) == null)
			getSupportFragmentManager().beginTransaction().add(R.id.inner_fragment, new MenuFragment()).commit();
	}
	
	public SlidingMenu getMenu(){
		return mMenu;
	}

	
}
