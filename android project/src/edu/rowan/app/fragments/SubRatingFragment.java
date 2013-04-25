package edu.rowan.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class SubRatingFragment extends SherlockFragment{
	public static final String TYPE_MARKETPLACE = "marketplace";
	public static final String TYPE_SMOKEHOUSE = "smokehouse";
	private static final String FRAGMENT_TYPE = "fragment rating type";
	public static final int NUM_ITEMS = 2;
	String ratingType;
	
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
		ratingType = getArguments() != null ? getArguments().getString(FRAGMENT_TYPE) : TYPE_MARKETPLACE;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		View view = inflater.inflate(R.layout.food_rating_layout, container);
		TextView view = new TextView(getActivity());
		view.setText("Viewpager!");
		
		// TODO: fill the inflated view with information
		return view;
	}
}

