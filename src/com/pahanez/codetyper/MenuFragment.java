package com.pahanez.codetyper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pahanez.codertyper.R;

public class MenuFragment extends Fragment{
	
	private Button mStartButton;
	private EditText mTitle;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_menu, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mStartButton = (Button) view.findViewById(R.id.menu_start_button);
		mTitle = (EditText) view.findViewById(R.id.main_menu_title);
		mTitle.requestFocus();
		Log.e("p37td8" , "requestFocus");
		mStartButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction().replace(R.id.inner_fragment, new TyperFragment()).commit();
			}
		});
	}
	
	
	
}
