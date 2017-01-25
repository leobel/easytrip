package org.freelectron.leobel.easytrip;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.freelectron.leobel.easytrip.adapters.PlaceRecyclerViewAdapter;
import org.freelectron.leobel.easytrip.models.PageResponse;
import org.freelectron.leobel.easytrip.models.PaginateInfo;
import org.freelectron.leobel.easytrip.models.Place;
import org.freelectron.leobel.easytrip.models.RecyclerViewAdapter;
import org.freelectron.leobel.easytrip.models.RecyclerViewListener;
import org.freelectron.leobel.easytrip.models.SearchViewManager;
import org.freelectron.leobel.easytrip.services.LocalisationService;
import org.freelectron.leobel.easytrip.services.PinterestService;
import org.freelectron.leobel.easytrip.widgets.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchPlaceActivity extends AppCompatActivity implements RecyclerViewListener<Place> {

    public static final String PLACE = "PLACE";
    public static final String QUERY = "QUERY";
    public static final String ITEMS_LIST = "ITEM_LIST";


    SearchView searchView;
    ViewGroup searchPlate;
    RecyclerView recyclerView;

    @Inject
    public LocalisationService localisationService;

    @Inject
    public PinterestService pinterestService;


    private SwipeRefreshLayout swipeRefreshLayout;
    private View emptyView;
    private SearchViewManager recyclerViewManager;
    String query;
    List<Place> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);

        EasyTripApp.getInstance().getComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());

        recyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        emptyView = findViewById(R.id.empty_view_container);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.place_divider));

        if(savedInstanceState != null){
            items = (ArrayList<Place>)savedInstanceState.getSerializable(ITEMS_LIST);
            query = savedInstanceState.getString(QUERY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.find_location_place_holder));
        searchView.requestFocus();

        searchPlate = (ViewGroup) this.searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        addProgressIndicator();

        recyclerViewManager = new SearchViewManager<>(this, searchView, recyclerView, swipeRefreshLayout, emptyView, items, query);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerViewManager.unSubscribeManager();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(recyclerViewManager != null){
            outState.putSerializable(ITEMS_LIST, (ArrayList<Place>)recyclerViewManager.getItems());
            outState.putString(QUERY, recyclerViewManager.getQuery());
        }
    }

    @Override
    public RecyclerViewAdapter getAdapter() {
        return new PlaceRecyclerViewAdapter(R.layout.fragment_place);
    }

    @Override
    public void itemClick(Place item) {
        Intent intent = new Intent();
        intent.putExtra(PLACE, item);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public Observable<PageResponse<List<Place>>> getItems(PaginateInfo<?> paginateInfo) {
        String query = recyclerViewManager.getQuery();
        showProgressIndicator(true);
        return localisationService.findPlacesByAutoSuggest(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    showProgressIndicator(false);
                    if(response.isSuccessful()){
                        return new PageResponse<>(response.getValue(), null, response.getSource());
                    }
                    else{
                        return new PageResponse<List<Place>>(response.getError(), response.getSource());
                    }
                });
    }

    @Override
    public void onLoadingItemsError(Throwable error) {

    }

    private void addProgressIndicator() {
        if(searchPlate.findViewById(R.id.search_progress_bar) == null){
            View v = LayoutInflater.from(this).inflate(R.layout.loading_progress, null);
            searchPlate.addView(v, 1);
        }
    }

    private void showProgressIndicator(boolean show){
        View progressIndicator = searchPlate.findViewById(R.id.search_progress_bar);
        progressIndicator.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
