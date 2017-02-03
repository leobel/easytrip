package org.freelectron.leobel.easytrip;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pinterest.android.pdk.PDKPin;

import org.freelectron.leobel.easytrip.fragments.PinDetailsFragment;

import static org.freelectron.leobel.easytrip.fragments.InspireMeFragment.BOARD_FRAGMENT_DETAILS;
import static org.freelectron.leobel.easytrip.fragments.InspireMeFragment.PIN_FRAGMENT_DETAILS;

public class PinDetailsActivity extends AppCompatActivity implements PinDetailsFragment.OnPinDetailsInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_details);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int statusBarHeight = Utils.getStatusBarHeight(this);
        layoutParams.setMargins(0, statusBarHeight, 0, 0);
        toolbar.setLayoutParams(layoutParams);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());

        Intent intent = getIntent();
        PDKPin pin = (PDKPin) intent.getSerializableExtra(PIN_FRAGMENT_DETAILS);
        String boardId = intent.getStringExtra(BOARD_FRAGMENT_DETAILS);

        PinDetailsFragment fragment = PinDetailsFragment.newInstance(pin, boardId);
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
