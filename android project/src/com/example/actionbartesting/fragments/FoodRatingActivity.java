package com.example.actionbartesting.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FoodRatingActivity extends SherlockFragmentActivity{

	
	public Object TYPE_MARKETPLACE;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		LinearLayout view = new LinearLayout(this);
		// view pager indicator
		TextView text = new TextView(this);
		text.setText("Hello world! -------- ");
		//
		ViewPager pager = new ViewPager(this);
		pager.setId(4343432);
		FoodRatingAdapter adapter = new FoodRatingAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		
		view.addView(text);
		view.addView(pager);
		
		setContentView(view);
	}
	
	private class FoodRatingAdapter extends FragmentPagerAdapter {

		public FoodRatingAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			switch (pos) {
			case 0:
				return SubRatingFragment.newInstance(SubRatingFragment.TYPE_MARKETPLACE);
			default:
				return SubRatingFragment.newInstance(SubRatingFragment.TYPE_SMOKEHOUSE);
			}
		}

		@Override
		public int getCount() {
			return SubRatingFragment.NUM_ITEMS;
		}
		
	}
	
}
