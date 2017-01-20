package org.freelectron.leobel.easytrip.models;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by leobel on 1/16/17.
 */

public class RecyclerViewManager<T> {

    private final RecyclerViewListener<T> listener;
    private final RecyclerView recyclerView;
    private final View emptyView;
    private final RecyclerViewAdapter recyclerViewAdapter;
    private final SwipeRefreshLayout swipeRefreshLayout;

    private Subscription pendingRequest;
    private PaginateInfo<?> currentPaginateInfo;
    private boolean isLoading;

    public RecyclerViewManager(RecyclerViewListener<T> listener, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, View emptyView, List<T> items, PaginateInfo<?> paginateInfo){
        this.listener = listener;
        this.recyclerView = recyclerView;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.emptyView = emptyView;
        this.recyclerViewAdapter = listener.getAdapter();
        this.currentPaginateInfo = paginateInfo;

        this.recyclerView.setAdapter(recyclerViewAdapter);
        this.emptyView.setVisibility(View.INVISIBLE);
        this.swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshList();
        });
        this.emptyView.setOnClickListener(view -> {
            emptyView.setVisibility(View.GONE);
            requestItems(currentPaginateInfo, true);
        });
        recyclerViewAdapter.setListener(listener);

        if(items == null){
            requestItems(null, false);
        }
        else{
            recyclerViewAdapter.setItems(items);
        }
    }

    private void refreshList() {
        currentPaginateInfo = null;
        unSubscribe();
        requestItems(currentPaginateInfo, false);
    }

    public void requestItems(PaginateInfo<?> paginateInfo, boolean append) {
        isLoading = true;
        pendingRequest = listener.getItems(paginateInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    isLoading = false;
                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    if(response.isSuccessful()){
                        if(append){
                            recyclerViewAdapter.appendItems(response.getValue());
                        }
                        else{
                            recyclerViewAdapter.setItems(response.getValue());
                        }
                        currentPaginateInfo = response.getPaginateInfo();
                    }
                    else{
                        emptyView.setVisibility(View.VISIBLE);
                        listener.onLoadingItemsError(response.getError());
                    }
                });
    }

    public void unSubscribe(){
        if(pendingRequest != null && !pendingRequest.isUnsubscribed()){
            pendingRequest.unsubscribe();
            isLoading = false;
        }
    }

    public boolean isLoading(){
        return isLoading;
    }

    public List<T> getItems(){
        return recyclerViewAdapter.getItems();
    }

    public void setItems(List<T> items){
        recyclerViewAdapter.setItems(items);
    }

    public PaginateInfo<?> getPaginateInfo(){
        return currentPaginateInfo;
    }
}
