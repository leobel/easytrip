package org.freelectron.leobel.easytrip;

import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import org.freelectron.leobel.easytrip.adapters.PlaceRecyclerViewAdapter;
import org.freelectron.leobel.easytrip.adapters.RecyclerViewPlaceAdapterListener;
import org.freelectron.leobel.easytrip.models.Place;
import org.freelectron.leobel.easytrip.services.LocalisationService;
import org.freelectron.leobel.easytrip.widgets.DividerItemDecoration;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecyclerViewPlaceAdapterListener{

    SearchView searchView;
    RecyclerView recyclerView;
    PlaceRecyclerViewAdapter adapter;



    @Inject
    public LocalisationService localisationService;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EasyTripApp.getInstance().getComponent().inject(this);

        searchView = (SearchView) findViewById(R.id.search_place_search_view);
        recyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.place_divider));
        adapter = new PlaceRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscription = RxSearchView.queryTextChanges(searchView)
                .filter(charSequence -> charSequence.length() > 2)
                .throttleLast(100, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap(query ->  localisationService.findPlacesByAutoSuggest(query))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isSuccessful()){
                        adapter.setPlaces(response.getValue());
                    }
                    else Timber.d("Result observable: " + response.getError());
                });
    }

    @Override
    protected void onStop() {
        subscription.unsubscribe();
        super.onStop();
    }

    @Override
    public void onSelectPlace(Place place) {

    }
}
