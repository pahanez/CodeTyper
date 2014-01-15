package com.pahanez.codetyper;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pahanez.codertyper.R;

public class TimerManager {
	private TimerManager(){}
	private static TimerManager sInstance;
	private static int pr = 0;
	public static Executor sSerialExecutor = AsyncTask.SERIAL_EXECUTOR;
	
	
	public static synchronized TimerManager getInstance(){
		return sInstance == null? sInstance = new TimerManager(): sInstance;
	}
	
	public static class Updater implements Runnable {
		private ProgressBar pb;
		private boolean mIsCancelled = false;
		private int mDelay;
		private int mFileSize;
		private String mFileName;
		private boolean mCatched = false;
		private FragmentActivity mActivity;
		private int mId;
		public Updater(FragmentActivity activity,int fileSize , String fileName){
			this.setProgressBar(pb);
			mDelay = fileSize / 10;
			mFileName = fileName;
			mFileSize = fileSize;
			mActivity = activity;
			mId = Utils.getId(fileName);
		}

		@Override
		public void run() {
			
			for(int i = 100; i >= 00; i--){
				if(!isCancelled() && Utils.shouldStart(mFileName)){
				pr = i;
				final int k = i;
				if(pb == null){
					SlidingMenuFragment menu = (SlidingMenuFragment)mActivity.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
					if(menu.getList() != null)
						pb = ((SlidingMenuFragment.TyperMenuAdaper)menu.getList().getAdapter()).getmProgressBar();
				}
				if(pb != null)
				getProgressBar().post(new Runnable() {
					@Override
					public void run() {
						getProgressBar().setProgress(k);
					}
				});
				
				
				
				try {
					TimeUnit.MILLISECONDS.sleep(mDelay*7/mId);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				}else{
					if(pb!=null)
					getProgressBar().setProgress(100);
				}
			}
			if(!isCancelled() && Utils.shouldStart(mFileName)){
				mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast toast = new Toast(mActivity.getApplicationContext());
						toast.setDuration(Toast.LENGTH_LONG); 
						toast.setView(mActivity.getLayoutInflater().inflate(
								R.layout.catched_toast, null));
						toast.setGravity(Gravity.TOP, 0, 100);
						toast.show();
					}
				});
				mCatched = true;
			}
		}

		public ProgressBar getProgressBar() {
			return pb;
		}

		public void setProgressBar(ProgressBar pb) {
			this.pb = pb;
			if (pb!=null && Utils.shouldStart(mFileName)) {
				android.util.Log.e("p37td8", "setProgress");
				pb.setProgress(pr);
			}
		}

		public boolean isCancelled() {
			return mIsCancelled;
		}

		public void setCancelled(boolean isCancelled) {
			this.mIsCancelled = isCancelled;
		}
		
		public void setExtraProgress(int skip){ 
			if(pb != null)
				pb.setProgress((skip * 100) / mFileSize );
		}
		
		public boolean isCatched(){
			return mCatched;
		}
		
	}
	
}
