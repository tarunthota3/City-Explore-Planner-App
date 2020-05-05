package com.example.inclass14;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    public static InteractWithAddTripActivity interact;

    ArrayList<City> cities;
    Context ctx;
    public CityAdapter(ArrayList<City> cities, Context ctx) {
        this.cities = cities;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, final int position) {
        interact = (InteractWithAddTripActivity) ctx;
        holder.textViewDescirption.setText(cities.get(position).description);
        holder.cardViewCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interact.getCityDetails(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDescirption;
        CardView cardViewCity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescirption = itemView.findViewById(R.id.textViewDescription);
            cardViewCity = itemView.findViewById(R.id.cardViewCity);
        }
    }
    public interface InteractWithAddTripActivity{
        void getCityDetails(int position);
    }
}
