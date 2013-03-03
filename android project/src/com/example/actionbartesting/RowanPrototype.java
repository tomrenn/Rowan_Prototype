package com.example.actionbartesting;

import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.carousel.CarouselView;

public class RowanPrototype extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// think this might be taken from Google IO app from 2012
		//This is a workaround for http://b.android.com/15340 from http://stackoverflow.com/a/5852198/132047
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.rowan_actionbar_background_pattern_tile);
            bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            getSupportActionBar().setBackgroundDrawable(bg);
        }
        
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainLayout);
        layout.setPadding(0, 20, 0, 0);
        CarouselView rowanFeatures = new CarouselView(getBaseContext());
        rowanFeatures.setId(2342343);
        layout.addView(rowanFeatures);
        
        GridView grid = new GridView(getBaseContext());
        grid.setNumColumns(3);
        grid.setVerticalSpacing(20);
        grid.setAdapter(new RowanAdapter());
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        gridParams.addRule(RelativeLayout.BELOW, rowanFeatures.getId());
        gridParams.topMargin = 20;
        layout.addView(grid, gridParams);
	}

	
	private class RowanAdapter extends BaseAdapter {
		private String[] items = new String[] {"current students", "food ratings", "campus map", "clubs", "information", "shuttle info"};
		
		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return items[position];
		}

		// no use for our case
		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null){
				view = View.inflate(getApplicationContext(), R.layout.button_layout, null);
				TextView text = (TextView) view.findViewById(R.id.description);
				text.setText(items[position]);
//				view = new TextView(RowanPrototype.this.getBaseContext());
//				view.setText(items[position]);
			}
			else {
				TextView text = (TextView) view.findViewById(R.id.description);
				text.setText(items[position]);
//				view.setText(items[position]);
			}
			
			return view;
		}
		
	}

}
