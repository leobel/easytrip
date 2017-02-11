package org.freelectron.leobel.easytrip.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.freelectron.leobel.easytrip.R;
import org.freelectron.leobel.easytrip.models.Place;
import org.freelectron.leobel.easytrip.models.RecyclerViewHolder;

/**
 * Created by leobel on 1/24/17.
 */

public class PlaceViewHolder extends RecyclerViewHolder<Place> {

    ImageView iconImageView;
    TextView nameTextView;
    TextView locationTextView;

    public PlaceViewHolder(View itemView) {
        super(itemView);

        iconImageView = (ImageView) itemView.findViewById(R.id.place_icon);
        nameTextView = (TextView) itemView.findViewById(R.id.place_name);
        locationTextView = (TextView) itemView.findViewById(R.id.place_location);
    }

    @Override
    public void updateFrom(Place place) {
        iconImageView.setImageResource(place.isCity() ? R.drawable.ic_place :  R.drawable.ic_airport);
        nameTextView.setText(place.getPlaceName());
        locationTextView.setText(place.getCountryName());
    }
}
