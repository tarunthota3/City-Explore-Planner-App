package com.example.inclass14;

public class City {
    String description;
    String place_id;

    public City(String description, String place_id) {
        this.description = description;
        this.place_id = place_id;
    }

    @Override
    public String toString() {
        return "City{" +
                "description='" + description + '\'' +
                ", place_id='" + place_id + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
