package org.freelectron.leobel.easytrip;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pinterest.android.pdk.PDKPin;

import org.freelectron.leobel.easytrip.fragments.PinDetailsFragment;

import static org.freelectron.leobel.easytrip.InspireMeActivity.PIN_FRAGMENT_DETAILS;

public class PinDetailsActivity extends NavigationActivity implements PinDetailsFragment.OnPinDetailsInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        PDKPin pin = (PDKPin) intent.getSerializableExtra(PIN_FRAGMENT_DETAILS);

        PinDetailsFragment fragment = PinDetailsFragment.newInstance(pin);
        addFragment(fragment, PIN_FRAGMENT_DETAILS);
    }

    @Override
    public void onFindFlight(Uri uri) {

    }

    @Override
    public void onFindPlaceToStay() {

    }
}
