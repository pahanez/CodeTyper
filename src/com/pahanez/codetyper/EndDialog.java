package com.pahanez.codetyper;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pahanez.codertyper.R;



public class EndDialog extends Dialog implements android.view.View.OnClickListener{
	
	private static final int UPDATE_TITLE = 100;
	private static final int UPDATE_PROGRESS = 101;
	
	private final TextView mTextView;
	private ViewGroup mHackedYButtons;
	private ViewGroup mHackedNButtons;
	private Button mAgainY,mAgainN,mNext;
	final private Animation fadein ;
	private final ProgressBar mPB;
	private int mProgress;
	private boolean mHacked;
	private DialogHelper mDialogHelper;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_TITLE:
					String title = mTextView.getText().toString();
					if(title.contains("....")){
						mTextView.setText("Uploading");
						mPB.setIndeterminate(false);
						sendEmptyMessage(UPDATE_PROGRESS);
					}
					else{
						mTextView.setText(title+".");
						sendEmptyMessageDelayed(UPDATE_TITLE, 1000);
					}
				break;
			case UPDATE_PROGRESS:
					mPB.setProgress(mProgress++);
					if(mProgress!=101)
						sendEmptyMessageDelayed(UPDATE_PROGRESS, 100);
					else{
						
						setCancelable(true);
						if(mHacked){
							mTextView.startAnimation(fadein);
							mTextView.setText("Congratulations!\nYou hacked NASA.");
							fadein.setAnimationListener(new CustomAnimationListener(mHackedYButtons));
							mHackedYButtons.startAnimation(fadein);
						}
						else{
							mTextView.setText("Bad News!\nYou was catched.");
							fadein.setAnimationListener(new CustomAnimationListener(mHackedNButtons));
							mHackedNButtons.startAnimation(fadein);							
						}
					}
				break;

			default:
				break;
			}
		}
	};
	
	public EndDialog(Context context, int theme , boolean hacked, DialogHelper dialogHelper) {
		super(context, theme);
		setContentView(R.layout.end_dialog_layot);
		mTextView = (TextView) findViewById(R.id.end_text);
		mPB = (ProgressBar) findViewById(R.id.end_progress);
		mHackedYButtons = (LinearLayout) findViewById(R.id.end_hacked_y);
		mHackedNButtons = (ViewGroup) findViewById(R.id.end_hacked_n);
		mHandler.sendEmptyMessageDelayed(UPDATE_TITLE, 500);
		mAgainN = (Button) findViewById(R.id.try_again_n);
		mAgainY = (Button) findViewById(R.id.try_again_y);
		mNext = (Button) findViewById(R.id.next_y);
		mAgainN.setOnClickListener(this);
		mAgainY.setOnClickListener(this);
		mNext.setOnClickListener(this);
		mHacked = hacked;
		fadein	= AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
		mDialogHelper = dialogHelper;
		
		
		
	}
	
	
	private class CustomAnimationListener implements AnimationListener{
		
		private ViewGroup mGroup;
		
		public CustomAnimationListener(ViewGroup group) {
			mGroup = group;
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mGroup.setVisibility(View.VISIBLE);
		
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.try_again_n:
		case R.id.try_again_y:
			if(mDialogHelper != null)
				dismiss();
				mDialogHelper.restart();
			break;

		default:
			break;
		}
	}

}
