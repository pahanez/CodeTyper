package com.pahanez.codetyper;

import com.pahanez.codertyper.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;

public class EndDialog extends Dialog{
	public EndDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.end_dialog_layot);
	}

	public EndDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public EndDialog(Context context) {
		super(context);
	}
	

}
