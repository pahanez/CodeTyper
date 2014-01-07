package com.pahanez.codetyper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.pahanez.codertyper.R;

public class CustomView extends View{ // DEL TODO
	
	private Handler mHandler = new Handler();
	
	private LinearGradient mLinearGradient;
	
	private Runnable mUpdater = new Runnable() {
		
		@Override
		public void run() {
			CustomView.this.invalidate();
		}
	};
	
	private final int mDisplayWidth;
	private final int mDisplayHeight;
	private int mYPosition;
	private Paint mPaint = new Paint();

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDisplayHeight = context.getResources().getDisplayMetrics().heightPixels;
		mDisplayWidth = context.getResources().getDisplayMetrics().widthPixels;
		context.getResources().getColor(R.color.typer_color_green);
		
		mLinearGradient = new LinearGradient(
			    0, 0, 0, 100, 
			    Color.BLACK, Color.GREEN, Shader.TileMode.CLAMP);
		mPaint.setShader(mLinearGradient);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		mHandler.postDelayed(mUpdater, 1000/25);
		super.onDraw(canvas);
		canvas.drawRect(0, (++mYPosition - 100), mDisplayWidth, mYPosition, mPaint);
		if(mYPosition > (mDisplayHeight + 200)) mYPosition = -30;
		
	}

}
