package com.pahanez.codetyper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.pahanez.codertyper.R;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initMenuFrament();
	}

	private void initMenuFrament() {
		if(getSupportFragmentManager().findFragmentById(R.id.inner_fragment) == null)
			getSupportFragmentManager().beginTransaction().add(R.id.inner_fragment, new MenuFragment()).commit();
	}

	
}
