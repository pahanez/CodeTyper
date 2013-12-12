package com.pahanez.codetyper;

import static com.pahanez.codetyper.Constants.SAVE_OUR_DATA;
import static com.pahanez.codetyper.Constants.SKIP_DATA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pahanez.codertyper.R;

public class TyperFragment extends Fragment implements OnSourceChanged {
	private EditText mHackerViewHidden;
	private TextView mHackerView;
	private BufferedReader mReader;
	private int mSkip = 0;
	private char[] chars = new char[10];

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.typer_layout, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mHackerView = (TextView) view.findViewById(R.id.hacker_typer_tv);
		mHackerViewHidden = (EditText) view.findViewById(R.id.hacker_et);

		try {
			mReader = new BufferedReader(new InputStreamReader(getActivity()
					.getAssets().open(Settings.getInstance().getSourceName(Settings.getInstance().getSourceId()))));
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

		mHackerViewHidden.requestFocus();
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mHackerViewHidden, InputMethodManager.SHOW_IMPLICIT);

		mHackerViewHidden.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mHackerView.setText(mHackerView.getText() + getNextData());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// EMPTY
			}

			@Override
			public void afterTextChanged(Editable s) {
				// EMPTY
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

	private String getNextData() {
		try {
			mSkip += mReader.read(chars);
			return new String(chars, 0, chars.length);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
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
			android.util.Log.e("p37td8", " 	" + id);
			mReader = new BufferedReader(new InputStreamReader(
					getActivity().getAssets().open(id)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Something went wrong.");
		}
		
		mHackerView.setText("");
		mSkip = 0;
	}

}
