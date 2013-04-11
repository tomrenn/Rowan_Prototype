package com.example.actionbartesting.fragments;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.actionbartesting.R;
import com.example.actionbartesting.util.JsonQueryManager;

public class FoodCommentFragment extends SherlockFragment implements JsonQueryManager.Callback{
	String foodEntryId;
	String userId;
	public static final String FOOD_COMMENT_ADDR = "http://therowanuniversity.appspot.com/food/comment";
	private static final String COMMENT_PARAM = "comment";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		foodEntryId = args.getString(FoodRatingFragment.FOOD_ENTRY_ID);
		userId = args.getString(FoodRatingFragment.USER_ID);
		
	}
	
	/**
	 * Setup the view and set the button action when clicked
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.food_comment_layout, container, false);
		Button submit = (Button)view.findViewById(R.id.commentButton);
		final EditText commentField = (EditText)view.findViewById(R.id.commentField);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// hide keyboard
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
					      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(commentField.getWindowToken(), 0);
				
				JsonQueryManager jsonManager = JsonQueryManager.getInstance(getActivity());
				Map<String, String> params = new HashMap<String, String>();
				String comment = commentField.getText().toString();
				comment = comment.replaceAll("<.*>", "");
				try {
					comment = java.net.URLEncoder.encode(comment, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				params.put(FoodRatingFragment.FOOD_ENTRY_ID, foodEntryId);
				params.put(FoodRatingFragment.USER_ID, userId);
				params.put(COMMENT_PARAM, comment);
				jsonManager.requestJson(FOOD_COMMENT_ADDR, params, FoodCommentFragment.this);
			}
		});
		
		return view;
	}
	
	// When we receving the message back from the web service
	@Override
	public void receiveJson(JSONObject json, String origin) {
		// TODO: edit web api to send back json to say if comment was successful 
		Toast.makeText(getActivity(), "Comment successfully posted", Toast.LENGTH_SHORT).show();
		getActivity().onBackPressed();
	}
	
	
}
