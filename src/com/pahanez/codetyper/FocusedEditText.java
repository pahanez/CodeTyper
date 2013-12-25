package com.pahanez.codetyper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
