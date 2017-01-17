package org.freelectron.leobel.easytrip;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.pinterest.android.pdk.PDKPin;

import org.freelectron.leobel.easytrip.fragments.PinDetailsFragment;
import org.freelectron.leobel.easytrip.fragments.PinListFragment;
import org.freelectron.leobel.easytrip.services.PinterestService;

import javax.inject.Inject;

public class InspireMeActivity extends NavigationActivity implements PinListFragment.OnPinListInteractionListener{

    public static String PIN_FRAGMENT_LIST = "PIN_FRAGMENT_LIST";
    public static String PIN_FRAGMENT_DETAILS = "PIN_FRAGMENT_DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasyTripApp.getInstance().getComponent().inject(this);

        PinListFragment fragment = PinListFragment.newInstance();
        addFragment(fragment, PIN_FRAGMENT_LIST);
    }

    @Override
    public void onPinSelected(PDKPin pin) {
        Intent intent = new Intent(this, PinDetailsActivity.class);
        intent.putExtra(PIN_FRAGMENT_DETAILS, pin);
        startActivity(intent);
    }
}
