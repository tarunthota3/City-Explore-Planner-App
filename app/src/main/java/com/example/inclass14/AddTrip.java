package com.example.inclass14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddTrip extends AppCompatActivity implements CityAdapter.InteractWithAddTripActivity {

    private EditText editTextTripName, editTextCityName;
    private Button buttonSearch, buttonAddTrip;
    public static final String TAG="demo";
    RecyclerView recyclerView;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layoutManager;
    ArrayList<City> cities = null;
    City selectedCity = new City("","");
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setTitle("Add Trip");
        editTextTripName = findViewById(R.id.editTextTripName);
        editTextCityName = findViewById(R.id.editTextCityName);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonAddTrip = findViewById(R.id.buttonAddTrip);

        recyclerView = findViewById(R.id.recyclerViewAddTrip);
        rv_layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rv_layoutManager);
        db = FirebaseFirestore.getInstance();


        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextTripName.getText().toString().equals("")){
                    editTextTripName.setError("Trip Name can't be empty");
                }
                else if(editTextCityName.getText().toString().equals("")){
                    editTextCityName.setError("City can't be empty");
                }
                else{
//                    Search for Cities:
                    String url = null;
                    try {
                        url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?" +
                                "key=" + getResources().getString(R.string.api_key) + "&" +
                                "types=(cities)" + "&" +
                                "input=" + URLEncoder.encode(editTextCityName.getText().toString(),"UTF-8");
                        new getCities().execute(url);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        buttonAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextTripName.getText().toString().equals("")){
                 editTextTripName.setError("Trip Name can't be empty");
                }
                else if(editTextCityName.getText().toString().equals("")){
                    editTextCityName.setError("City can't be empty");
                }
                else if(selectedCity.place_id.equals("")){
                    Toast.makeText(AddTrip.this,"Please search for a city", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        String url = "https://maps.googleapis.com/maps/api/place/details/json?" +
                                "key="+getResources().getString(R.string.api_key)+"&"+
                                "placeid="+URLEncoder.encode(selectedCity.place_id,"UTF-8");
                        new getGeoCoordinates().execute(url);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    class getGeoCoordinates extends AsyncTask<String, Void, double[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected double[] doInBackground(String... strings) {
            double[] geo = {0,0};
            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    Log.d(TAG, "doInBackground: " + json);
                    JSONObject root = new JSONObject(json);
                    JSONObject result = root.getJSONObject("result");
                    JSONObject geometry = result.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    Double latitude = Double.valueOf(location.getString("lat"));
                    Double longitude = Double.valueOf(location.getString("lng"));
                    geo[0] = latitude;
                    geo[1] = longitude;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return geo;
        }

        @Override
        protected void onPostExecute(double[] d) {
            super.onPostExecute(d);
            Log.d(TAG, "onPostExecute: " + d);
            if(selectedCity.place_id.equals("") || selectedCity.description.equals("")){
                Toast.makeText(AddTrip.this, "Please select a city", Toast.LENGTH_SHORT).show();
            }
            else{
                HashMap<String, Object> hmap = new HashMap<>();
                hmap.put("trip",editTextTripName.getText().toString());
                hmap.put("description", selectedCity.description);
                hmap.put("place_id",selectedCity.place_id);
                hmap.put("latitude",d[0]);
                hmap.put("longitude",d[1]);

                db.collection("cities")
                        .document(selectedCity.place_id)
                        .set(hmap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: successfull!!" );
                                Intent intent  = new Intent(AddTrip.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "onFailure: save falied " + e.getMessage());
                            }
                        });
            }

        }
    }

    @Override
    public void getCityDetails(int position) {
        selectedCity = cities.get(position);
        editTextCityName.setText(selectedCity.description);
    }

    class getCities extends AsyncTask<String, Void, ArrayList<City>>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<City> doInBackground(String... strings) {
            cities = new ArrayList<>();

            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    Log.d(TAG, "doInBackground: " + json);
                    JSONObject root = new JSONObject(json);
                    JSONArray predictions = root.getJSONArray("predictions");
                    for(int i = 0; i< predictions.length(); i++){
                        JSONObject object =predictions.getJSONObject(i);
                        String description = object.getString("description");
                        String place_id = object.getString("place_id");
                        City city = new City(description, place_id);
                        cities.add(city);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return cities;
        }

        @Override
        protected void onPostExecute(ArrayList<City> cities) {
            super.onPostExecute(cities);
            for (City c: cities) {
                Log.d(TAG, "onPostExecute: " + c.toString());
            }
            rv_adapter = new CityAdapter(cities, AddTrip.this);
            recyclerView.setAdapter(rv_adapter);
        }
    }
}
