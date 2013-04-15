package com.example.actionbartesting.util;

import java.util.Calendar;

public class FoodComment {
		private String comment;
		private long timestamp;
		
		public FoodComment(String comment, long timestamp) {
			this.comment = comment;
			// add miliseconds back on timestamp (API doesn't give miliseconds)
			this.timestamp = timestamp * 1000;
		}
		
		public String getComment() { return comment; }
		
		public String getReadableTimeSinceNow() {
			String str = "";
			long now = Calendar.getInstance().getTimeInMillis();
			long difference = now - timestamp;
			
//			int seconds = (int) (difference / 1000) % 60 ;
			int minutes = (int) ((difference / (1000*60)) % 60);
			int hours   = (int) ((difference / (1000*60*60)) % 24);
			if (hours == 0 && minutes == 0) {
				str = "Just now";
			}
			else if (hours == 0) {
				str = minutes + " minutes ago";
			}
			else {
				str = hours + " hours and " + minutes + " minutes ago";
			}
			return str;
		}
	}