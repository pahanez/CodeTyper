package com.pahanez.codetyper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pahanez.codertyper.R;

public class MainActivity extends FragmentActivity {
	
private String[] mFileTitles = new String[]{"kexec.c" , "kexec.c" , "Activity.java"};
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
               R.layout.menu_item, mFileTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		initMenuFrament();
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	    	Log.e("p37td8" , "k : " + parent);
	    	Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.inner_fragment);
	    	if ( fragment instanceof OnSourceChanged ){
	    		((OnSourceChanged)fragment).sourceChanged(position);
	    	}
	    	mDrawerLayout.closeDrawers();
	    }
	}

	private void initMenuFrament() {
		if(getSupportFragmentManager().findFragmentById(R.id.inner_fragment) == null)
			getSupportFragmentManager().beginTransaction().add(R.id.inner_fragment, new MenuFragment()).commit();
	}

	
}
