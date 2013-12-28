package com.pahanez.codetyper;

import static com.pahanez.codetyper.Constants.SAVE_OUR_DATA;
import static com.pahanez.codetyper.Constants.SKIP_DATA;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pahanez.codertyper.R;

public class TyperFragment extends Fragment implements ContentTyper,OnProgressBarChangedListener {
	private EditText mHackerViewHidden;
	private FocusedEditText mHackerView;
	private BufferedReader mReader; 
	private int mSkip = 0;
	private char[] chars;
	private Updater mUpdater;
	
	private class Updater implements Runnable {
		private ProgressBar pb;
		private boolean isCancelled = false;
		public Updater(ProgressBar pb){
			this.setProgressBar(pb);
		}

		@Override
		public void run() {
			for(int i = 0; i <= 100; i++){
				if(!isCancelled()){
					
				final int k = i;
				getProgressBar().post(new Runnable() {
					@Override
					public void run() {
						getProgressBar().setProgress(k);
					}
				});
				
				
				
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				}
			}
		}

		public ProgressBar getProgressBar() {
			return pb;
		}

		public void setProgressBar(ProgressBar pb) {
			this.pb = pb;
		}

		public boolean isCancelled() {
			return isCancelled;
		}

		public void setCancelled(boolean isCancelled) {
			this.isCancelled = isCancelled;
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.typer_layout, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		chars = new char[Settings.getInstance().getSpeed()+1];
		mHackerView = (FocusedEditText) view.findViewById(R.id.hacker_typer_tv);
		mHackerViewHidden = (EditText) view.findViewById(R.id.hacker_et);
		((SlidingMenuFragment.TyperMenuAdaper)((SlidingMenuFragment)getFragmentManager().findFragmentById(R.id.menu_frame)).getList().getAdapter()).setProgressBarChangedListener(this);
		try {
			mReader = new BufferedReader(new InputStreamReader(getActivity()
					.getAssets().open(Settings.getInstance().getSourceId())));
			mReader.mark(1);
			mUpdater = new Updater(((SlidingMenuFragment.TyperMenuAdaper)((SlidingMenuFragment)getFragmentManager().findFragmentById(R.id.menu_frame)).getList().getAdapter()).getmProgressBar());
			AsyncTask.SERIAL_EXECUTOR.execute(mUpdater);
		} catch (IOException e) {
			e.printStackTrace();
		} 

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(SAVE_OUR_DATA))
				mHackerView
						.setText(savedInstanceState.getString(SAVE_OUR_DATA));
			if (savedInstanceState.containsKey(SKIP_DATA))
				try {
					mSkip = savedInstanceState.getInt(SKIP_DATA);
					mReader.skip(mSkip);
				} catch (IOException e) {
					e.printStackTrace();
				}
		} else {
			Toast toast = new Toast(getActivity().getApplicationContext());
			toast.setDuration(Toast.LENGTH_LONG); 
			toast.setView(getLayoutInflater(savedInstanceState).inflate(
					R.layout.toast_text, null));
			toast.setGravity(Gravity.TOP, 0, 100);
			toast.show();

		}
		mHackerViewHidden.setText(R.string.initial_text);
		mHackerView.setTextColor(Settings.getInstance().getColor());
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mHackerViewHidden, InputMethodManager.SHOW_IMPLICIT);
		
		mHackerViewHidden.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				return false;
			}
		});
		mHackerViewHidden.addTextChangedListener(new TextWatcher() {
			private int mCount;
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					mSkip += mReader.read(chars);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mHackerView.append(new String(chars));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				mCount = mHackerViewHidden.getText().length();
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if((mCount - mHackerViewHidden.getText().length()) == 1){
					android.util.Log.e("p37td8", "del happened" + mSkip);
					if(mHackerView.getText().length() >= chars.length * 2){
						mHackerView.setText(mHackerView.getText().subSequence(0, mHackerView.getText().length() - chars.length * 2));
						mSkip -= chars.length * 2;
					}
					else{
						mHackerView.setText(mHackerView.getText().subSequence(0, mHackerView.getText().length() - chars.length));
						mSkip -= chars.length;
					}
					try {
						mReader.reset();
						mReader.skip(mSkip);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				mHackerView.setSelection(mHackerView.getText().length());
			}
		});

	}
	
	@Override
	public void onStop() { 
		super.onStop();
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mHackerViewHidden,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mHackerView != null) 
			outState.putString(SAVE_OUR_DATA, mHackerView.getText().toString());
		if (mSkip != 0)
			outState.putInt(SKIP_DATA, mSkip);

	}

	@Override
	public void sourceChanged(String id) {
		try {
			InputStream stream = 
					getActivity().getAssets().open(id);
			mReader = new BufferedReader(new InputStreamReader(stream));
			mReader.mark(1);

			if(mUpdater != null)mUpdater.setCancelled(true);

			android.util.Log.e("p37td8", "if :	 " + stream.available());
			mUpdater = new Updater(((SlidingMenuFragment.TyperMenuAdaper)((SlidingMenuFragment)getFragmentManager().findFragmentById(R.id.menu_frame)).getList().getAdapter()).getmProgressBar());
			AsyncTask.SERIAL_EXECUTOR.execute(mUpdater);
		} catch (IOException e) {
			e.printStackTrace(); 
			throw new RuntimeException("Something went wrong.");
		}
		
		
		mHackerView.setText("");
		mSkip = 0;
	}

	@Override
	public void speedChanged(int speed) {
		chars = new char[Settings.getInstance().getSpeed()+1];
	}

	@Override
	public void setColor(int color) {
		mHackerView.setTextColor(color);
	}

	@Override
	public void onProgressBarChanged(ProgressBar pb) {
		if(mUpdater != null)
			mUpdater.setProgressBar(pb);
	}

}
