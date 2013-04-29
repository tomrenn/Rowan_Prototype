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
import edu.rowan.app.util.SimpleUpFragment;


public class InfoFragment extends SimpleUpFragment implements OnClickListener{
	private String GITHUB_ADDR = "https://github.com/tomrenn/Rowan_Prototype";
	private String FACEBOOK_ADDR = "http://facebook.com/openrowanapp";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * Construct the view for this fragment.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		getActivity().setTitle("About");

		View view = inflater.inflate(R.layout.info_page, container, false);
		ImageButton button = (ImageButton)view.findViewById(R.id.githubButton);
		button.setOnClickListener(this);
		ImageButton fbutton = (ImageButton)view.findViewById(R.id.facebookButton);
		fbutton.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View view) {
		String url = "";

		// setup link
		switch (view.getId()) {
		case R.id.githubButton:
			url = GITHUB_ADDR;
			break;
		case R.id.facebookButton:
			url = FACEBOOK_ADDR;
			break;
		}
		
		openBrowserTo(url);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().setTitle("Rowan University");
	}
	
}
