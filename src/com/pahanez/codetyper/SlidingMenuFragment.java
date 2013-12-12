package com.pahanez.codetyper;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
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
				Fragment fragment = getFragmentManager().findFragmentById(R.id.inner_fragment);
				if (fragment instanceof OnSourceChanged)
					((OnSourceChanged)fragment).sourceChanged((String)view.getTag());
				((MainActivity)getActivity()).getMenu().toggle();
				Settings.getInstance().setSourceId(position);
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
		return menuList;
			
	}

	@Override
	public void onOpened() {
		mList.setItemChecked(Settings.getInstance().getSourceId(), true	);
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
		private static final class OptionItem extends MenuItem{

			public OptionItem(String name) {
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
		private static final int OPTION_ITEM 		= 2;
		
		
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
			}
			
			return convertView;
		}
		
		@Override
		public int getItemViewType(int position) {
			MenuItem item = mItems.get(position);
			return item instanceof MenuItem.SeparatorItem ? SEPARATOR_ITEM : item instanceof MenuItem.SourceItem ? SOURCE_ITEM : OPTION_ITEM ;
		}
		
		
		@Override
		public int getViewTypeCount() {
			return 3;
		}
	} 
}
