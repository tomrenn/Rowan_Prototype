/*
 * Copyright (C) 2010 The Android Open Source Project
 * Copyright (C) 2013 Tom Renn
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
 * MODIFIED BY: Tom Renn
 */
package edu.rowan.app.fragments;

import rowan.application.quickaccess.R;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.rowan.app.util.ActivityFacade;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * A fragment that displays a WebView.
 * <p>
 * The WebView is automically paused or resumed when the Fragment is paused or resumed.
 */
public class WebViewFragment extends SherlockFragment {
    private WebView mWebView;
    private boolean mIsWebViewAvailable;
    private String mUrl;
    private ActivityFacade mActivity;
    public static final String ADDRESS = "web address";
    
    /**
     * Create a new WebViewFragment with the preferred url to load
     * @param url Web address
     * @return new WebViewFragment
     */
    public static WebViewFragment newInstance(String url) {
    	WebViewFragment f = new WebViewFragment();
    	Bundle args = new Bundle();
    	args.putString(ADDRESS, url);
    	f.setArguments(args);
    	return f;
    }
    
    // Set the URL address to load at the start
    public void setUrlToLoad(String url){
    	mUrl = url;
    }
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		mActivity = (ActivityFacade) getActivity();
		 
		if (savedInstanceState != null) {
			mUrl = savedInstanceState.getString(ADDRESS);
		}
		if (getArguments().containsKey(ADDRESS)){
			mUrl = getArguments().getString(ADDRESS);
		}
		
		// for some reason blackboard doesn't work in the webview. Fuck blackboard.
		if (mUrl.equals(getResources().getString(R.string.blackboard_URL))){
			Toast.makeText(getActivity(), R.string.blackboard_issue, Toast.LENGTH_LONG).show();
			openInBrowser();
		}
		mWebView.loadUrl(mUrl);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		// set up navigation possible
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.webfragment_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.default_browser_icon:
			openInBrowser();
			return true;
		case android.R.id.home:
            getSherlockActivity().getSupportFragmentManager().popBackStackImmediate();
            return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Open the current web address in the users primary web browser
	 */
	public void openInBrowser() {
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
//		String url = mWebView.getUrl();
//		if (url == null)
//			return;
		Uri uri = Uri.parse(mUrl);
		intent.setData(uri);
		startActivity(intent);
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
    
    /**
     * Called to instantiate the view. Creates and returns the WebView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = new WebView(getActivity());
        mWebView.setWebViewClient(new InnerWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) 
            {
            	mActivity.showLoading(progress);
            }
        });
        WebSettings webSettings = mWebView.getSettings();
        // next two lines let the WebView be fully zoomed out (show entire webpage)
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true); 
        mIsWebViewAvailable = true;
        return mWebView;
    }

    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    	mActivity.showLoading(false);
    }

    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    /**
     * Called when the WebView has been detached from the fragment.
     * The WebView is no longer available after this time.
     */
    @Override
    public void onDestroyView() {
        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mIsWebViewAvailable = false;
        super.onDestroyView();
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
    
    /**
     * Gets the WebView.
     */
    public WebView getWebView() {
        return mIsWebViewAvailable ? mWebView : null;
    }
    
    /* To ensure links open within the application */
    private class InnerWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    
    
}