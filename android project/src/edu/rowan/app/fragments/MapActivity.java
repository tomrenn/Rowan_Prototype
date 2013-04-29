/**
 * Copyright 2013 Tim Costagliola
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
package edu.rowan.app.fragments;

import java.util.LinkedList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import rowan.application.quickaccess.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;

public class MapActivity extends FragmentActivity {
	private GoogleMap mMap;
	private CheckBox eduCB, studCB, recCB;
	private List<Marker> eduBuildings, studentBuildings, recCenters;
	private static final LatLng ROWAN = new LatLng(39.708771,-75.118783);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		setUpMapIfNeeded();
		setUpInfoButtons();
		setUpBuildings();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpBuildings(){
		eduBuildings = new LinkedList<Marker>();
		setEduBuildings();
		studentBuildings = new LinkedList<Marker>();
		setStudBuildings();
		recCenters = new LinkedList<Marker>();
		setRecCenters();
	}

	private void setUpInfoButtons(){
		eduCB = (CheckBox) findViewById(R.id.eduCB);
		eduCB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(eduCB.isChecked()){
					enableMarkers(eduBuildings);
				}
				else{
					disableMarkers(eduBuildings);
				}
			}
		});

		studCB = (CheckBox) findViewById(R.id.studCB);
		studCB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(studCB.isChecked()){
					enableMarkers(studentBuildings);
				}
				else{
					disableMarkers(studentBuildings);
				}

			}
		});

		recCB = (CheckBox) findViewById(R.id.recCB);
		recCB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(recCB.isChecked()){
					enableMarkers(recCenters);
				}
				else{
					disableMarkers(recCenters);
				}

			}
		});
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			//Create a GoogleMap
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

		}
		if (mMap != null) {
			setMapOptions();
		}

	}

	private void setMapOptions(){
		// Initialize map options
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //Map Type
		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(false); //disable zoom buttons

		//Set mMap over Rowan University
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(ROWAN)      		// Sets the center of the map to Rowan
		.zoom(15.3f)                // Sets the zoom
		.bearing(315)               // Sets the orientation of the camera to NW
		.tilt(0)                   	// Sets the tilt of the camera to 0 degrees
		.build();                   // Creates a CameraPosition from the builder
		mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); //Move camera to target location 
	}

	private void setEduBuildings(){
		Marker mark;

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.706072,-75.120483))
		.visible(false)
		.title("Bole Hall"));
		eduBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.707826,-75.12154))
		.visible(false)
		.title("Bozorth Hall"));
		eduBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.709939,-75.121593))
		.visible(false)
		.title("Westby Hall"));
		eduBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.709729,-75.12066))
		.visible(false)
		.title("Science Hall"));
		eduBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.710542,-75.120285))
		.visible(false)
		.title("Robinson Hall"));
		eduBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.71166,-75.119555))
		.visible(false)
		.title("James Hall"));
		eduBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.71145,-75.121357))
		.visible(false)
		.title("Wilson Hall"));
		eduBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.712147,-75.122253))
		.visible(false)
		.title("Rowan Hall"));
		eduBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.707013,-75.120746))
		.visible(false)
		.title("Bunce Hall"));
		eduBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.709357,-75.119689))
		.visible(false)
		.title("Savitz Hall"));
		eduBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.709081,-75.119061))
		.visible(false)
		.title("Campbell Library"));
		eduBuildings.add(mark);

	}

	private void setStudBuildings(){
		Marker mark;

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.709572,-75.113955))
		.visible(false)
		.title("Chestnut Hall"));
		studentBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.70705,-75.116717))
		.visible(false)
		.title("Evergreen Hall"));
		studentBuildings.add(mark);
		
		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.706815,-75.1158))
		.visible(false)
		.title("Mullica Hall"));
		studentBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.709782,-75.117554))
		.visible(false)
		.title("Mimosa Hall"));
		studentBuildings.add(mark);
		
		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.707063,-75.118981))
		.visible(false)
		.title("Oak & Laurel Hall"));
		studentBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.709745,-75.11602))
		.visible(false)
		.title("Willow Hall"));
		studentBuildings.add(mark);
		
		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.709626,-75.115049))
		.visible(false)
		.title("Magnolia Hall"));
		studentBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.710674,-75.115478))
		.visible(false)
		.title("Edgewood Park Apartments"));
		studentBuildings.add(mark);
		
		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.705961,-75.111873))
		.visible(false)
		.title("Rowan Boulevard Apartments"));
		studentBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.711553,-75.125853))
		.visible(false)
		.title("Triad Apartments"));
		studentBuildings.add(mark);
		
		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.709114,-75.122827))
		.visible(false)
		.title("Townhouses"));
		studentBuildings.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.70653,-75.11447))
		.visible(false)
		.title("Whitney Center Apartments"));
		studentBuildings.add(mark);
	}

	private void setRecCenters(){
		Marker mark;

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.707277,-75.11587))
		.visible(false)
		.title("Basketball Court"));
		recCenters.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.709386,-75.116937))
		.visible(false)
		.title("Basketball Court"));
		recCenters.add(mark);
		
		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.71034,-75.116749))
		.visible(false)
		.title("Baseball Field"));
		recCenters.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.707302,-75.122344))
		.visible(false)
		.title("Baseball Field"));
		recCenters.add(mark);
		
		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.712485,-75.120628))
		.visible(false)
		.title("Soccer Field"));
		recCenters.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.71303,-75.119094))
		.visible(false)
		.title("Soccer Field"));
		recCenters.add(mark);
		
		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.714033,-75.120462))
		.visible(false)
		.title("Richard Wackar Stadium"));
		recCenters.add(mark);

		mark = mMap.addMarker(new MarkerOptions()
		.position(new LatLng(39.710488,-75.118557))
		.visible(false)
		.title("Rec Center"));
		recCenters.add(mark);
	}

	private void enableMarkers(List<Marker> list){
		for(int i=0; i<list.size(); i++){
			list.get(i).setVisible(true);
		}
	}

	private void disableMarkers(List<Marker> list){
		for(int i=0; i<list.size(); i++){
			list.get(i).setVisible(false);
		}
	}
}
