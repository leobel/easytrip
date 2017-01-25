package org.freelectron.leobel.easytrip;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.freelectron.leobel.easytrip.models.Place;

public class FlightFormActivity extends AppCompatActivity {

    private static final int REQUEST_FROM_PLACE = 1;
    private static final int REQUEST_TO_PLACE = 2;
    private static final int REQUEST_DEPARTURE_TIME = 3;
    private static final int REQUEST_RETURN_TIME = 4;
    private static final int REQUEST_CABIN_PASSENGERS = 5;

    View flightReturnContainer;
    private Animation fadeIn;
    private Animation fadeOut;

    Button searchFlightFrom;
    Button searchFlightTo;
    Button searchFlightDeparture;
    Button searchFlightReturn;
    Button searchFlightClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_form);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int statusBarHeight = getStatusBarHeight();
        layoutParams.setMargins(0, statusBarHeight, 0, 0);
        toolbar.setLayoutParams(layoutParams);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());

        SimpleDraweeView image = (SimpleDraweeView) findViewById(R.id.background_image);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(R.drawable.flight_form))
                .build();
        image.setImageURI(uri);

        flightReturnContainer = findViewById(R.id.flight_return_container);
        searchFlightFrom = (Button) findViewById(R.id.search_flight_from);
        searchFlightTo = (Button) findViewById(R.id.search_flight_to);
        searchFlightDeparture = (Button) findViewById(R.id.search_flight_departure);
        searchFlightReturn = (Button) findViewById(R.id.search_flight_return);
        searchFlightClass = (Button) findViewById(R.id.search_flight_class);

        searchFlightFrom.setOnClickListener(view -> {
            startActivityForResult(new Intent(this, SearchPlaceActivity.class), REQUEST_FROM_PLACE);
        });

        searchFlightTo.setOnClickListener(view -> {
            startActivityForResult(new Intent(this, SearchPlaceActivity.class), REQUEST_TO_PLACE);
        });

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                flightReturnContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                flightReturnContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Place place = null;
            switch (requestCode){
                case REQUEST_FROM_PLACE:
                    place = (Place) data.getSerializableExtra(SearchPlaceActivity.PLACE);
                    searchFlightFrom.setText(place.getPlaceName());
                    break;
                case REQUEST_TO_PLACE:
                    place = (Place) data.getSerializableExtra(SearchPlaceActivity.PLACE);
                    searchFlightTo.setText(place.getPlaceName());
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.flight_round_trip:
                if (checked)
                    flightReturnContainer.startAnimation(fadeIn);
                    break;
            case R.id.flight_one_way:
                if (checked)
                    flightReturnContainer.startAnimation(fadeOut);
                    break;
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
