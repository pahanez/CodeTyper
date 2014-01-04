package com.pahanez.codetyper;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pahanez.codertyper.R;



public class EndDialog extends Dialog{
	
	private static final int UPDATE_TITLE = 100;
	private static final int UPDATE_PROGRESS = 101;
	
	private final TextView mTextView;
	private final ProgressBar mPB;
	private int mProgress;
	private boolean mHacked;
	
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
						if(mHacked)
							mTextView.setText("Congratulations!\nYou hacked NASA.");
						else
							mTextView.setText("Bad News!\nYou was catched.");
					}
				break;

			default:
				break;
			}
		}
	};
	
	public EndDialog(Context context, int theme , boolean hacked) {
		super(context, theme);
		setContentView(R.layout.end_dialog_layot);
		mTextView = (TextView) findViewById(R.id.end_text);
		mPB = (ProgressBar) findViewById(R.id.end_progress);
		mHandler.sendEmptyMessageDelayed(UPDATE_TITLE, 500);
		mHacked = hacked;
		
		
		
	}
	

}
