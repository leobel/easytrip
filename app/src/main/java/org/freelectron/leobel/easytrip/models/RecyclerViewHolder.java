package org.freelectron.leobel.easytrip.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by leobel on 1/16/17.
 */

public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder{

    public RecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void updateFrom(T item);

}
