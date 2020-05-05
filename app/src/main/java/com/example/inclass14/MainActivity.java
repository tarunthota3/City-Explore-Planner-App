/*
Assignment: InClass14
File name: MainActivity.java
Full name:
Akhil Madhamshetty-801165622
Tarun thota-801164383
 */

package com.example.inclass14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PlaceAdapterMain.InteractWithTripsAdapterActivity,TripAdapter.InteractWithMainActivity {

    public static final String TAG = "demo";
    RecyclerView recyclerView;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layoutManager;
    private ImageView buttonPlus;
    private FirebaseFirestore db;
    ArrayList<Trip> trips = new ArrayList<>();
    static String TRIPKEY = "tripkey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Trips");
        buttonPlus = findViewById(R.id.imageViewAdd);
        recyclerView = findViewById(R.id.recyclerViewTrips);
        rv_layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rv_layoutManager);
        db = FirebaseFirestore.getInstance();

        db.collection("cities")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Log.d(TAG, "onSuccess: " + documentSnapshot);
                            final String description = documentSnapshot.getString("description");
                            final Double latitude = documentSnapshot.getDouble("latitude");
                            final Double longitude = documentSnapshot.getDouble("longitude");
                            final String place_id = documentSnapshot.getString("place_id");
                            final String tripName = documentSnapshot.getString("trip");
                            final ArrayList<Place> places = new ArrayList<>();


                            db.collection("cities")
                                    .document(place_id)
                                    .collection("places")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                                String id = documentSnapshot.getString("id");
                                                String name = documentSnapshot.getString("name");
                                                String icon = documentSnapshot.getString("icon");
                                                Double latitude = documentSnapshot.getDouble("latitude");
                                                Double longitude = documentSnapshot.getDouble("longitude");
                                                Place place = new Place(id, name, icon, latitude, longitude);
                                                places.add(place);
                                            }
                                            Trip trip = new Trip(description, latitude,longitude,place_id,tripName,places);
                                            Log.d(TAG, "onSuccess: " + trip.toString());
                                            trips.add(trip);
                                            rv_adapter = new TripAdapter(trips, MainActivity.this);
                                            recyclerView.setAdapter(rv_adapter);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "onFailure: places data");
                                        }
                                    });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });





        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTrip.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void deletePlace(final String selectedTripId, final String placeId) {

        Log.d(TAG, "deletePlace tripId: " + selectedTripId + " placeId: " + placeId);


        db.collection("cities").document(selectedTripId)
                .collection("places")
                .document(placeId)
                .delete()
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        int tripPosition = -1, placePosition = -1;
                        for(int i = 0; i < trips.size(); i++){
                            Trip trip = trips.get(i);
                            if(trip.place_id.equals(selectedTripId)){
                                tripPosition = i;
                                for(int j = 0; j < trip.places.size(); j++){
                                    Place place = trip.places.get(j);
                                    if(place.id.equals(placeId)){
                                        placePosition = j;

                                        trips.get(tripPosition).places.remove(placePosition);
                                        rv_adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                });

    }

    @Override
    public void goToMap(Trip trip) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra(TRIPKEY, trip);
        startActivity(intent);
    }
}
