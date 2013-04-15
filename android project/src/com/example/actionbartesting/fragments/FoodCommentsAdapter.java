package com.example.actionbartesting.fragments;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.actionbartesting.R;
import com.example.actionbartesting.util.FoodComment;

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
