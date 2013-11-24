package com.pahanez.codetyper;

import com.pahanez.codertyper.R;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initMenuFrament();
	}

	private void initMenuFrament() {
		getSupportFragmentManager().beginTransaction().add(R.id.inner_fragment, new MenuFragment()).commit();
	}

	
}
