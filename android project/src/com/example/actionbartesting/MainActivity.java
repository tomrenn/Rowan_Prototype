package com.example.actionbartesting;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Build;
import android.os.Bundle;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;

public class MainActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//This is a workaround for http://b.android.com/15340 from http://stackoverflow.com/a/5852198/132047
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.rowan_actionbar_background_pattern_tile);
            bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            getSupportActionBar().setBackgroundDrawable(bg);
        }
	}


}
