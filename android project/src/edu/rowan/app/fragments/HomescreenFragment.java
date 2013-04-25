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

import rowan.application.quickaccess.ActivityFacade;
import rowan.application.quickaccess.ActivityFacade.ApplicationAction;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import rowan.application.quickaccess.R;
import edu.rowan.app.carousel.CarouselView;

/**
 * This fragment is responsible for the view that represents the base/home screen.
 * 
 * @author tomrenn
 */
public class HomescreenFragment extends SherlockFragment implements OnItemClickListener{
	private ActivityFacade activity;

	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (ActivityFacade)getActivity();
	}
	
	/**
	 * Construct the view for this fragment.
	 * The core layout of this fragment is a CarouselView and grid of available buttons
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide actionbar loading indcator
		activity.showLoading(false);
		
		View view = inflater.inflate(R.layout.activity_main, container, false);
		RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.mainLayout);
        layout.setPadding(0, 20, 0, 0);
        
        CarouselView rowanFeatures = new CarouselView(inflater.getContext());
        // set view Id so that relativeLayout can position. Without an Id relativeLayout doesn't work
        rowanFeatures.setId(2342343);
        layout.addView(rowanFeatures);
        setTouchListener(rowanFeatures);

	        
        GridView grid = new GridView(inflater.getContext());
        grid.setOnItemClickListener(this);
        grid.setNumColumns(3);
        grid.setVerticalSpacing(20);
        grid.setAdapter(new RowanAdapter());
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        gridParams.addRule(RelativeLayout.BELOW, rowanFeatures.getId());
        gridParams.topMargin = 20;
        layout.addView(grid, gridParams);
        
        return view;
	}
	
	/**
	 * Determine when CarouselView is clicked and not swiped
	 * Thanks to: http://stackoverflow.com/a/13264379/121654
	 */
	public void setTouchListener(final CarouselView carouselView) {
		carouselView.setOnTouchListener(new View.OnTouchListener() {
		    float oldX = 0, newX = 0, sens = 5;

		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		    	Log.d("onTouch", "MotionEvent");
		        switch (event.getAction()) {
		        case MotionEvent.ACTION_DOWN:
		            oldX = event.getX();
		            break;

		        case MotionEvent.ACTION_UP:
		            newX = event.getX();
		            if (Math.abs(oldX - newX) < sens) {
				    	// MotionEvent positions did not surpass sensitivity threshold
		            	showFeature(carouselView.getCurrentFeatureLink());
		                return true;
		            }
		            oldX = 0;
		            newX = 0;
		            break;
		        }

		        return false;
		    }
		});
	}
	
	// tell activity to show link in new webviewFragment
	private void showFeature(String link) {
		Bundle data = new Bundle();
		data.putString(WebViewFragment.ADDRESS, link);
		activity.perform(ApplicationAction.LAUNCH_URL, data);
	}
	
	/**
	 * Handle the buttons in the GridView 
	 */
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
		String itemClicked = (String) adapter.getItemAtPosition(pos);
		
		if (itemClicked.equals(getResources().getString(R.string.currentStudents))){
			activity.perform(ApplicationAction.LAUNCH_WEBSITES, null);
		}
		else if (itemClicked.equals(getResources().getString(R.string.clubs))){
			activity.perform(ApplicationAction.LAUNCH_ORGANIZATIONS, null);
		}
		else if (itemClicked.equals(getResources().getString(R.string.foodRatings))){
			activity.perform(ApplicationAction.LAUNCH_RATINGS, null);
		}
		else 
			Toast.makeText(getActivity(), "Not yet implemented", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * This adapter produces the data for the GridView
	 * 
	 * @author tomrenn
	 */
	private class RowanAdapter extends BaseAdapter {
		private String[] items = getResources().getStringArray(R.array.homeItems);
		private final int[] icons = new int[] {
				R.drawable.current_students,
				R.drawable.current_students,
				R.drawable.clubs,
				R.drawable.information,
				R.drawable.food_rating
		};
		
		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return items[position];
		}

		// serves no purpose in our situation
		@Override
		public long getItemId(int position) {
			return 0;
		}

		// return a simple textView with an image above it
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null){
				view = View.inflate(parent.getContext(), R.layout.button_layout, null);
				TextView text = (TextView) view.findViewById(R.id.description);
				int drawable = icons[position];
				text.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0);
				text.setText(items[position]);
			}
			else {
				TextView text = (TextView) view.findViewById(R.id.description);
				text.setText(items[position]);
			}
			
			return view;
		}
	}
}
