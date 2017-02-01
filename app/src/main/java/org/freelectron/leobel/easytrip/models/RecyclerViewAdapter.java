package org.freelectron.leobel.easytrip.models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leobel on 1/16/17.
 */

public class RecyclerViewAdapter<T, V extends RecyclerViewHolder<T> > extends RecyclerView.Adapter<V> {

    private final int viewHolderId;
    private final ViewFactory<V> viewFactory;
    private List<T> items;
    private RecyclerViewListener<T> listener;

    public RecyclerViewAdapter(int viewHolderId, ViewFactory<V> viewFactory){
        this.viewHolderId = viewHolderId;
        this.viewFactory = viewFactory;
        items = new ArrayList<>();
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewHolderId, parent, false);

        return viewFactory.create(view);
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        holder.updateFrom(items.get(position));
        holder.itemView.setOnClickListener(view -> {
            if(listener != null){
                listener.itemClick(items.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<T> items){
        this.items.clear();
        appendItems(items);
    }

    public void appendItems(List<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public List<T> getItems(){
        return items;
    }

    public void setListener(RecyclerViewListener<T> listener){
        this.listener = listener;
    }
}
