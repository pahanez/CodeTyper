package com.pahanez.codetyper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pahanez.codertyper.R;

public class MenuDrawerFragment extends Fragment{
	
	private String[] mPlanetTitles = new String[]{"Mercury" , "Venera" , "Earth"};
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_layout, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
	        mDrawerList = (ListView) view.findViewById(R.id.left_drawer);

	        // Set the adapter for the list view
	        mDrawerList.setAdapter(new ArrayAdapter<String>(getActivity(),
	               android.R.layout.simple_list_item_1, mPlanetTitles));
	        // Set the list's click listener
	        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	    	Log.e("p37td8" , "id " + id);
	    }
	}
}
