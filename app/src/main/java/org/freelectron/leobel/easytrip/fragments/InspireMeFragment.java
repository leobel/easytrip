package org.freelectron.leobel.easytrip.fragments;

import android.content.Intent;
import android.os.Bundle;

import com.pinterest.android.pdk.PDKPin;

import org.freelectron.leobel.easytrip.EasyTripApp;
import org.freelectron.leobel.easytrip.PinDetailsActivity;

public class InspireMeFragment extends NavigationFragment implements PinListFragment.OnPinListInteractionListener{

    public static String PIN_FRAGMENT_LIST = "PIN_FRAGMENT_LIST";
    public static String PIN_FRAGMENT_DETAILS = "PIN_FRAGMENT_DETAILS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasyTripApp.getInstance().getComponent().inject(this);

        PinListFragment fragment = PinListFragment.newInstance();
        addFragment(fragment, PIN_FRAGMENT_LIST);
    }

    @Override
    public void onPinSelected(PDKPin pin) {
        Intent intent = new Intent(getActivity(), PinDetailsActivity.class);
        intent.putExtra(PIN_FRAGMENT_DETAILS, pin);
        startActivity(intent);
    }
}
