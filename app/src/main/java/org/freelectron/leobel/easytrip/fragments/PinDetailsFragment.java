package org.freelectron.leobel.easytrip.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pinterest.android.pdk.PDKOriginal;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKPlace;

import org.freelectron.leobel.easytrip.EasyTripApp;
import org.freelectron.leobel.easytrip.R;
import org.freelectron.leobel.easytrip.adapters.PinRecyclerViewAdapter;
import org.freelectron.leobel.easytrip.models.PageResponse;
import org.freelectron.leobel.easytrip.models.PaginateInfo;
import org.freelectron.leobel.easytrip.models.RecyclerViewAdapter;
import org.freelectron.leobel.easytrip.models.RecyclerViewListener;
import org.freelectron.leobel.easytrip.models.RecyclerViewManager;
import org.freelectron.leobel.easytrip.services.PinterestService;
import org.freelectron.leobel.easytrip.widgets.PinImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPinDetailsInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PinDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinDetailsFragment extends Fragment implements OnMapReadyCallback, RecyclerViewListener<PDKPin> {
    private static final String ARG_PIN_PARAM = "ARG_PIN_PARAM";
    private static final String ARG_BOARD_PARAM = "ARG_BOARD_PARAM";
    private static final String ITEMS_LIST = "ITEMS_LIST";
    private static final String PAGINATE_INFO = "PAGINATE_INFO";

    private PDKPin pin;
    private OnPinDetailsInteractionListener mListener;

    TextView metadataName;
    TextView pinNote;
    PinImageView image;

    MapView mapView;
    private GoogleMap map;
    private PDKPlace place;
    private static String MAPVIEW_BUNDLE_KEY = "MAPVIEW_BUNDLE_KEY";
    private TextView abouTextView;
    private TextView findMoreTextView;
    private String boardId;
    private RecyclerViewManager recyclerViewManager;

    @Inject
    public PinterestService pinterestService;



    public PinDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pin Pinterest pin.
     * @param boardId Pinterest board id.
     * @return A new instance of fragment PinDetailsActivity.
     */
    public static PinDetailsFragment newInstance(PDKPin pin, String boardId) {
        PinDetailsFragment fragment = new PinDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PIN_PARAM, pin);
        args.putString(ARG_BOARD_PARAM, boardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasyTripApp.getInstance().getComponent().inject(this);

        if (getArguments() != null) {
            pin =  (PDKPin) getArguments().getSerializable(ARG_PIN_PARAM);
            boardId = getArguments().getString(ARG_BOARD_PARAM);
            place = pin.getMetadata().getPlace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pin_details, container, false);

        metadataName = (TextView) view.findViewById(R.id.pin_metadata_name_details);
        pinNote = (TextView) view.findViewById(R.id.pin_note_details);
        abouTextView = (TextView) view.findViewById(R.id.pin_about_place);
        findMoreTextView = (TextView) view.findViewById(R.id.pin_find_more_place);

        image = (PinImageView) view.findViewById(R.id.pin_image_details);
        mapView = (MapView) view.findViewById(R.id.map);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pins_recycler_view);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        View emptyView = view.findViewById(R.id.empty_view_container);

        int columns = getResources().getInteger(R.integer.gallery_columns);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<PDKPin> items = null;
        PaginateInfo<String> paginateInfo = null;
        if(savedInstanceState != null){
            items = (ArrayList<PDKPin>)savedInstanceState.getSerializable(ITEMS_LIST);
            paginateInfo = (PaginateInfo<String>) savedInstanceState.getSerializable(PAGINATE_INFO);
        }
        recyclerViewManager = new RecyclerViewManager<>(this, recyclerView, swipeRefreshLayout, emptyView, items, paginateInfo);

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain ONLY MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        PDKOriginal original = pin.getImage().getOriginal();
        image.setImageWidth(original.getWidth());
        image.setImageHeight(original.getHeight());
        image.setImageURI(Uri.parse(original.getUrl()));


        if(place != null){
            metadataName.setText(place.getName());
            pinNote.setText(String.format("%s, %s", place.getLocality() != null ? place.getLocality() : place.getName() , place.getCountry()));
            abouTextView.setText(String.format(getString(R.string.pin_about_place), place.getName()));

            findMoreTextView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(place.getSourceUrl()));
                startActivity(intent);
            });
        }
        else{
            metadataName.setText("");
            pinNote.setText("");
            abouTextView.setText(String.format(getString(R.string.pin_about_place), ""));
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(recyclerViewManager != null){
            outState.putSerializable(ITEMS_LIST, (ArrayList<PDKPin>)recyclerViewManager.getItems());
            outState.putSerializable(PAGINATE_INFO, recyclerViewManager.getPaginateInfo());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPinDetailsInteractionListener) {
            mListener = (OnPinDetailsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPinDetailsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        Timber.d("Map ready!!!");
        if(place != null){
            LatLng coordinate = new LatLng(place.getLatitude(), place.getLongitude());
            map.addMarker(new MarkerOptions().position(coordinate).title(place.getName()));
            map.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
        }

    }

    @Override
    public RecyclerViewAdapter getAdapter() {
        return new PinRecyclerViewAdapter(R.layout.fragment_pin);
    }

    @Override
    public void itemClick(PDKPin item) {

    }

    @Override
    public Observable<PageResponse<List<PDKPin>>> getItems(PaginateInfo<?> paginateInfo) {
        String cursor = "";
        if(paginateInfo != null) {
            cursor = (String) paginateInfo.getIndex();
        }
        return pinterestService.getBoardPins(boardId, getString(R.string.pin_details_query), cursor)
                .map(listPageResponse -> {
                    if(listPageResponse.isSuccessful()){
                        List<PDKPin> pins = listPageResponse.getValue();
                        pins.remove(pin);
                        return new PageResponse<>(pins, listPageResponse.getPaginateInfo(), listPageResponse.getSource());
                    }
                    else{
                        return listPageResponse;
                    }
                });
    }

    @Override
    public void onLoadingItemsError(Throwable error) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPinDetailsInteractionListener {
        void onFindFlight(Uri uri);
        void onFindPlaceToStay();
    }

}
