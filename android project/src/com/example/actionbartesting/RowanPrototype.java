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
package com.example.actionbartesting;

import java.util.Map;

import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.example.actionbartesting.fragments.FoodCommentFragment;
import com.example.actionbartesting.fragments.FoodRatingFragment;
import com.example.actionbartesting.fragments.HomescreenFragment;
import com.example.actionbartesting.fragments.WebViewFragment;
import com.example.actionbartesting.fragments.WebsiteListingFragment;
import com.example.actionbartesting.fragments.WebsiteListingFragment.ListType;
/**
 * The Activity handling transactions of various fragments
 * 
 * @author tomrenn
 */
public class RowanPrototype extends SherlockFragmentActivity implements ActivityFacade{
	private WebViewFragment displayingWebViewFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Necessary for showing progress indicator in the Actionbar 
        requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.fragment_holder);
		
		FoodRatingFragment.prefetch(this, false);
		
		// think the lines below were taken from the Google IO 2012 app
		//This is a workaround for http://b.android.com/15340 from http://stackoverflow.com/a/5852198/132047
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.rowan_actionbar_background_pattern_tile);
            bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            getSupportActionBar().setBackgroundDrawable(bg);
        }
        
        // add homescreen fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        HomescreenFragment fragment = new HomescreenFragment();
        fragmentTransaction.add(R.id.fragmentHolder, fragment);
        fragmentTransaction.commit();
	}

	/**
	 * Various actions this Activity must perform
	 * Primary reason of actions are to load different fragments
	 */
	@Override
	public void perform(ApplicationAction action, Bundle data) {
		Fragment fragment;
		switch(action) {
		case LAUNCH_WEBSITES:
			fragment = WebsiteListingFragment.newInstance(ListType.COMMON_WEBSITES);
			placeNewFragment(fragment);
	        break;
		case LAUNCH_ORGANIZATIONS:
			fragment = WebsiteListingFragment.newInstance(ListType.CLUBS);
			placeNewFragment(fragment);
	        break;
		case LAUNCH_URL:
			fragment = WebViewFragment.newInstance((String)data.get(WebViewFragment.ADDRESS));
			placeNewFragment(fragment);
			displayingWebViewFragment = (WebViewFragment)fragment;
	        break;
		case LAUNCH_RATINGS:
//			FoodRatingActivity activity = new FoodRatingActivity();
//			Intent intent = new Intent(this, FoodRatingActivity.class);
//			startActivity(intent);
			fragment = new FoodRatingFragment();
			placeNewFragment(fragment);
			break;
		case LAUNCH_RATINGS_COMMENT:
			fragment = new FoodCommentFragment();
			fragment.setArguments(data);
			placeNewFragment(fragment);
			
	    default:
	    	
		}
	}
	
	/**
	 * Go back in the WebView when displaying a WebViewFragment
	 * Otherwise, use the default action
	 */
	@Override
	public void onBackPressed() {
		if (displayingWebViewFragment != null && displayingWebViewFragment.isVisible()) {
			// see if we can go back
			if (displayingWebViewFragment.getWebView().canGoBack()) {
				displayingWebViewFragment.getWebView().goBack();
			}
			else {
				super.onBackPressed();
			}
		}
		else {
			super.onBackPressed();
		}
	}
	
	/**
	 * Replaces the current fragment the activity is showing with a different one
	 * It also places the transaction on the back stack 
	 * @param f New fragment to place in activity
	 */
	private void placeNewFragment(Fragment f) {
		FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHolder, f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
	}

	/**
	 * Displays a locating indicator in the top right of the action bar
	 */
	@Override
	public void showLoading(boolean isLoading) {
		if (isLoading) {
			setProgressBarIndeterminateVisibility(Boolean.TRUE);
		}
		else {
			setProgressBarIndeterminateVisibility(Boolean.FALSE);
		}
	}

	@Override
	public void showLoading(int progress) {
		int scaledProgress = (Window.PROGRESS_END - Window.PROGRESS_START) / 100 * progress;
        setSupportProgress(scaledProgress);
	}
}
