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
import android.widget.SeekBar;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.pahanez.codertyper.R;

public class SlidingMenuFragment extends Fragment implements OnOpenedListener{

	private ListView mList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_layout, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		
		mList = (ListView) view.findViewById(R.id.menu_list);
		mList.setAdapter(new TyperMenuAdaper(initMenuData()));
		
		mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (mList.getAdapter().getItemViewType(position)) {
				case TyperMenuAdaper.SOURCE_ITEM:
					Fragment fragment = getFragmentManager().findFragmentById(R.id.inner_fragment);
					if (fragment instanceof ContentTyper)
						((ContentTyper)fragment).sourceChanged((String)view.getTag());
					Settings.getInstance().setSourceId((String)view.getTag());
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
				case TyperMenuAdaper.COLOR_ITEM:
				    OnAmbilWarnaListener onAmbilWarnaListener = new OnAmbilWarnaListener() {
				        @Override
				        public void onCancel(AmbilWarnaDialogFragment dialogFragment) {
				        }

				        @Override
				        public void onOk(AmbilWarnaDialogFragment dialogFragment, int color) {
				            Settings.getInstance().setColor(color);
				            ((TyperMenuAdaper)mList.getAdapter()).notifyDataSetChanged();
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
		for(String name : getResources().getStringArray(R.array.source_names))
			menuList.add(new MenuItem.SourceItem(name));
		menuList.add(new MenuItem.SeparatorItem(getString(R.string.typer_settings)));
		menuList.add(new MenuItem.SpeedItem(getString(R.string.typer_speed)));
		menuList.add(new MenuItem.ColorItem(getString(R.string.typer_color)));
		return menuList;
			
	}

	@Override
	public void onOpened() {
		for (int i = 0; i < mList.getAdapter().getCount(); i++) {
			if((((MenuItem)mList.getAdapter().getItem(i)).mName).equals(Settings.getInstance().getSourceId())){
					mList.setItemChecked(i, true);
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
		private static final class SourceItem extends MenuItem{

			public SourceItem(String name) {
				super(name);
			}}
		private static final class SpeedItem extends MenuItem{

			public SpeedItem(String name) {
				super(name);
			}}
		private static final class ColorItem extends MenuItem{

			public ColorItem(String name) {
				super(name);
			}}
		
	}
	
	
	private final class TyperMenuAdaper extends BaseAdapter{
		
		private final List<MenuItem> mItems;
		
		public TyperMenuAdaper(List<MenuItem> items) {
			mItems = items;
		}
		private static final int SEPARATOR_ITEM   	= 0;
		private static final int SOURCE_ITEM 		= 1;
		private static final int SPEED_ITEM 		= 2;
		private static final int COLOR_ITEM 		= 3;
		
		
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
			
			return !(mItems.get(position) instanceof MenuItem.SeparatorItem);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MenuItem item = mItems.get(position);
			
			switch (getItemViewType(position)) {
			case SEPARATOR_ITEM:
				convertView = getLayoutInflater(getArguments()).inflate(R.layout.sliding_menu_separator, null);
				((TextView)convertView.findViewById(R.id.separator_tv)).setText(item.mName);
				convertView.setClickable(false);
				break;
			case SOURCE_ITEM:
				convertView = getLayoutInflater(getArguments()).inflate(R.layout.sliding_menu_item, null);
				((CheckedTextView)convertView.findViewById(android.R.id.text1)).setText(item.mName);
				convertView.setTag(item.mName);
				break;
			case SPEED_ITEM:
				convertView = getLayoutInflater(getArguments()).inflate(R.layout.sliding_menu_speed, null);
				((TextView)convertView.findViewById(R.id.speed_tv)).setText(item.mName);
				((TextView)convertView.findViewById(R.id.speed_value_tv)).setText(String.valueOf(Settings.getInstance().getSpeed() + 1));
				convertView.setTag(item.mName);
				break;
			case COLOR_ITEM:
				convertView = getLayoutInflater(getArguments()).inflate(R.layout.sliding_menu_speed, null);
				((TextView)convertView.findViewById(R.id.speed_tv)).setText(item.mName);
				((TextView)convertView.findViewById(R.id.speed_value_tv)).setText("#" + Integer.toHexString(Settings.getInstance().getColor()).substring(2));
				((TextView)convertView.findViewById(R.id.speed_value_tv)).setTextColor(Settings.getInstance().getColor());
				convertView.setTag(item.mName);
				break;
			}
			
			return convertView;
		}
		
		@Override
		public int getItemViewType(int position) {
			MenuItem item = mItems.get(position);
			return item instanceof MenuItem.SeparatorItem ? SEPARATOR_ITEM : item instanceof MenuItem.SourceItem ? SOURCE_ITEM : item instanceof MenuItem.SpeedItem?SPEED_ITEM : COLOR_ITEM ;
		}
		
		
		@Override
		public int getViewTypeCount() {
			return 4;
		}
	} 
}
