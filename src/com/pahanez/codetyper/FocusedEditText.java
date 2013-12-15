package com.pahanez.codetyper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.EditText;

public class FocusedEditText extends EditText{

	public FocusedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}

}
