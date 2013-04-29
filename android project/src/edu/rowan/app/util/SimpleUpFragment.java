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
package edu.rowan.app.util;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

import edu.rowan.app.fragments.WebViewFragment;

/** 
 * Custom Fragment that factors out the up navigation for simple fragments 
 * Also includes some convenience methods for calling on the ActivityFacade 
 * 
 * @author tomrenn
 *
 */
public class SimpleUpFragment extends SherlockFragment{
	private ActivityFacade activity;
	
	// 
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		activity = (ActivityFacade)getActivity();
	}
	
	// set up button enabled
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	// tell activity to start webviewfragment loading the given URL
	public void openBrowserTo(String url) {
		Bundle bundle = new Bundle();
		bundle.putString(WebViewFragment.ADDRESS, url);
		activity.perform(ActivityFacade.ApplicationAction.LAUNCH_URL, bundle);
	}
	
	// removes the up navigation from the Action Bar
	@Override
	public void onDestroyView() {
		super.onDestroyView();
        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	/**
	 * Handle Actionbar menu items. Only have to deal with up navigation 
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            getSherlockActivity().getSupportFragmentManager().popBackStackImmediate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	public void showLoading(int progress){
		activity.showLoading(progress);
	}
	
	public void showLoading(boolean isLoading){
		activity.showLoading(isLoading);
	}
}


