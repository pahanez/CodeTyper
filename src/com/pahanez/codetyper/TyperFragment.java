package com.pahanez.codetyper;

import static com.pahanez.codetyper.Constants.SAVE_OUR_DATA;
import static com.pahanez.codetyper.Constants.SKIP_DATA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
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
import com.pahanez.codetyper.TimerManager.Updater;

public class TyperFragment extends Fragment implements ContentTyper,OnProgressBarChangedListener,DialogHelper {
	private EditText mHackerViewHidden;
	private FocusedEditText mHackerView;
	private BufferedReader mReader; 
	private int mSkip = 0;
	private char[] chars;
	private Updater mUpdater;
	private String mSourceId;
	private int mFileSize;
	private Dialog mEndDialog;
	private boolean mEOF = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((MainActivity)getActivity()).getMenu().setSlidingEnabled(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.typer_layout, null);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mUpdater !=null) mUpdater.setCancelled(true);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		chars = new char[Settings.getInstance().getSpeed()+1];
		mHackerView = (FocusedEditText) view.findViewById(R.id.hacker_typer_tv);
		mHackerViewHidden = (EditText) view.findViewById(R.id.hacker_et);
		setRetainInstance(true);

		try {
			((SlidingMenuFragment.TyperMenuAdaper)((SlidingMenuFragment)getFragmentManager().findFragmentById(R.id.menu_frame)).getList().getAdapter()).setProgressBarChangedListener(this);
			
			mSourceId = Settings.getInstance().getSourceId();
			InputStream stream = 
					getActivity().getAssets().open(mSourceId);
			mReader = new BufferedReader(new InputStreamReader(stream));
			mReader.mark(1);

			mFileSize = stream.available();
			if(mUpdater != null)mUpdater.setCancelled(true);
			mUpdater = new Updater(getActivity() , mFileSize , Settings.getInstance().getSourceId());
			TimerManager.sSerialExecutor.execute(mUpdater);
			
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
				if(!mEOF)
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
				if(mUpdater !=null && !Utils.shouldStart(mSourceId))
					mUpdater.setExtraProgress(mSkip);
				mHackerView.setSelection(mHackerView.getText().length());
				if(mSkip + chars.length >= mFileSize && Utils.shouldStart(mSourceId) && !mEOF ){ 
					mUpdater.setCancelled(true);
					mEndDialog = new EndDialog(getActivity() , R.style.Theme_CustomDialog , !mUpdater.isCatched() , TyperFragment.this , mSourceId);
					mEndDialog.setCancelable(false);
					mEndDialog.show();
					mEOF = true;
				}else if(mSkip >= mFileSize && !mEOF){
					mEOF = true;
				}
			}
		});

	}
	
	@Override
	public void onStop() { 
		super.onStop(); 
		if(mEndDialog != null) mEndDialog.dismiss();
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mHackerViewHidden,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		ProgressBar pb = null;
		SlidingMenuFragment menu = (SlidingMenuFragment)getFragmentManager().findFragmentById(R.id.menu_frame);
		if(menu.getList() != null)
			pb = ((SlidingMenuFragment.TyperMenuAdaper)menu.getList().getAdapter()).getmProgressBar();
		mUpdater.setProgressBar(pb);
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
		Settings.getInstance().setSourceId(id);
		mEOF = false;
		try {
			mSourceId = id;
			InputStream stream = 
					getActivity().getAssets().open(id);
			mReader = new BufferedReader(new InputStreamReader(stream));
			mReader.mark(1);

			if(mUpdater != null)mUpdater.setCancelled(true);

			mFileSize = stream.available();
			mUpdater = new Updater(getActivity() , mFileSize , id);
			TimerManager.sSerialExecutor.execute(mUpdater);
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
		if(mUpdater != null )
			mUpdater.setProgressBar(pb);
	}

	@Override
	public void restart() { 
		sourceChanged(mSourceId);
	}

	@Override
	public void next() {
		String [] arrays = getResources().getStringArray(R.array.source_names);
		for(int i = 0; i < arrays.length; i++){
			
			if(arrays[i].equals(mSourceId) && i != (arrays.length - 1)){
				sourceChanged(arrays[i+1]);
				break;
			}
		}
	}
	@Override
	public void setTextSize(int size) {
		if(mHackerView != null)
			mHackerView.setTextSize(size);
		
	}

}
