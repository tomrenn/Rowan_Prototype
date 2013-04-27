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

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import rowan.application.quickaccess.R;

public class FoodCommentsAdapter extends ArrayAdapter<FoodComment>{
	
	public FoodCommentsAdapter(Context context, int textViewResourceId, List<FoodComment> comments) {
		super(context, textViewResourceId, comments);
	}
	
	public void addAllItems(FoodComment[] items){
		for (FoodComment item : items) {
			this.add(item);
		}
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.food_comment_list_item, null);
        }
        FoodComment comment = getItem(position);
        if (comment != null) {
                TextView commentView = (TextView) v.findViewById(R.id.foodCommentText);
                TextView timestampView = (TextView) v.findViewById(R.id.foodCommentTimestamp);
                if (commentView != null) {
                	commentView.setText(comment.getComment());    
                }
                if(timestampView != null){
                	timestampView.setText(comment.getReadableTimeSinceNow());
                }
        }
        return v;
	}
}
