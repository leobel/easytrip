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
import com.pinterest.android.pdk.PDKOriginal;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKPlace;

import org.freelectron.leobel.easytrip.R;
import org.freelectron.leobel.easytrip.models.RecyclerViewManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPinDetailsInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PinDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinDetailsFragment extends Fragment {
    // the fragment initialization parameters
    private static final String ARG_PIN_PARAM = "ARG_PIN_PARAM";

    private PDKPin pin;
    private OnPinDetailsInteractionListener mListener;

    TextView boardName;
    TextView metadataName;
    TextView pinNote;
    SimpleDraweeView image;


    public PinDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pin Pinterest pin.
     * @return A new instance of fragment PinDetailsFragment.
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pin_details, container, false);

        boardName = (TextView) view.findViewById(R.id.pin_board_name_details);
        metadataName = (TextView) view.findViewById(R.id.pin_metadata_name_details);
        pinNote = (TextView) view.findViewById(R.id.pin_note_details);

        image = (SimpleDraweeView) view.findViewById(R.id.pin_image_details);

        PDKOriginal original = pin.getImage().getOriginal();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, original.getHeight(), getResources().getDisplayMetrics());
        layoutParams.addRule(RelativeLayout.BELOW, R.id.pin_metadata_name_details);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        image.setLayoutParams(layoutParams);
        image.setImageURI(Uri.parse(original.getUrl()));

        boardName.setText(pin.getBoard().getName());
        PDKPlace place = pin.getMetadata().getPlace();
        if(place != null){
            metadataName.setText(place.getName());
        }
        else{
            metadataName.setText("");
        }
        pinNote.setText(pin.getNote());

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
