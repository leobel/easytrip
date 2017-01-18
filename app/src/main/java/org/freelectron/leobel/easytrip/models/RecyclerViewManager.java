package org.freelectron.leobel.easytrip.models;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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

    public RecyclerViewManager(RecyclerViewListener<T> listener, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, View emptyView){
        this.listener = listener;
        this.recyclerView = recyclerView;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.emptyView = emptyView;
        this.recyclerViewAdapter = listener.getAdapter();

        this.recyclerView.setAdapter(recyclerViewAdapter);
        this.emptyView.setVisibility(View.INVISIBLE);
        this.swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshList();
        });
        this.emptyView.setOnClickListener(view -> {
            emptyView.setVisibility(View.GONE);
            refreshList();
        });
        recyclerViewAdapter.setListener(listener);
        requestItems(currentPaginateInfo);
    }

    private void refreshList() {
        currentPaginateInfo = null;
        detachRecyclerView();
        requestItems(currentPaginateInfo);
    }

    private void requestItems(PaginateInfo<?> paginateInfo) {
        pendingRequest = listener.getItems(paginateInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    if(response.isSuccessful()){
                        if(paginateInfo == null){
                            recyclerViewAdapter.setItems(response.getValue());
                        }
                        else{
                            recyclerViewAdapter.appendItems(response.getValue());
                        }
                        if(response.hasMoreItems()){
                            currentPaginateInfo = response.getPaginateInfo();
                        }

                    }
                    else{
                        emptyView.setVisibility(View.VISIBLE);
                        listener.onLoadingItemsError(response.getError());
                    }
                });
    }

    public void detachRecyclerView(){
        if(pendingRequest != null && !pendingRequest.isUnsubscribed()){
            pendingRequest.unsubscribe();
        }
    }
}
