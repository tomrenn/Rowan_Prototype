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
package edu.rowan.app.util;

import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class JsonQueryManager {
	private static JsonQueryManager singleton = null;
	private AQuery aq = null;
	
	public static synchronized JsonQueryManager getInstance(Activity reference) {
		if (singleton == null){
			singleton = new JsonQueryManager(reference);
		}
		return singleton;
	}
	
	// disallow use of default constructor
	private JsonQueryManager() {}
	
	private JsonQueryManager(Activity reference) {
		aq = new AQuery(reference);
	}
	
	/**
	 * Performs a request for JSON data. :SIGH: this got ugly. WebRequests are done 
	 * via GET, so this method puts the params into the address instead of using Aquery POST
	 * @param address Full http address of request
	 * @param params Additional parameters of the request
	 * @param receiver Where to deliver the json data
	 */
	public void requestJson(final String address, Map<String, String> params, final Callback receiver) {
		// stuff params into the address if there are any
		String addressModified = address;
		if (params != null) {
			Set<String> keys = params.keySet();
			addressModified += "?";
			for (String key : keys) {
				addressModified += key + "=" + params.get(key) + "&";
			}
			// remove trailing &
			addressModified = addressModified.substring(0, addressModified.length()-1);
		}

		Log.d("JsonQuery", "Running GET request");
		Log.d("JsonQuery", addressModified);
		aq.ajax(addressModified,  JSONObject.class, 
			new AjaxCallback<JSONObject>() {
				@Override
				public void callback(String url, JSONObject json, AjaxStatus status) {
					Log.d("Callback aquery", "status code " + status.getCode());
					receiver.receiveJson(json, address);
				}
		});
	}
	
	/**
	 * Interface for classes to receive requested information
	 */
	public interface Callback {
		/**
		 * 
		 * @param json Data received, null if error
		 * @param origin Address/URL of the request performed
		 */
		public void receiveJson(JSONObject json, String origin);
	}
}
