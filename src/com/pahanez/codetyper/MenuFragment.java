package com.pahanez.codetyper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.pahanez.codertyper.R;

public class MenuFragment extends Fragment{
	
	private Button mStartButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_menu, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mStartButton = (Button) view.findViewById(R.id.menu_start_button);
		mStartButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction().replace(R.id.inner_fragment, new TyperFragment()).commit();
			}
		});
	}
	
	
}
