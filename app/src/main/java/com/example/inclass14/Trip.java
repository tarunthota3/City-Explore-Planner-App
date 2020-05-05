package com.example.inclass14;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Trip implements Serializable {
    String description;
    Double latitude;
    Double longitude;
    String place_id;
    String trip_name;
    ArrayList<Place> places;

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getTrip() {
        return trip_name;
    }

    public void setTrip(String trip_name) {
        this.trip_name = trip_name;
    }



    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", place_id='" + place_id + '\'' +
                ", trip_name='" + trip_name + '\'' +
                ", places=" + places +
                '}';
    }

    public Trip(String description, Double latitude, Double longitude, String place_id, String trip_name, ArrayList<Place> places) {
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.place_id = place_id;
        this.trip_name = trip_name;
        this.places = places;
    }
}
