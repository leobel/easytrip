package org.freelectron.leobel.easytrip.fragments;

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

    private Integer category;
    private PinListFragment fragment;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            category = getArguments().getInt(ARG_CATEGORY_PARAM);
        }
        List<PDKPin> items = null;
        if(savedInstanceState != null){
            items = (ArrayList<PDKPin>)savedInstanceState.getSerializable(ITEMS_LIST);
        }

        fragment = PinListFragment.newInstance(category, items);
        addFragment(fragment, PIN_FRAGMENT_LIST);
    }

    @Override
    public void onPinSelected(PDKPin pin) {
        Intent intent = new Intent(getActivity(), PinDetailsActivity.class);
        intent.putExtra(PIN_FRAGMENT_DETAILS, pin);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ITEMS_LIST, (ArrayList<PDKPin>)fragment.getItems());
    }
}
