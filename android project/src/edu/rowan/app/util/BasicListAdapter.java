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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Simple ArrayAdapter that displays text
 * @author Tom Renn
 */
public class BasicListAdapter<T> extends ArrayAdapter<T>{
	
	public BasicListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	public void addAllItems(T[] items){
		for (T item : items) {
			this.add(item);
		}
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		TextView view = (TextView)super.getView(position, convertView, parent);
		view.setTextColor(Color.WHITE);
		return view;
	}
	
	public int getPixelsFromDPI(float dpi) {
		Resources r = this.getContext().getResources();
		float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, r.getDisplayMetrics());

		return (int)pixels;
	}
	
}
