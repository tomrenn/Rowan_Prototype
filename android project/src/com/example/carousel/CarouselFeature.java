package com.example.carousel;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.example.actionbartesting.R;


/**
 * Copyright 2013 Thomas Renn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class CarouselFeature {
	private String title;
	private String description;
	private String url;
	private String imageURL;
	private Bitmap image;
	private RelativeLayout carouselView;
	private ImageView imageView;
	private CarouselListener listener;
	private Context context;
	private AQuery aq;
	public static final String DEFAULT_FEATURE_TAG = "default image";
	private static final String TITLE = "title";
	private static final String DESC = "description";
	private static final String URL = "article url";
	private static final String IMG_URL = "image url";
	/**
	 * Create a new CarouselFeature
	 * 
	 * @param title Title of the feature
	 * @param description Description of the feature
	 * @param url Link to the article
	 */
	public CarouselFeature(String title, String description, String url, 
							String imageURL, CarouselListener listener, Context context){
		this.title = title;
		this.description = description;
		this.url = url;
		this.imageURL = imageURL;
		image = null;
		carouselView = null;
		aq = new AQuery(context);
		this.listener = listener;
		this.context = context;
	}
	
	public JSONObject convertToJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put(TITLE, title);
			json.put(DESC, description);
			json.put(URL, url);
			json.put(IMG_URL, imageURL);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public CarouselFeature(JSONObject data, CarouselListener listener, Context context) throws JSONException {
		title = data.getString(TITLE);
		description = data.getString(DESC);
		url = data.getString(URL);
		imageURL = data.getString(IMG_URL);
		image = null;
		carouselView = null;
		aq = new AQuery(context);
		this.listener = listener;
		this.context = context;
	}
	
	public CarouselFeature(Context context) {
		// default feature with simple default image
		this.context = context;
		carouselView = new RelativeLayout(context);
		imageView = new ImageView(context);
		imageView.setImageResource(R.drawable.carousel_placeholder);
		carouselView.setTag(DEFAULT_FEATURE_TAG);
		carouselView.addView(imageView);
	}
	
	// Asynchronously load image from web
	public void loadImage() {
		carouselView = new RelativeLayout(context);
		imageView = new ImageView(context);
		Bitmap cachedImage = aq.getCachedImage(imageURL);
		if (cachedImage != null) {
			System.out.println("Image was cached!");
			imageView.setImageBitmap(cachedImage);
			carouselView.addView(imageView);
			setupView();
			listener.doneLoading(this);
		}
		else {
			// Aquery download and cache image
			aq.id(imageView).image(imageURL, true, true, 0, 0, new BitmapAjaxCallback() {
				@Override 
				// set imageview to loaded bitmap and tell listener we're done loading
		        public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status){            
		                iv.setImageBitmap(bm);    
		                carouselView.addView(iv);
		                setupView();
		                listener.doneLoading(CarouselFeature.this);
		        }
			});
		}
	}
	
	public void measureHeight(String str){
		System.out.println(str+ " height = " + imageView.getHeight());
	}
	
	private void setupView() {
		imageView.setId(1);
		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		imageView.setLayoutParams(imageParams);
//		imageView.setAdjustViewBounds(true);
		Resources r = context.getResources();
		int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
		
		TextView descriptionView = new TextView(context);
		descriptionView.setId(2);
		descriptionView.setPadding(padding, 0, padding, padding);
		descriptionView.setText(description);
		descriptionView.setBackgroundColor(Color.argb(220, 63, 26, 10));
		descriptionView.setTextColor(Color.WHITE);
		RelativeLayout.LayoutParams descParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		descParams.addRule(RelativeLayout.ALIGN_BOTTOM, imageView.getId());
		carouselView.addView(descriptionView, descParams);
		
		TextView titleView = new TextView(context);
		titleView.setText(title);
		titleView.setPadding(padding, 0, padding, 0);
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleParams.addRule(RelativeLayout.ABOVE, descriptionView.getId());
		titleView.setLayoutParams(titleParams);
		titleView.setTextColor(Color.WHITE);
		titleView.setShadowLayer(2, 3, 3, Color.argb(230, 0, 0, 0));
		titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
		
		carouselView.addView(titleView);
	}
	
	public RelativeLayout getView(){ return carouselView; } 
	public String getTitle() { return title; }
	public String getDescription() { return description; }
	public String getURL() { return url; }
	public Bitmap getImage() { return image; }
	public String getImageURL() { return imageURL; }
	
	public String toString(){
		return title + " - " + description + " LINK: " + url + " IMG: " + imageURL;
	}
	
	/**
	 * Basically return the filename from the imageURL
	 * @return image filename 
	 */
	private String getImageKey() {
		return imageURL.replaceFirst(".*news/",  "");
	}
	
}