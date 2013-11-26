package com.pahanez.codetyper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.pahanez.codertyper.R;

public class MenuFragment extends Fragment{
	
	private Button mStartButton;
	private EditText mTitle;
	
	private Animation mButtonAnimation, mTitleAnimation;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adjustAnimations();
	}
	
	private void adjustAnimations() {
		mButtonAnimation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
		
		mButtonAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				mStartButton.setEnabled(false);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mStartButton.setVisibility(View.GONE);
//				mTitle.startAnimation(mTitleAnimation);
				getFragmentManager().beginTransaction().replace(R.id.inner_fragment, new TyperFragment()).commit();
			}
		});
		
		mTitleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.title_animation);
		mTitleAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				getFragmentManager().beginTransaction().replace(R.id.inner_fragment, new TyperFragment()).commit();
			}
		});
	}

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
		mStartButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mStartButton.startAnimation(mButtonAnimation);
			}
		});
	}
	
	
	
}
