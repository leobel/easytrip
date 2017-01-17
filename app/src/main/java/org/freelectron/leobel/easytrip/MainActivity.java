package org.freelectron.leobel.easytrip;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKUser;

import org.freelectron.leobel.easytrip.adapters.PlaceRecyclerViewAdapter;
import org.freelectron.leobel.easytrip.adapters.RecyclerViewPlaceAdapterListener;
import org.freelectron.leobel.easytrip.models.Place;
import org.freelectron.leobel.easytrip.models.Response;
import org.freelectron.leobel.easytrip.services.LocalisationService;
import org.freelectron.leobel.easytrip.services.PinterestService;
import org.freelectron.leobel.easytrip.widgets.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecyclerViewPlaceAdapterListener{

    SearchView searchView;
    RecyclerView recyclerView;
    PlaceRecyclerViewAdapter adapter;

    @Inject
    public LocalisationService localisationService;

    @Inject
    public PinterestService pinterestService;


    private Subscription subscription;
    private ViewGroup searchPlate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EasyTripApp.getInstance().getComponent().inject(this);

        Fresco.initialize(getApplication());

        searchView = (SearchView) findViewById(R.id.search_place_search_view);
        recyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.place_divider));
        adapter = new PlaceRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        searchView.setIconified(false);
        searchPlate = (ViewGroup) searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        addProgressIndicator();

        Button login = (Button)findViewById(R.id.login);
        Button pins = (Button)findViewById(R.id.find_pins);

        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);

        login.setOnClickListener(view -> {
            pinterestService.login(this, scopes)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Action1<Response<PDKUser>>() {
                        @Override
                        public void call(Response<PDKUser> pdkUserResponse) {
                            Timber.d("User Login successful :" + pdkUserResponse.getValue().getFirstName());
                        }
                    });
        });

        pins.setOnClickListener(view -> {
            startActivity(new Intent(this, InspireMeActivity.class));
        });

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

    @Override
    protected void onResume() {
        super.onResume();
        subscription = RxSearchView.queryTextChanges(searchView)
                .filter(charSequence -> charSequence.length() > 2)
                .throttleLast(100, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(query -> {
                    showProgressIndicator(true);
                    return query;
                })
                .observeOn(Schedulers.io())
                .flatMap(query ->  localisationService.findPlacesByAutoSuggest(query))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    showProgressIndicator(false);
                    if(response.isSuccessful()){
                        adapter.setPlaces(response.getValue());
                    }
                    else Timber.d("Result observable: " + response.getError());
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PDKClient.getInstance().onOauthResponse(requestCode, resultCode, data);
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
