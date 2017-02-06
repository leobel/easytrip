package org.freelectron.leobel.easytrip.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pinterest.android.pdk.PDKPin;

import org.freelectron.leobel.easytrip.EasyTripApp;
import org.freelectron.leobel.easytrip.PinDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class InspireMeFragment extends NavigationFragment implements PinListFragment.OnPinListInteractionListener{

    private static final String ARG_CATEGORY_PARAM = "ARG_CATEGORY_PARAM";
    private static final String ITEMS_LIST = "ITEMS_LIST";


    public static String PIN_FRAGMENT_LIST = "PIN_FRAGMENT_LIST";
    public static String PIN_FRAGMENT_DETAILS = "PIN_FRAGMENT_DETAILS";
    public static String BOARD_FRAGMENT_DETAILS = "BOARD_FRAGMENT_DETAILS";

    private Integer category;
    private PinListFragment fragment;
    private OnPinterestListener listener;

    public InspireMeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category Travel category.
     * @return A new instance of fragment InspireMeFragment.
     */
    public static InspireMeFragment newInstance(Integer category) {
        InspireMeFragment fragment = new InspireMeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_PARAM, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (OnPinterestListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnPinterestListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            category = getArguments().getInt(ARG_CATEGORY_PARAM);
        }

        fragment = (PinListFragment) findFragment(PIN_FRAGMENT_LIST);
        if(fragment == null){
            fragment = PinListFragment.newInstance(category);
            addFragment(fragment, PIN_FRAGMENT_LIST);
        }
        else{
            attachFragment(fragment);
        }
    }

    @Override
    public void onPinSelected(PDKPin pin, String boardId) {
        Intent intent = new Intent(getActivity(), PinDetailsActivity.class);
        intent.putExtra(PIN_FRAGMENT_DETAILS, pin);
        intent.putExtra(BOARD_FRAGMENT_DETAILS, boardId);
        startActivity(intent);
    }

    @Override
    public void onLoadingItemsError(Throwable error) {
        listener.onLoadingItemsError(error);
    }


    public interface OnPinterestListener {
        void onLoadingItemsError(Throwable error);
    }
}
