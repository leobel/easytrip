package org.freelectron.leobel.easytrip.models;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import org.freelectron.leobel.easytrip.Utils;

import java.io.Serializable;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by leobel on 1/16/17.
 */

public class RecyclerViewManager<T> {

    protected RecyclerViewListener<T> listener;
    protected RecyclerView recyclerView;
    protected View emptyView;
    protected RecyclerViewAdapter recyclerViewAdapter;
    protected SwipeRefreshLayout swipeRefreshLayout;

    protected Subscription pendingRequest;
    protected PaginateInfo<?> currentPaginateInfo;
    protected boolean isLoading;

    public RecyclerViewManager(RecyclerViewListener<T> listener, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, View emptyView, List<T> items, PaginateInfo<?> paginateInfo){
        this.listener = listener;
        this.recyclerView = recyclerView;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.emptyView = emptyView;
        this.recyclerViewAdapter = listener.getAdapter();
        this.currentPaginateInfo = paginateInfo;

        this.recyclerView.setAdapter(recyclerViewAdapter);
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(currentPaginateInfo != null){
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    int totalItems = recyclerViewAdapter.getItemCount();
                    int visibleItems = layoutManager.getChildCount();
                    int firstVisiblePosition;
                    if(layoutManager instanceof StaggeredGridLayoutManager){
                        firstVisiblePosition = Utils.min(((StaggeredGridLayoutManager)layoutManager).findFirstVisibleItemPositions(null), 0);
                    }
                    else{
                        firstVisiblePosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    }
                    if(dy > 0 &&!isLoading && (visibleItems + firstVisiblePosition) >= totalItems){
                        requestItems(currentPaginateInfo, true);
                    }
                }

            }

        });
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

    public RecyclerViewListener<T> getListener(){
        return listener;
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
