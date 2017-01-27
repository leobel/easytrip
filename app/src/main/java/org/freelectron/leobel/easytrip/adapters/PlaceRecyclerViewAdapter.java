package org.freelectron.leobel.easytrip.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.freelectron.leobel.easytrip.R;
import org.freelectron.leobel.easytrip.models.Place;
import org.freelectron.leobel.easytrip.models.RecyclerViewAdapter;
import org.freelectron.leobel.easytrip.models.ViewFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leobel on 1/6/17.
 */

public class PlaceRecyclerViewAdapter extends RecyclerViewAdapter<Place, PlaceViewHolder> {

    public PlaceRecyclerViewAdapter(int viewHolderId) {
        super(viewHolderId, view -> new PlaceViewHolder(view));
    }


//    @Override
//    public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.fragment_place, parent, false);
//        return new PlaceHolder(view);
//    }


}