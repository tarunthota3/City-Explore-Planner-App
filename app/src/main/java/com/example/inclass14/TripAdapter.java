package com.example.inclass14;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {
    public static InteractWithMainActivity interact;
    ArrayList<Trip> trips;
    Context ctx;
    String selectedTripId;
    public TripAdapter(ArrayList<Trip> trips, Context ctx) {
        this.trips = trips;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.ViewHolder holder, final int position) {

        selectedTripId = trips.get(position).place_id;
        interact = (InteractWithMainActivity) ctx;
        holder.textViewTrip.setText(trips.get(position).trip_name);
        holder.textViewDescription.setText(trips.get(position).description);

        holder.rv_adapter = new PlaceAdapterMain(trips.get(position).places, selectedTripId, ctx);
        holder.recyclerViewPlace.setAdapter(holder.rv_adapter);

        holder.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, AddPlaces.class);
                intent.putExtra("place_id", trips.get(position).place_id);
                String geoLocation = trips.get(position).latitude + "," + trips.get(position).longitude;
                intent.putExtra("geolocation", geoLocation);
                ctx.startActivity(intent);
            }
        });
        holder.imageButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    interact.goToMap(trips.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return trips.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTrip, textViewDescription;
        ImageView imageButtonMap, imageButtonPlus;
        RecyclerView recyclerViewPlace;
        RecyclerView.Adapter rv_adapter;
        RecyclerView.LayoutManager rv_layoutManager;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTrip = itemView.findViewById(R.id.textViewTrip);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageButtonMap = itemView.findViewById(R.id.imageViewMap);
            imageButtonPlus = itemView.findViewById(R.id.imageViewAdd);
            recyclerViewPlace = itemView.findViewById(R.id.recyclerViewPlace);
            rv_layoutManager = new LinearLayoutManager(ctx);
            recyclerViewPlace.setLayoutManager(rv_layoutManager);
        }
    }
    public interface InteractWithMainActivity{
        void goToMap(Trip trip);
    }
}
