package com.xiaocai_.romaunt_1;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class StoryFragment extends Fragment implements OnItemClickListener{
	
	private static final String TAG = "StoryFragment";
	
	private ListViewCompat mListView;
	
	private View view;
	
	private SlideAdapter slideAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_story, container, false);
		initView();
		return view;
	}
	
	private void initView() { 
		mListView = (ListViewCompat) view.findViewById(R.id.list_story);
		slideAdapter = new SlideAdapter(getActivity());
		mListView.setAdapter(slideAdapter);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		 Log.e(TAG, "À≠œ»£ø");
		 if(slideAdapter.getmLastSlideViewWithStatusOn() == null)
		 Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
		
	}

}
