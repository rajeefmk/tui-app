package us.jaaga.theuglyindianapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity{
	
	private GoogleMap mMap;
	private SupportMapFragment mMapFragment;
	private Marker customMarker;
	private LatLng markerLatLng;
	Button mMyEvent, mVolunteerhour, mNewEvent, mPastEvent;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mMyEvent = (Button) findViewById(R.id.myevent);
		mMyEvent.setText(Html.fromHtml("<font color='red'>6</font><br/><small>My</small>"));
		
		mVolunteerhour = (Button) findViewById(R.id.volunteerhours);
		mVolunteerhour.setText(Html.fromHtml("<font color='red'>11</font><br/><small>Hours</small>"));
		
		mNewEvent = (Button) findViewById(R.id.newevent);
		mNewEvent.setText(Html.fromHtml("<font color='red'>14</font><br/><small>New</small>"));
		
		mPastEvent = (Button) findViewById(R.id.pastevent);
		mPastEvent.setText(Html.fromHtml("<font color='red'>120</font><br/><small>Past</small>"));
		
		
		
		markerLatLng = new LatLng(12.830268,77.485662);
		setUpMapIfNeeded();
		
	}

	
	private void setUpMapIfNeeded() {
		//Null Check to confirm map not already instantiated
		if(mMap == null) {
			
			//Obtaining map from SupportMapFragment
			mMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
			ViewGroup.LayoutParams params = mMapFragment.getView().getLayoutParams();
			params.height = 200;
			mMapFragment.getView().setLayoutParams(params);
			mMap = mMapFragment.getMap();
			
			//Check if map loaded is success
			if (mMap != null){
				setUpMap();
			}
			
		}
		
	}


	private void setUpMap() {
		
		View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
		TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
		numTxt.setText("27");
		
		customMarker = mMap.addMarker(new MarkerOptions()
										.position(markerLatLng)
										.title("Title")
										.snippet("Description")
										.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker)))
				
				);
		
		final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
		if(mapView.getViewTreeObserver().isAlive()) {
			
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				
				@Override
				public void onGlobalLayout() {
					
					LatLngBounds bounds = new LatLngBounds.Builder()
														  .include(markerLatLng).build();
					
					if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
						
						mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}else {
						
						mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}
					
					mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
					
				}
			});
		}
		
	}


	private Bitmap createDrawableFromView(Context context, View marker) {
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		marker.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		marker.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmap);
		marker.draw(canvas);
		return bitmap;
	}

}
