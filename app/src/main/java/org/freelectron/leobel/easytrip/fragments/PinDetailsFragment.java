package org.freelectron.leobel.easytrip.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.freelectron.leobel.easytrip.R;
import org.freelectron.leobel.easytrip.models.RecyclerViewManager;
import org.freelectron.leobel.easytrip.widgets.PinImageView;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPinDetailsInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PinDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinDetailsFragment extends Fragment implements OnMapReadyCallback {
    // the fragment initialization parameters
    private static final String ARG_PIN_PARAM = "ARG_PIN_PARAM";

    private PDKPin pin;
    private OnPinDetailsInteractionListener mListener;

    TextView metadataName;
    TextView pinNote;
    PinImageView image;

    MapView mapView;
    private GoogleMap map;
    private PDKPlace place;
    private static String MAPVIEW_BUNDLE_KEY = "MAPVIEW_BUNDLE_KEY";


    public PinDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pin Pinterest pin.
     * @return A new instance of fragment PinDetailsActivity.
     */
    public static PinDetailsFragment newInstance(PDKPin pin) {
        PinDetailsFragment fragment = new PinDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PIN_PARAM, pin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pin =  (PDKPin) getArguments().getSerializable(ARG_PIN_PARAM);
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

        image = (PinImageView) view.findViewById(R.id.pin_image_details);
        mapView = (MapView) view.findViewById(R.id.map);

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
            pinNote.setText(String.format("%s, %s", place.getLocality(), place.getCountry()));
        }
        else{
            metadataName.setText("");
            pinNote.setText("");
        }

        return view;
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
