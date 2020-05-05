package com.example.inclass14;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    public static InteractWithAddPlacesActivity interact;
    ArrayList<Place> places;
    Context ctx;

    public PlaceAdapter(ArrayList<Place> places, Context ctx) {
        this.places = places;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.places_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, final int position) {
        interact = (InteractWithAddPlacesActivity) ctx;
        Picasso.get().load(places.get(position).icon).into(holder.imageViewIcon);
        holder.textViewText.setText(places.get(position).name);
        holder.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interact.addPlace(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon;
        TextView textViewText;
        ImageView imageButtonPlus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewText = itemView.findViewById(R.id.textViewText);
            imageButtonPlus = itemView.findViewById(R.id.imageViewAdd);
        }
    }
    public interface InteractWithAddPlacesActivity{
        void addPlace(int position);
    }
}
