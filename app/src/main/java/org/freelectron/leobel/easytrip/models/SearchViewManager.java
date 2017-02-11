package org.freelectron.leobel.easytrip.models;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import org.freelectron.leobel.easytrip.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by leobel on 1/24/17.
 */

public class SearchViewManager<T> extends RecyclerViewManager<T> {

    private final SearchView searchView;
    private Subscription searchViewSubscription;
    private String query;

    public SearchViewManager(RecyclerViewListener<T> listener, SearchView searchView, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, View emptyView, List<T> items, String query) {
        super(listener, recyclerView, swipeRefreshLayout, emptyView, items, null);

        this.searchView = searchView;
        if(query != null){
            this.searchView.setQuery(query, false);
        }
        this.query = this.searchView.getQuery().toString();
        setSearchViewObservable();
    }

    private void setSearchViewObservable(){
        searchViewSubscription = RxSearchView.queryTextChanges(searchView)
                .filter(charSequence -> charSequence.length() > 2)
                .throttleLast(100, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query ->  {
                    if(this.query == null || !this.query.equals(query)){
                        this.query = query;
                        unSubscribe();
                        requestItems(null, false);
                    }
                });
    }

    @Override
    public void requestItems(PaginateInfo<?> paginateInfo, boolean append) {
        if(this.query != null && this.query.length() > 2){
            super.requestItems(paginateInfo, append);
        }
    }

    public String getQuery(){
        return searchView.getQuery().toString();
    }

    public void unSubscribeManager(){
        unSubscribe();
        if(searchViewSubscription != null && !searchViewSubscription.isUnsubscribed()){
            searchViewSubscription.unsubscribe();
        }
    }


}
