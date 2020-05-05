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

public class PlaceAdapterMain extends RecyclerView.Adapter<PlaceAdapterMain.ViewHolder> {
    public static InteractWithTripsAdapterActivity interact;

    ArrayList<Place> places = null;
    String selectedTripId;
    Context ctx;

    public PlaceAdapterMain(ArrayList<Place> places, String selectedTripId, Context ctx) {
        this.places = places;
        this.selectedTripId = selectedTripId;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public PlaceAdapterMain.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_main, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapterMain.ViewHolder holder, final int position) {
        interact = (InteractWithTripsAdapterActivity) ctx;
        Picasso.get().load(places.get(position).icon).into(holder.imageViewIcon);
        holder.textViewText.setText(places.get(position).name);
        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interact.deletePlace(selectedTripId, places.get(position).id);
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
        ImageView imageButtonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewText = itemView.findViewById(R.id.textViewText);
            imageButtonDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }
    public interface InteractWithTripsAdapterActivity{
        void deletePlace(String selectedTripId,String placeId);
    }
}
