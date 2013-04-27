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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import rowan.application.quickaccess.R;
import com.viewpagerindicator.TitlePageIndicator;

import edu.rowan.app.util.ActivityFacade;
import edu.rowan.app.util.FoodComment;
import edu.rowan.app.util.FoodCommentsAdapter;
import edu.rowan.app.util.JsonQueryManager;
import edu.rowan.app.util.ActivityFacade.ApplicationAction;
import edu.rowan.app.util.JsonQueryManager.Callback;

public class FoodRatingFragment extends SherlockFragment{
	public static final String PREFS = "food rating prefs";
	private static final String CREATE_USER_ADDR = "http://therowanuniversity.appspot.com/food/createUser.json";
	private static final String CAST_VOTE_ADDR = "http://therowanuniversity.appspot.com/food/vote";
	public static final String USER_ID = "userID";
	public static final String LAST_REFRESH = "last updated";
	private static final int UPDATE_INTERVAL = 3; // num hours
	private static final String FOOD_RATING_ADDR = "http://therowanuniversity.appspot.com/food/ratings";
	public static final String FOOD_ENTRY_ID = "foodID";
	private static final String TYPE_MARKETPLACE = "marketplace";
	private static final String TYPE_SMOKEHOUSE = "smokehouse";
	public static final int VOTE_UP = 1;
	public static final int VOTE_DOWN = 0;
	// eventually change this param to be : http://stackoverflow.com/a/13241629/121654
	private static final int VIEW_PAGER_ID = 1337;
	private static final String FRAGMENT_TYPE = "fragment rating type";
	private String[] RATING_TYPES = new String[] {TYPE_MARKETPLACE, TYPE_SMOKEHOUSE};
	// variable used for refreshing. 
	private int numOfTypesRefreshing = 0;
	private FoodRatingAdapter fragmentAdapter;
	private ViewPager fragmentPager;
	// handler for updating UI
	private Handler mainThreadHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		// Allow 'up' navigation
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mainThreadHandler = new Handler(getActivity().getApplicationContext().getMainLooper());
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.refresh_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.refresh_icon:
			// don't refresh if we're in the process of refreshing
			if (numOfTypesRefreshing == 0)
				refresh();
			return true;
		case android.R.id.home:
            getSherlockActivity().getSupportFragmentManager().popBackStackImmediate();
            return true;
		}
		return false;
	}
	
	// refresh the rating data
	private void refresh() {
		((ActivityFacade)getActivity()).showLoading(true);
		numOfTypesRefreshing = RATING_TYPES.length;
		prefetch(getActivity(), true, this);
	}
	
	/**
	 * Start pre-fetching data (load data before it is needed)
	 * 
	 * @param reference Activity reference required for AQuery
	 */
	public static void prefetch(Activity reference, boolean updateCache, FoodRatingFragment callback) {
		SharedPreferences prefs = reference.getSharedPreferences(FoodRatingFragment.PREFS, 0);
		if (!prefs.contains(FoodRatingFragment.USER_ID)) { // we need to get a userId
			getUserID(reference);
		}
		long lastUpdate = prefs.getLong(LAST_REFRESH, 0);
		// [difference] / (milis * seconds * minutes)
		int hourDifference = (int)(Calendar.getInstance().getTimeInMillis() - lastUpdate) / (1000 * 60 * 60);
		Log.d("FoodRatingFragment", "Hour difference: " + hourDifference);
		if (updateCache || hourDifference > UPDATE_INTERVAL || lastUpdate == 0) {
			Log.d("FoodRatingFragment", "Fetching Ratings");
			fetchAllRatings(reference, callback);
		}
		else {
			Log.d("FoodRatingFragment", "Cached food ratings");
			Log.d("Marketplace", prefs.getString(Integer.toString(prefs.getInt(TYPE_MARKETPLACE, 0)), "CACHE FAIL"));
			Log.d("Smokehouse", prefs.getString(Integer.toString(prefs.getInt(TYPE_SMOKEHOUSE, 0)), "CACHE FAIL"));

		}
	}
	
	// call on both food rating types
	private static void fetchAllRatings(final Activity reference, FoodRatingFragment callback) {
		JsonQueryManager jsonQuery = JsonQueryManager.getInstance(reference);
		fetchRating(jsonQuery, TYPE_MARKETPLACE, reference, callback);
		fetchRating(jsonQuery, TYPE_SMOKEHOUSE, reference, callback);
	}
	
	private static void fetchRating(JsonQueryManager jsonQuery, final String foodType, 
			final Activity reference, final FoodRatingFragment callback) {
		final String address = FOOD_RATING_ADDR;
		Map<String,String> params = new HashMap<String, String>();
		params.put("type", foodType);
		jsonQuery.requestJson(address, params, 
	    		new Callback() {
					@Override
					public void receiveJson(JSONObject json, String origin) {
						try {
							if (origin == address){
								if (json != null) {
									SharedPreferences prefs = reference.getSharedPreferences(PREFS, 0);
									updatePreferencesData(prefs, foodType, json);
									Log.d("Homescreen", "saved json: " + json.toString());
									// make sure callback is still an active fragment
									if (callback != null && callback.isResumed()){
										callback.ratingsUpdated(reference);
									}
								}
							}
						}
						catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	/**
	 * Stores received JSON data the given SharedPreferences object
	 * Below an example map how the data is stored:
	 * 
	 * foodType -> entryId
	 * entryId -> data.toString()  
	 * 
	 * When a new entry for a particular foodType is created, it will have a different entryId.
	 * When new entries are received, we must clear the data of previous entries
	 * 
	 * @param prefs Should be sharedPreferences created with FoodRatingFragment.PREFS
	 * @param foodType TYPE_MARKETPLACE or TYPE_SMOKEHOUSE
	 * @param data The Json being put into the preferences
	 * @throws JSONException
	 */
	private static void updatePreferencesData(SharedPreferences prefs, String foodType, JSONObject data) throws JSONException{
		Editor edit = prefs.edit();
		int newFoodId = data.getJSONObject("entry").getInt("id");
		int oldFoodId = prefs.getInt(foodType, -1);
		// oldFoodEntry exists && newEntryId is different. Have a new entry, clear old data
		if (oldFoodId != -1 && newFoodId != oldFoodId) {
			Log.d("FoodRatingFragment", "NEW ENTRY DETECTED: removing old entry and vote state");
			edit.remove(Integer.toString(oldFoodId));
			// remove local vote state
			edit.remove(SubRatingFragment.getLocalVoteKey(foodType));
		}
		edit.putInt(foodType, newFoodId);
		edit.putString(Integer.toString(newFoodId), data.toString());
		edit.putLong(LAST_REFRESH, Calendar.getInstance().getTimeInMillis());
		edit.commit();
	}
	
	
	// get and store userId into sharedPreferences
	private static void getUserID(final Activity reference) {
		JsonQueryManager jsonQuery = JsonQueryManager.getInstance(reference);
		
		Map<String, String> params = new HashMap<String, String>();
	    params.put("ostype", Build.MANUFACTURER +", " + Build.MODEL + " version: " + Build.VERSION.SDK_INT);
	    jsonQuery.requestJson(CREATE_USER_ADDR, params, 
    		new Callback() {
				@Override
				public void receiveJson(JSONObject json, String origin) {
					try {
						if (origin == CREATE_USER_ADDR){
							if (json != null) {
								SharedPreferences prefs = reference.getSharedPreferences(PREFS, 0);
								Editor edit = prefs.edit();
								edit.putString(USER_ID, json.getString(USER_ID));
								edit.commit();
								Log.d("Homescreen", "received json: " + json.getString("userID"));
							}
						}
					}
					catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		LinearLayout view = new LinearLayout(getActivity());
		view.setOrientation(LinearLayout.VERTICAL);
		// view pager indicator
		TitlePageIndicator pageIndicator = new TitlePageIndicator(getActivity());
		pageIndicator.setBackgroundResource(R.color.rowanBrown);
		fragmentPager = new ViewPager(getActivity());
		fragmentPager.setId(VIEW_PAGER_ID);
		fragmentAdapter = new FoodRatingAdapter(getChildFragmentManager());
		fragmentPager.setAdapter(fragmentAdapter);
		//View view = inflater.inflate(R.layout.activity_main, container, false);
		
		pageIndicator.setViewPager(fragmentPager);
		view.addView(pageIndicator);
		view.addView(fragmentPager);
		return view;
	}
	
	/**
	 * Callback notifying one rating type has finished refreshing
	 * Update the UI if we have finished refreshing all types
	 * synchronized to prevent one of the rating types from calling this at the same time
	 * @param reference
	 */
	public synchronized void ratingsUpdated(Activity reference) {
		// decrement the waiting variable
		numOfTypesRefreshing--;
		// done refreshing all types
		if (numOfTypesRefreshing == 0) { 
			// create a runnable to execute on main thread (UI cannot be updated outside main thread)
			Runnable updateUI = new Runnable() {	
				@Override
				public void run() {
					((ActivityFacade)getActivity()).showLoading(false);
					// reload viewPager TODO: setAdapter() doesn't seem the best way to do this
					int pagerPosition = fragmentPager.getCurrentItem();
					fragmentPager.setAdapter(fragmentAdapter);
					fragmentPager.setCurrentItem(pagerPosition);
				}
			};
			mainThreadHandler.post(updateUI);
			
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	/**
	 * Simple adapter to load the different SubRatingFragments
	 * 
	 * @author tomrenn
	 *
	 */
	private class FoodRatingAdapter extends FragmentPagerAdapter {
		public FoodRatingAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int pos) {
			return SubRatingFragment.newInstance(RATING_TYPES[pos]);
		}
		@Override
		public CharSequence getPageTitle(int pos) {
			return RATING_TYPES[pos];
		}
		@Override
		public int getCount() {
			return RATING_TYPES.length;
		}
		
	}
	
	public static class SubRatingFragment extends SherlockFragment {
		private String ratingType;
		private ListView commentList;
		private ActivityFacade activity;

		static SubRatingFragment newInstance(String foodRatingType) {
			SubRatingFragment f = new SubRatingFragment();
			Bundle args = new Bundle();
			args.putString(FRAGMENT_TYPE, foodRatingType);
			f.setArguments(args);
			
			return f;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			activity = (ActivityFacade)getActivity();
			// set fragment type from arguments, default to MARKETPLACE
			ratingType = getArguments() != null ? getArguments().getString(FRAGMENT_TYPE) : TYPE_MARKETPLACE;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.food_rating_layout, container, false);
			// TODO: fill the inflated view with information
			// TODO: before calling loadDataIntoView, make sure PREFS actually has data in it...
			loadDataIntoView(view);
			setupButtons(view);
			return view;
		}
		
		// load the saved vote from last time
		private void loadSavedVote(ImageButton upButton, ImageButton downButton) {
			SharedPreferences prefs = getActivity().getSharedPreferences(PREFS, 0);
			int vote = prefs.getInt(getLocalVoteKey(ratingType), -1); 
			if (vote == VOTE_DOWN) { // user last voted down
				downButton.setImageResource(R.drawable.downvote_selected);
				downButton.setClickable(false);
			}
			if (vote == VOTE_UP) {
				upButton.setImageResource(R.drawable.upvote_selected);
				upButton.setClickable(false);
			}
		}
		
		public void setupButtons(View view) {
			ImageButton upButton = (ImageButton)view.findViewById(R.id.buttonUpvote);
			ImageButton downButton = (ImageButton)view.findViewById(R.id.buttonDownvote);
			Button commentButton = (Button)view.findViewById(R.id.buttonCommentTransition);
			setupCommentButton(commentButton);
			
			setVoteActionOn(upButton, VOTE_UP, downButton);
			setVoteActionOn(downButton, VOTE_DOWN, upButton);
			loadSavedVote(upButton, downButton);
			
		}
		
		public void setupCommentButton(Button commentButton) {
			commentButton.setText( getRandomCommentText() );
			commentButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SharedPreferences prefs = getActivity().getSharedPreferences(PREFS, 0);
					int entryId = prefs.getInt(ratingType, -1);
					String userId = prefs.getString(USER_ID, "");
					// No entry or no userId to comment
					if (entryId == -1 || userId.equals("")) 
						return;
					Bundle params = new Bundle();
					params.putString(FOOD_ENTRY_ID, String.valueOf(entryId));
					params.putString(USER_ID, userId);
					activity.perform(ApplicationAction.LAUNCH_RATINGS_COMMENT, params);
				}
			});
		}
		
		/**
		 * Records the vote by:
		 * Sending vote to web service
		 * Recording the vote locally
		 * Update local preference data and tick vote up/down numbers 
		 * 
		 * @param vote
		 */
		public void castVote(int vote) {
			Log.d("FoodRatingFragment", "Vote casted: " + vote);
			sendVoteToServer(vote);
			// change preference data, change vote desc textView
			showVoteChange(vote);
			recordVoteLocally(vote);
		}
		
		public void showVoteChange(int vote) {
			SharedPreferences prefs = getActivity().getSharedPreferences(PREFS, 0);
			int previousVote = prefs.getInt(getLocalVoteKey(ratingType), -1);
			String foodId = String.valueOf(prefs.getInt(ratingType, -1));
			String jsonString = prefs.getString(foodId, null);
			if (jsonString == null)
				return;
			
			try {
				JSONObject json = new JSONObject(jsonString);
				JSONObject entry = json.getJSONObject("entry");
				int upvotes = entry.getInt("upvotes");
				int totalVotes = entry.getInt("totalVotes");
				
				// take away previous vote data
				if (previousVote != -1) {
					totalVotes = totalVotes - 1;
					if (previousVote == 1){
						upvotes = upvotes - 1;
					}
				}
				// add in new vote
				totalVotes = totalVotes + 1;
				if (vote == 1){
					upvotes = upvotes + 1;
				}
				entry.put("upvotes", upvotes);
				entry.put("totalVotes", totalVotes);
				
				Editor edit = prefs.edit();
				edit.putString(foodId, json.toString());
				edit.commit();
				View v = getView();
				loadDataIntoView( v );
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		
		private void sendVoteToServer(final int vote) {
			Map<String, String> params = new HashMap<String, String>();
			SharedPreferences prefs = getActivity().getSharedPreferences(PREFS, 0);
			String userHash = prefs.getString(USER_ID, "");
			String entryId = String.valueOf(prefs.getInt(ratingType, 0));
			params.put(USER_ID, userHash);
			params.put(FOOD_ENTRY_ID, entryId);
			params.put("vote", String.valueOf(vote));
			JsonQueryManager jsonQuery = JsonQueryManager.getInstance(getActivity());
			jsonQuery.requestJson(CAST_VOTE_ADDR, params, new Callback() {
				@Override
				public void receiveJson(JSONObject json, String origin) {
					Log.d("FoodRatingFragment", "Vote successfully made");
				}
			});
		}
		
		/**
		 * Stores the vote made in sharedPreferences to be accessed when the fragment is rebuilt
		 * @param vote 
		 */
		private void recordVoteLocally(int vote){
			SharedPreferences prefs = getActivity().getSharedPreferences(PREFS, 0);
			Editor edit = prefs.edit();
			String voteLocalKey = getLocalVoteKey(ratingType);
			edit.putInt(voteLocalKey, vote);
			edit.commit(); // TODO: refactor to maybe only commit when leaving fragment
		}

		// return key for this SubRatingFragment vote 
		public static String getLocalVoteKey(String ratingType) { return ratingType + "_local-vote"; }
		
		/**
		 * Set the action a button takes
		 * Includes: calling castVote(vote), changing button images, and disabling/enabling clicking
		 * @param button Vote Button being clicked
		 * @param vote The vote value the button will cast 
		 * @param otherButton The other Vote button to re-enable and change image (if it was changed)
		 */
		private void setVoteActionOn(final ImageButton button, final int vote, final ImageButton otherButton) {
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					castVote(vote);
					if (vote == FoodRatingFragment.VOTE_UP){ // button is buttonUp
						button.setImageResource(R.drawable.upvote_selected);
						otherButton.setImageResource(R.drawable.downvote);
					}
					else { // button is buttonDown
						button.setImageResource(R.drawable.downvote_selected);
						otherButton.setImageResource(R.drawable.upvote);
					}
					button.setClickable(false);
					otherButton.setClickable(true);
				}
			});
		}
		
		
		public void loadDataIntoView(View view) {
			SharedPreferences prefs = getActivity().getSharedPreferences(PREFS, 0);
			String typeId = Integer.toString(prefs.getInt(ratingType, -1));
			commentList = (ListView)view.findViewById(R.id.listViewComments);
			commentList.setCacheColorHint(Color.TRANSPARENT);
			// we don't have a food entry id, there is no data to setup the view
			if (typeId.equals("-1"))
				return; 
			
			String data = prefs.getString(typeId, "");
			TextView voteDesc = (TextView)view.findViewById(R.id.textTotalVotes);
			TextView titleSummary = (TextView)view.findViewById(R.id.ratingSummaryTitle);
			
			try {
				JSONObject json = new JSONObject(data);
				JSONObject entry = json.getJSONObject("entry");
				loadComments(json.getJSONObject("comments"));
				int totalVotes = entry.getInt("totalVotes");
				int upvotes = entry.getInt("upvotes");
				int downvotes = totalVotes - upvotes;
				
				voteDesc.setText(upvotes + " likes, " + downvotes + " dislikes");
				titleSummary.setText( getTitleText(totalVotes, upvotes) );
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void loadComments(JSONObject commentData) throws JSONException {
			ArrayList<String> comments = new ArrayList<String>();
			ArrayList<Long> timestamps = new ArrayList<Long>();
			List<FoodComment> listData = new ArrayList<FoodComment>();

			int i = 0;

			// while there are some comments
			while (!commentData.optString(String.valueOf(i)).equals("")) {
				String commentNum = String.valueOf( i++ );
				JSONObject comment = commentData.getJSONObject(commentNum);
				comments.add(comment.getString("comment"));
				timestamps.add(comment.getLong("date"));
			}
			
			for (int j=0; j<comments.size(); j++){
				listData.add( new FoodComment(comments.get(j), timestamps.get(j)) );
			}
			FoodCommentsAdapter adapter = new FoodCommentsAdapter(getActivity(), R.layout.food_comment_list_item, listData);
			commentList.setAdapter(adapter);
		}
		
		// get a random comment phrase
		public String getRandomCommentText() {
			String[] phrases = getResources().getStringArray(R.array.foodButtonPhrases);
			int random = (int) (Math.random() * phrases.length);
			return phrases[random];
		}
		
		public String getTitleText(int totalVotes, int upvotes) {
			String[] phrases = getResources().getStringArray(R.array.foodRatingTitles);
			double upvotePercentage = totalVotes / ((double)upvotes);
			
			if (totalVotes == 0){
				return phrases[0];
			}
			if (totalVotes > 5) {
				if (upvotePercentage > .70) {
					return phrases[2];
				}
				if (upvotePercentage < .30) {
					return phrases[3];
				}
			}
			return phrases[1];
			
		}
	}
}
