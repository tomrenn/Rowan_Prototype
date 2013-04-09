package com.example.carousel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;

/**
 * 
 * @author Tom Renn
 *
 */
public class CarouselFetch extends AsyncTask<Void, Void, CarouselFeature[]> {
	private final CarouselListener RECEIVER;
	private final Context context;
	private SharedPreferences prefs;
	private String prefsName = "Carousel_data";
	private static final String LAST_UPDATE = "last updated date";
	private static final String LAST_DATA = "data from last update";
	private static final String JSON_ELEMENTS = "num of features saved";
	private static final int UPDATE_INTERVAL = 3; // num hours to update 
	private AQuery aq;
	
	public CarouselFetch(CarouselListener receiver, Context context) {
		super();
		RECEIVER = receiver;
		aq = new AQuery(context);
		this.context = context;
		prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
	}

	@Override
	protected CarouselFeature[] doInBackground(Void... params) {
		String rowanURL = "http://rowan.edu";
		ArrayList<CarouselFeature> cfeatures = new ArrayList<CarouselFeature>();
		
		long lastUpdated = prefs.getLong(LAST_UPDATE, -1);
		if (lastUpdated > 0) {
			long timeDiff = Calendar.getInstance().getTimeInMillis() - lastUpdated;
			int hours = (int) (timeDiff / (60 * 60 * 1000));
			if (hours < UPDATE_INTERVAL) { // just load saved features
				cfeatures.addAll(loadFeaturesFromPreferences());
//				System.out.println("Loaded features from prefernces");
				return cfeatures.toArray(new CarouselFeature[cfeatures.size()]);
			}
		}
		// ELSE: Attempt to update
		// but check if we have available connection
		
		try { // Download + Parse Rowan's homepage for features
			//Toast.makeText(context, "Updating CarouselView", Toast.LENGTH_SHORT).show(); DOUH CAN"T DO THIS
			Document document = Jsoup.connect(rowanURL).get();
			Elements features = document.select(".feature ");
			
			for (Element feature : features) {
				String title = feature.select(".title a span").first().text();
				String description = feature.select(".description a").first().text();
				Element link = feature.select("a").first();
				String linkURL = link.attr("abs:href");
				String imageURL = link.select("img").first().attr("abs:src");
				
				CarouselFeature cFeature = new CarouselFeature(title, description, linkURL, imageURL, RECEIVER, context);
				cfeatures.add(cFeature);
			}
			saveDataToPreferences(cfeatures);
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
		return cfeatures.toArray(new CarouselFeature[cfeatures.size()]);
	}
	
	/**
	 * Read saved JSON representation of features from preferences
	 * @return CarouselFeature[] of CarouselFeatures found in preferences
	 */
	private List<CarouselFeature> loadFeaturesFromPreferences() {
		ArrayList<CarouselFeature> features = new ArrayList<CarouselFeature>();
		
		try {
			JSONObject jsonFeatures = new JSONObject(prefs.getString(LAST_DATA, ""));
			int numFeatures = jsonFeatures.getInt(JSON_ELEMENTS);
			for (int i=0; i<numFeatures; i++){
				JSONObject feature = jsonFeatures.getJSONObject(String.valueOf(i));
				features.add(new CarouselFeature(feature, RECEIVER, context));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return features;
	}
	
	/**
	 * saves the current data and time to preferences
	 * @param features CarouselFeatures being saved
	 */
	private void saveDataToPreferences(List<CarouselFeature> features) {
		Editor edit = prefs.edit();
		edit.putLong(LAST_UPDATE, Calendar.getInstance().getTimeInMillis());
		// put in data
		JSONObject jsonFeatures = new JSONObject();
		for (int i=0; i<features.size(); i++) {
			try {
				if (i==0) {
					jsonFeatures.put(JSON_ELEMENTS, features.size());
				}
				jsonFeatures.put(String.valueOf(i), features.get(i).convertToJSON());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		 
		String jsonData = jsonFeatures.toString();
		edit.putString(LAST_DATA, jsonData);
		edit.commit();
	}
	
	protected void onPostExecute(CarouselFeature[] features) {
		RECEIVER.receiveFeatures(features);
	}

	
	/** TODO: delete this, it's not used
	 * Download image
	 */
	static Bitmap downloadBitmap(String url) {
	    final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	    final HttpGet getRequest = new HttpGet(url);

	    try {
	        HttpResponse response = client.execute(getRequest);
	        final int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != HttpStatus.SC_OK) { 
	            Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url); 
	            return null;
	        }
	        
	        final HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            InputStream inputStream = null;
	            try {
	                inputStream = entity.getContent(); 
	                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	                return bitmap;
	            } finally {
	                if (inputStream != null) {
	                    inputStream.close();  
	                }
	                entity.consumeContent();
	            }
	        }
	    } catch (Exception e) {
	        // Could provide a more explicit error message for IOException or IllegalStateException
	        getRequest.abort();
	        Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
	    } finally {
	        if (client != null) {
	            client.close();
	        }
	    }
	    return null;
	}
}
