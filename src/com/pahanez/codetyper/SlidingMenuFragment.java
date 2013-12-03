package com.pahanez.codetyper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pahanez.codertyper.R;

public class SlidingMenuFragment extends Fragment {

	private ListView mList;
	private String[] mFileTitles = new String[] { "kexec.c", "kexec.c",
			"Activity.java" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_layout, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mList = (ListView) view.findViewById(R.id.menu_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.menu_item, mFileTitles);
		mList.setAdapter(adapter);

		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Fragment fragment = getFragmentManager().findFragmentById(R.id.inner_fragment);
				if (fragment instanceof OnSourceChanged)
					((OnSourceChanged)fragment).sourceChanged(position);
				((MainActivity)getActivity()).getMenu().toggle();
			}
		});
	}
}
