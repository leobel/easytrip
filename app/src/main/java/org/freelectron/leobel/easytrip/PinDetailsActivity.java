package org.freelectron.leobel.easytrip;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.pinterest.android.pdk.PDKPin;

import org.freelectron.leobel.easytrip.fragments.PinDetailsFragment;

import static org.freelectron.leobel.easytrip.fragments.InspireMeFragment.PIN_FRAGMENT_DETAILS;

public class PinDetailsActivity extends AppCompatActivity implements PinDetailsFragment.OnPinDetailsInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_navigation);

        Intent intent = getIntent();
        PDKPin pin = (PDKPin) intent.getSerializableExtra(PIN_FRAGMENT_DETAILS);

        org.freelectron.leobel.easytrip.fragments.PinDetailsFragment fragment = org.freelectron.leobel.easytrip.fragments.PinDetailsFragment.newInstance(pin);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.root_container, fragment);
        transaction.commit();
    }

    @Override
    public void onFindFlight(Uri uri) {

    }

    @Override
    public void onFindPlaceToStay() {

    }
}
