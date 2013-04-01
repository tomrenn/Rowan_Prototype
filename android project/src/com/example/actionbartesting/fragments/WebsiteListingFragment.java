/**
 * Copyright 2013 Tom Renn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.example.actionbartesting.fragments;

import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;
import com.example.actionbartesting.ActivityFacade;
import com.example.actionbartesting.R;
import com.example.actionbartesting.ActivityFacade.ApplicationAction;
import com.example.actionbartesting.R.array;
import com.example.actionbartesting.util.BasicListAdapter;

/**
 * A list fragment used to browse and launch website links
 * 
 * @author tomrenn
 */
public class WebsiteListingFragment extends SherlockListFragment{
	private ActivityFacade activity;
	private String[] websiteUrls;
	public static final String LIST_TYPE = "ListType";
	public enum ListType {
		COMMON_WEBSITES,
		CLUBS
	}
	private ListType websiteListType;
	
	/**
	 * Used to create new fragments filled with specific list of websites
	 * 
	 * @param type The kind of websites the fragment will list
	 * @return The new fragment
	 */
	public static WebsiteListingFragment newInstance(ListType type) {
		WebsiteListingFragment fragment = new WebsiteListingFragment();
		Bundle args = new Bundle();
		args.putSerializable(LIST_TYPE, type);
		fragment.setArguments(args);
		return fragment;
	}
	
	/**
	 * Initializing the fragment 
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		// Allow 'up' navigation
		setHasOptionsMenu(true);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		activity = (ActivityFacade) getActivity();
	
		if (getArguments() != null && getArguments().containsKey(LIST_TYPE)){
			Log.d("ListUpFragment", "onActivityCreated: got fragmentType from arguments()");
			this.websiteListType = (ListType) getArguments().getSerializable(LIST_TYPE);;
		}
		loadListData(websiteListType);
	}
	
	public void onStart() {
		super.onStart();
		Log.d("ListUpFragment", "On Start");
	}
	
	/**
	 * Loads the listAdapter with the information related to this fragments LIST_TYPE
	 * And load this fragment with the urls associated with the data in the list
	 */
	public void loadListData(ListType type) {
		BasicListAdapter<String> adapter = new BasicListAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		String[] items = null;
		Resources res = getResources();
		
		switch (type) {
		case COMMON_WEBSITES:
			items = res.getStringArray(R.array.websites);
			websiteUrls = res.getStringArray(R.array.websiteURLs);
			break;
		case CLUBS:
			items = res.getStringArray(R.array.clubs);
			websiteUrls = res.getStringArray(R.array.clubsURLs);
			break;
		default:
			items = res.getStringArray(R.array.websites);
			websiteUrls = getResources().getStringArray(R.array.websiteURLs);
			break;
		}
		adapter.addAllItems(items);
		this.setListAdapter(adapter);
	}
	 
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
		Log.d("ListUpFragment", "On save instanceState");
        outState.putSerializable(LIST_TYPE, this.websiteListType);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null){
			Log.d("ListUpFragment", "onCreate: (savedInstanceState != null)");
			ListType type = (ListType)savedInstanceState.getSerializable(LIST_TYPE);
			this.websiteListType = type;
		}
	}
	
	// change the basic listView background
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = super.onCreateView(inflater, container, savedInstanceState);
		view.setBackgroundColor(Color.BLACK);
		return view;
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id){
		String itemClicked = (String) websiteUrls[position];
		Toast.makeText(getActivity(), itemClicked, Toast.LENGTH_SHORT).show();
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(WebViewFragment.ADDRESS, itemClicked);
		activity.perform(ApplicationAction.LAUNCH_URL, data);
	}

	/**
	 * Remove the ability to go up through the actionbar
	 */
	public void onStop() {
		super.onStop();
		Log.d("ListUpFragment", "On Stop");
        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	public void onPause(){
		super.onPause();
		Log.d("ListUpFragment", "On pause");
	}

	
	/**
	 * Handle Actionbar menu items. Only have to deal with up navigation 
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
    		Log.d("ListUpFragment", "GOING UP");
            getSherlockActivity().getSupportFragmentManager().popBackStackImmediate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
