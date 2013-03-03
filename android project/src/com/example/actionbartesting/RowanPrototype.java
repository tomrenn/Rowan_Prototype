package com.example.actionbartesting;

import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;


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
        layout.addView(rowanFeatures);
	}


}
