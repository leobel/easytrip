package org.freelectron.leobel.easytrip.models;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import rx.Observable;

/**
 * Created by leobel on 1/16/17.
 */

public interface RecyclerViewListener<T> {

    RecyclerViewAdapter getAdapter();

    void itemClick(T item);

    Observable<PageResponse<List<T>>> getItems(PaginateInfo<?> paginateInfo);

    void onLoadingItemsError(Throwable error);
}
