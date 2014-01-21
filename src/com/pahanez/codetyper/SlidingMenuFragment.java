package com.pahanez.codetyper;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialogFragment;
import yuku.ambilwarna.OnAmbilWarnaListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.pahanez.codertyper.R;

public class SlidingMenuFragment extends Fragment implements OnOpenedListener{

	private ListView mList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		return inflater.inflate(R.layout.menu_layout, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		
		mList = (ListView) view.findViewById(R.id.menu_list);
		getList().setAdapter(new TyperMenuAdaper(initMenuData() , getLayoutInflater(savedInstanceState)));
		
		getList().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		getList().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (getList().getAdapter().getItemViewType(position)) {
				case TyperMenuAdaper.SOURCE_ITEM:
					Fragment fragment = getFragmentManager().findFragmentById(R.id.inner_fragment);
					if (fragment instanceof ContentTyper)
						((ContentTyper)fragment).sourceChanged((String)view.getTag());
					break;
				case TyperMenuAdaper.SPEED_ITEM:
						final Dialog d = new Dialog(getActivity(), R.style.Theme_CustomDialog);
						d.setCanceledOnTouchOutside(true);
						d.setContentView(R.layout.speed_dialog_layout);
						
						
						
						final Button b = (Button) d.findViewById(R.id.speed_dialog_b);
						final SeekBar sb = (SeekBar) d.findViewById(R.id.speed_dialog_sb);
						sb.setProgress(Settings.getInstance().getSpeed());
						
						b.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								int progress = sb.getProgress();
								Settings.getInstance().setSpeed(sb.getProgress());
								Fragment fragment = getFragmentManager().findFragmentById(R.id.inner_fragment);
								if (fragment instanceof ContentTyper)
									((ContentTyper)fragment).speedChanged(progress + 1);
								d.dismiss();
							}
						});
						
						d.show();
					break;
				case TyperMenuAdaper.SIZE_ITEM:
					final Dialog sd = new Dialog(getActivity(), R.style.Theme_CustomDialog);
					sd.setCanceledOnTouchOutside(true);
					sd.setContentView(R.layout.speed_dialog_layout);
					
					
					
					final Button sizeButton = (Button) sd.findViewById(R.id.speed_dialog_b);
					final SeekBar ssb = (SeekBar) sd.findViewById(R.id.speed_dialog_sb);
					ssb.setMax(39);
					ssb.setProgress(Settings.getInstance().getTextSize());
					
					sizeButton.setOnClickListener(new OnClickListener() {
						
						@Override
						
						public void onClick(View v) {
							int progress = ssb.getProgress();
							Settings.getInstance().setTextSize(progress + 1);
							Fragment fragment = getFragmentManager().findFragmentById(R.id.inner_fragment);
							if (fragment instanceof ContentTyper)
								((ContentTyper)fragment).setTextSize(progress + 1);
							sd.dismiss();
						}
					});
					
					sd.show();
				break;
				case TyperMenuAdaper.COLOR_ITEM:
				    OnAmbilWarnaListener onAmbilWarnaListener = new OnAmbilWarnaListener() {
				        @Override
				        public void onCancel(AmbilWarnaDialogFragment dialogFragment) {
				        }

				        @Override
				        public void onOk(AmbilWarnaDialogFragment dialogFragment, int color) {
				            Settings.getInstance().setColor(color);
				            ((TyperMenuAdaper)getList().getAdapter()).notifyDataSetChanged();
				            Fragment fragment = getFragmentManager().findFragmentById(R.id.inner_fragment);
				            if (fragment instanceof ContentTyper)
								((ContentTyper)fragment).setColor(color);
				        }
				    };

				    FragmentTransaction ft = getFragmentManager().beginTransaction();
				    AmbilWarnaDialogFragment colorPickerFragment = AmbilWarnaDialogFragment.newInstance(Settings.getInstance().getColor());
				    colorPickerFragment.setOnAmbilWarnaListener(onAmbilWarnaListener);

				    colorPickerFragment.show(ft, "color_picker_dialog");
				    break;
				default:
					break;
				}
				((MainActivity)getActivity()).getMenu().toggle();
			}
		});
		super.onViewCreated(view, savedInstanceState);
	}

	private List<MenuItem> initMenuData() {
		List<MenuItem> menuList = new ArrayList<MenuItem>();
		menuList.add(new MenuItem.SeparatorItem(getString(R.string.menu_choose_source)));
		String [] source_names = getResources().getStringArray(R.array.source_names);
		for (int i = 0; i < source_names.length; i++) {
			menuList.add(new MenuItem.SourceItem(source_names[i] , i));
		}
		menuList.add(new MenuItem.SeparatorItem(getString(R.string.menu_extra_source)));
		for(String name : getResources().getStringArray(R.array.extra_source_names))
			menuList.add(new MenuItem.ExtraSourceItem(name));
		menuList.add(new MenuItem.SeparatorItem(getString(R.string.typer_settings)));
		menuList.add(new MenuItem.SpeedItem(getString(R.string.typer_speed)));
		menuList.add(new MenuItem.ColorItem(getString(R.string.typer_color)));
		menuList.add(new MenuItem.SizeItem(getString(R.string.text_size)));
		menuList.add(new MenuItem.SeparatorItem(getString(R.string.menu_progress)));
		menuList.add(new MenuItem.ProgressItem(null));
		return menuList;
			
	}

	@Override
	public void onOpened() {
		for (int i = 0; i < getList().getAdapter().getCount(); i++) {
			if((((MenuItem)getList().getAdapter().getItem(i)).mName).equals(Settings.getInstance().getSourceId())){
					getList().setItemChecked(i, true);
					break;
			}
		}
	}
	
	private static class MenuItem{
		String mName;

		public MenuItem(String name) {
			mName = name;
		}
		
		private static final class SeparatorItem extends MenuItem{

			public SeparatorItem(String name) {
				super(name);
			}}
		private static final class ExtraSourceItem extends MenuItem{

			public ExtraSourceItem(String name) {
				super(name);
			}}
		private static final class SourceItem extends MenuItem{
			private int mPosition = -1;
			public SourceItem(String name , int position) {
				super(name);
				mPosition = position;
			}}
		private static final class SpeedItem extends MenuItem{

			public SpeedItem(String name) {
				super(name);
			}}
		private static final class ColorItem extends MenuItem{

			public ColorItem(String name) {
				super(name);
			}}
		private static final class ProgressItem extends MenuItem{

			public ProgressItem(String name) {
				super(name);
			}}
		private static final class SizeItem extends MenuItem{

			public SizeItem(String name) {
				super(name);
			}}
		
	}
	
	public ListView getList() {
		return mList;
	}

	public void setList(ListView mList) {
		this.mList = mList;
	}

	public final static class TyperMenuAdaper extends BaseAdapter{
		private OnProgressBarChangedListener mProgressBarChangedListener;
		private final List<MenuItem> mItems;
		private LayoutInflater mInflater;
		private ProgressBar mProgressBar;
		
		public TyperMenuAdaper(List<MenuItem> items, LayoutInflater lInflater) {
			mItems = items;
			mInflater = lInflater;
		}
		private static final int SEPARATOR_ITEM   	= 0;
		private static final int SOURCE_ITEM 		= 1;
		private static final int SPEED_ITEM 		= 2;
		private static final int COLOR_ITEM 		= 3;
		private static final int PROGRESS_ITEM 		= 4;
		private static final int SIZE_ITEM 		= 5;
		
		
		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public boolean isEnabled(int position) {
			if(mItems.get(position) instanceof MenuItem.SourceItem){
				MenuItem.SourceItem source_item = (MenuItem.SourceItem)mItems.get(position);
				return Settings.getInstance().isAvailable(source_item.mPosition);
			}
				
			
			return !(mItems.get(position) instanceof MenuItem.SeparatorItem || mItems.get(position) instanceof MenuItem.ProgressItem);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MenuItem item = mItems.get(position);
			
			switch (getItemViewType(position)) {
			case SEPARATOR_ITEM:
				convertView = mInflater.inflate(R.layout.sliding_menu_separator, null);
				((TextView)convertView.findViewById(R.id.separator_tv)).setText(item.mName);
				convertView.setClickable(false);
				break;
			case SOURCE_ITEM:
				convertView = mInflater.inflate(R.layout.sliding_menu_item, null);
				((CheckedTextView)convertView.findViewById(android.R.id.text1)).setText(item.mName);
				convertView.setTag(item.mName);
				if(item instanceof MenuItem.SourceItem){
					((CheckedTextView)convertView.findViewById(android.R.id.text1)).setEnabled(Settings.getInstance().isAvailable(((MenuItem.SourceItem)item).mPosition));
				}
				break;
			case SPEED_ITEM:
				convertView = mInflater.inflate(R.layout.sliding_menu_speed, null);
				((TextView)convertView.findViewById(R.id.speed_tv)).setText(item.mName);
				((TextView)convertView.findViewById(R.id.speed_value_tv)).setText(String.valueOf(Settings.getInstance().getSpeed() + 1));
				convertView.setTag(item.mName);
				break;
			case SIZE_ITEM:
				convertView = mInflater.inflate(R.layout.sliding_menu_speed, null);
				((TextView)convertView.findViewById(R.id.speed_tv)).setText(item.mName);
				((TextView)convertView.findViewById(R.id.speed_value_tv)).setText(String.valueOf(Settings.getInstance().getTextSize()));
				convertView.setTag(item.mName);
				break;
			case COLOR_ITEM:
				convertView = mInflater.inflate(R.layout.sliding_menu_speed, null);
				((TextView)convertView.findViewById(R.id.speed_tv)).setText(item.mName);
				((TextView)convertView.findViewById(R.id.speed_value_tv)).setText("#" + Integer.toHexString(Settings.getInstance().getColor()).substring(2));
				((TextView)convertView.findViewById(R.id.speed_value_tv)).setTextColor(Settings.getInstance().getColor());
				convertView.setTag(item.mName);
				break;
			case PROGRESS_ITEM:
				int progress = 0;
				if(mProgressBar!=null)
					progress = mProgressBar.getProgress();
				convertView = mInflater.inflate(R.layout.progress_item, null);
				mProgressBar = ((ProgressBar)convertView.findViewById(R.id.progress_item));
				if(progress!=0)mProgressBar.setProgress(progress);
				if(mProgressBarChangedListener != null) mProgressBarChangedListener.onProgressBarChanged(mProgressBar);
				break;
			}
			
			return convertView;
		}
		
		@Override
		public int getItemViewType(int position) {
			MenuItem item = mItems.get(position);
			if(item instanceof MenuItem.SeparatorItem) return SEPARATOR_ITEM;
			else if (item instanceof MenuItem.ExtraSourceItem) return SOURCE_ITEM;
			else if (item instanceof MenuItem.SourceItem) return SOURCE_ITEM;
			else if (item instanceof MenuItem.SpeedItem) return SPEED_ITEM;
			else if (item instanceof MenuItem.ColorItem) return COLOR_ITEM;
			else if (item instanceof MenuItem.ProgressItem) return PROGRESS_ITEM;
			else if (item instanceof MenuItem.SizeItem) return SIZE_ITEM;
			throw new IllegalStateException("Something wrong!");
		}
		
		
		@Override
		public int getViewTypeCount() {
			return 6;
		}

		public OnProgressBarChangedListener getProgressBarChangedListener() {
			return mProgressBarChangedListener;
		}

		public void setProgressBarChangedListener(
				OnProgressBarChangedListener mProgressBarChangedListener) {
			this.mProgressBarChangedListener = mProgressBarChangedListener;
		}
		
		public ProgressBar getmProgressBar() {
			return mProgressBar;
		}

		public void setmProgressBar(ProgressBar mProgressBar) {
			this.mProgressBar = mProgressBar;
		}
	} 
}
