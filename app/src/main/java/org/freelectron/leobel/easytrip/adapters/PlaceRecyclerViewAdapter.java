package org.freelectron.leobel.easytrip.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.freelectron.leobel.easytrip.R;
import org.freelectron.leobel.easytrip.models.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leobel on 1/6/17.
 */

public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceRecyclerViewAdapter.PlaceHolder> {


    private List<Place> places;
    private final RecyclerViewPlaceAdapterListener listener;

    public PlaceRecyclerViewAdapter(RecyclerViewPlaceAdapterListener listener){
        this(new ArrayList<>(), listener);
    }

    public PlaceRecyclerViewAdapter(List<Place> places, RecyclerViewPlaceAdapterListener listener){
        this.places = places;
        this.listener = listener;
    }

    @Override
    public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_place, parent, false);
        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceHolder holder, int position) {
        holder.updateFrom(places.get(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    public class PlaceHolder extends RecyclerView.ViewHolder {


        public View view;
        ImageView iconImageView;
        TextView nameTextView;
        TextView locationTextView;

        public PlaceHolder(View itemView) {
            super(itemView);
            view = itemView;
            iconImageView = (ImageView) itemView.findViewById(R.id.place_icon);
            nameTextView = (TextView) itemView.findViewById(R.id.place_name);
            locationTextView = (TextView) itemView.findViewById(R.id.place_location);

        }

        public void updateFrom(Place place) {
            iconImageView.setImageResource(place.isCity() ? R.drawable.ic_place :  R.drawable.ic_airport);
            nameTextView.setText(place.getPlaceName());
            locationTextView.setText(place.getCountryName());
        }
    }
}
