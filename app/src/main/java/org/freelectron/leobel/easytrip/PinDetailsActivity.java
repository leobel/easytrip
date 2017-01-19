package org.freelectron.leobel.easytrip;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.pinterest.android.pdk.PDKPin;

import org.freelectron.leobel.easytrip.fragments.PinDetailsFragment;

import static org.freelectron.leobel.easytrip.fragments.InspireMeFragment.PIN_FRAGMENT_DETAILS;

public class PinDetailsActivity extends AppCompatActivity implements PinDetailsFragment.OnPinDetailsInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());

        Intent intent = getIntent();
        PDKPin pin = (PDKPin) intent.getSerializableExtra(PIN_FRAGMENT_DETAILS);

        org.freelectron.leobel.easytrip.fragments.PinDetailsFragment fragment = org.freelectron.leobel.easytrip.fragments.PinDetailsFragment.newInstance(pin);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.pin_details, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onFindFlight(Uri uri) {

    }

    @Override
    public void onFindPlaceToStay() {

    }
}
