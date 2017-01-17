package org.freelectron.leobel.easytrip.models;

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

    private Subscription pendingRequest;
    private PaginateInfo<?> currentPaginateInfo;
    int currentPage;

    public RecyclerViewManager(RecyclerViewListener<T> listener, RecyclerView recyclerView, View emptyView){
        this.listener = listener;
        this.recyclerView = recyclerView;
        this.emptyView = emptyView;
        this.recyclerViewAdapter = listener.getAdapter();

        this.recyclerView.setAdapter(recyclerViewAdapter);
        this.emptyView.setVisibility(View.INVISIBLE);

        requestItems(currentPaginateInfo);
    }

    private void requestItems(PaginateInfo<?> paginateInfo) {
        pendingRequest = listener.getItems(paginateInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
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
                        listener.onLoadingItemsError(response.getError());
                    }
                });
    }

    public void detachRecyclerView(){
        if(!pendingRequest.isUnsubscribed()){
            pendingRequest.unsubscribe();
        }
    }
}
