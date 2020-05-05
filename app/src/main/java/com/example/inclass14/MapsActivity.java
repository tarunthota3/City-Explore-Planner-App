package com.example.inclass14;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String TAG = "demo";
    Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent() != null && getIntent().getExtras() != null) {
            trip = (Trip) getIntent().getExtras().getSerializable(MainActivity.TRIPKEY);
            Log.d(TAG, "onCreate: " + trip.toString());

        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        // marker to city
        LatLng city = new LatLng(trip.latitude, trip.longitude);
        latLngBuilder.include(city);
        mMap.addMarker(new MarkerOptions().position(city).title(trip.description));

        // marker to places
        for(Place p: trip.places){
            LatLng locationPlace = new LatLng(p.latitude,p.longitude);
            latLngBuilder.include(locationPlace);
            mMap.addMarker(new MarkerOptions().position(locationPlace).title(p.name));
        }

        final LatLngBounds latLngBounds = latLngBuilder.build();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
            }
        });


    }
}
