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
package edu.rowan.app.fragments;

import rowan.application.quickaccess.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

import edu.rowan.app.util.ActivityFacade;
import edu.rowan.app.util.ActivityFacade.ApplicationAction;


public class InfoFragment extends SherlockFragment implements OnClickListener{
	private ActivityFacade activity;
	private String GITHUB_ADDR = "https://github.com/tomrenn/Rowan_Prototype";
	private String FACEBOOK_ADDR = "http://facebook.com/openrowanapp";
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		activity = (ActivityFacade)getActivity();
	}
	
	/**
	 * Construct the view for this fragment.
	 * The core layout of this fragment is a CarouselView and grid of available buttons
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().setTitle("About");

		View view = inflater.inflate(R.layout.info_page, container, false);
		ImageButton button = (ImageButton)view.findViewById(R.id.githubButton);
		button.setOnClickListener(this);
		ImageButton fbutton = (ImageButton)view.findViewById(R.id.facebookButton);
		fbutton.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Bundle data = new Bundle();

		// setup link
		switch (id) {
		case R.id.githubButton:
			data.putString(WebViewFragment.ADDRESS, GITHUB_ADDR);
			break;
		case R.id.facebookButton:
			data.putString(WebViewFragment.ADDRESS, FACEBOOK_ADDR);
			break;
		}
		
		// launch link
		activity.perform(ApplicationAction.LAUNCH_URL, data);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().setTitle("Rowan University");
        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	/**
	 * Handle Actionbar menu items. Only have to deal with up navigation 
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	getActivity().setTitle("Rowan University");
            getSherlockActivity().getSupportFragmentManager().popBackStackImmediate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
