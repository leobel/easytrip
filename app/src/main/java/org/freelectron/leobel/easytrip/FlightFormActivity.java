package org.freelectron.leobel.easytrip;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.freelectron.leobel.easytrip.models.DatePickerListener;
import org.freelectron.leobel.easytrip.models.Place;
import org.freelectron.leobel.easytrip.widgets.DatePickerFragment;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FlightFormActivity extends AppCompatActivity implements DatePickerListener {

    private static final int REQUEST_FROM_PLACE = 1;
    private static final int REQUEST_TO_PLACE = 2;
    private static final int REQUEST_DEPARTURE_TIME = 3;
    private static final int REQUEST_RETURN_TIME = 4;
    private static final int REQUEST_CABIN_PASSENGERS = 5;

    private static final String DEPARTURE_PLACE = "DEPARTURE_PLACE";
    private static final String DESTINATION_PLACE = "DESTINATION_PLACE";
    private static final String DEPARTURE_DATE = "DEPARTURE_DATE";
    private static final String RETURN_DATE = "RETURN_DATE";
    private static final String FLIGHT_TYPE = "FLIGHT_TYPE";
    public static final String TRAVELERS_ADULTS = "TRAVELERS_ADULTS";
    public static final String TRAVELERS_CHILDREN= "TRAVELERS_CHILDREN";
    public static final String TRAVELERS_INFANTS = "TRAVELERS_INFANTS";
    public static final String TRAVELERS_CABIN_CLASS = "TRAVELERS_CABIN_CLASS";

    private static String[] cabinClass = new String[]{"Economy", "PremiumEconomy", "Business", "First"};

    View flightReturnContainer;
    private Animation fadeIn;
    private Animation fadeOut;

    Button searchFlightFrom;
    Button searchFlightTo;
    Button searchFlightDeparture;
    Button searchFlightReturn;
    Button searchFlightClass;
    Button searchFlight;
    RadioGroup radioGroup;


    private int flightTime;
    private DateTimeFormatter formatter;

    private Place departurePlace;
    private Place destinationPlace;
    private int flightType;
    private DateTime departureDate;
    private DateTime returnDate;
    private int adults;
    private int children;
    private int infants;
    private int cabin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_form);

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

        formatter = DateTimeFormat.forPattern("dd MMM yyyy");

        SimpleDraweeView image = (SimpleDraweeView) findViewById(R.id.background_image);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(R.drawable.flight_form))
                .build();
        image.setImageURI(uri);

        flightReturnContainer = findViewById(R.id.flight_return_container);
        searchFlightFrom = (Button) findViewById(R.id.search_flight_from);
        searchFlightTo = (Button) findViewById(R.id.search_flight_to);
        radioGroup = (RadioGroup) findViewById(R.id.flight_radio_group);
        searchFlightDeparture = (Button) findViewById(R.id.search_flight_departure);
        searchFlightReturn = (Button) findViewById(R.id.search_flight_return);
        searchFlightClass = (Button) findViewById(R.id.search_flight_class);
        searchFlight = (Button) findViewById(R.id.search_flight);

        searchFlightFrom.setOnClickListener(view -> {
            startActivityForResult(new Intent(this, SearchPlaceActivity.class), REQUEST_FROM_PLACE);
        });

        searchFlightTo.setOnClickListener(view -> {
            startActivityForResult(new Intent(this, SearchPlaceActivity.class), REQUEST_TO_PLACE);
        });

        searchFlightDeparture.setOnClickListener(view -> {
            flightTime = REQUEST_DEPARTURE_TIME;
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });

        searchFlightReturn.setOnClickListener(view -> {
            flightTime = REQUEST_RETURN_TIME;
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });

        searchFlightClass.setOnClickListener(view -> {
            Intent intent =  new Intent(this, CabinPassengerActivity.class);
            intent.putExtra(TRAVELERS_ADULTS, adults);
            intent.putExtra(TRAVELERS_CHILDREN, children);
            intent.putExtra(TRAVELERS_INFANTS, infants);
            intent.putExtra(TRAVELERS_CABIN_CLASS, cabin);
            startActivityForResult(intent, REQUEST_CABIN_PASSENGERS);
        });

        searchFlight.setOnClickListener(view -> {
            if(isValidForm()){

            }
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

        if(savedInstanceState != null){
            departurePlace = (Place) savedInstanceState.getSerializable(DEPARTURE_PLACE);
            destinationPlace = (Place) savedInstanceState.getSerializable(DESTINATION_PLACE);
            flightType = savedInstanceState.getInt(FLIGHT_TYPE);
            departureDate = (DateTime) savedInstanceState.getSerializable(DEPARTURE_DATE);
            returnDate = (DateTime) savedInstanceState.getSerializable(RETURN_DATE);
            adults = savedInstanceState.getInt(TRAVELERS_ADULTS);
            children = savedInstanceState.getInt(TRAVELERS_CHILDREN);
            infants = savedInstanceState.getInt(TRAVELERS_INFANTS);
            cabin = savedInstanceState.getInt(TRAVELERS_CABIN_CLASS);
        }
        else{
            departureDate = DateTime.now().withTime(0,0,0,0);
            returnDate = departureDate.plusDays(2);
            flightType = 0;
            adults = 1;
            children = 0;
            infants = 0;
            cabin = 0;
        }

        if(departurePlace != null){
            searchFlightFrom.setText(departurePlace.getPlaceName());
        }
        if(destinationPlace != null){
            searchFlightTo.setText(destinationPlace.getPlaceName());
        }

        searchFlightDeparture.setText(formatter.print(departureDate));
        searchFlightReturn.setText(formatter.print(returnDate));

        if(flightType == 1){
            radioGroup.check(R.id.flight_one_way);
            flightReturnContainer.startAnimation(fadeOut);
        }
        else{
            radioGroup.check(R.id.flight_round_trip);
            flightReturnContainer.startAnimation(fadeIn);
        }

        setCabinAndPassengers(adults, children, infants, cabin);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_FROM_PLACE:
                    departurePlace = (Place) data.getSerializableExtra(SearchPlaceActivity.PLACE);
                    searchFlightFrom.setText(departurePlace.getPlaceName());
                    if(searchFlightFrom.getError() != null){
                        searchFlightFrom.setError(null);
                    }
                    break;
                case REQUEST_TO_PLACE:
                    destinationPlace = (Place) data.getSerializableExtra(SearchPlaceActivity.PLACE);
                    searchFlightTo.setText(destinationPlace.getPlaceName());
                    if(searchFlightTo.getError() != null){
                        searchFlightTo.setError(null);
                    }
                    break;
                case REQUEST_CABIN_PASSENGERS:
                    adults = data.getIntExtra(CabinPassengerActivity.ADULTS_PARAM, 1);
                    children = data.getIntExtra(CabinPassengerActivity.CHILDREN_PARAM, 0);
                    infants = data.getIntExtra(CabinPassengerActivity.INFANTS_PARAM, 0);
                    cabin = data.getIntExtra(CabinPassengerActivity.CABIN_CLASS_PARAM, 0);
                    setCabinAndPassengers(adults, children, infants, cabin);
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(departurePlace != null){
            outState.putSerializable(DEPARTURE_PLACE, departurePlace);
        }
        if(destinationPlace != null){
            outState.putSerializable(DESTINATION_PLACE, destinationPlace);
        }

        outState.putInt(FLIGHT_TYPE, flightType);
        outState.putSerializable(DEPARTURE_DATE, departureDate);
        outState.putSerializable(RETURN_DATE, returnDate);
        outState.putInt(TRAVELERS_ADULTS, adults);
        outState.putInt(TRAVELERS_CHILDREN, children);
        outState.putInt(TRAVELERS_INFANTS, infants);
        outState.putInt(TRAVELERS_CABIN_CLASS, cabin);
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
                    flightType = 0;
                    returnDate = departureDate.plusDays(2);
                    searchFlightReturn.setText(formatter.print(returnDate));
                    flightReturnContainer.startAnimation(fadeIn);
                    break;
            case R.id.flight_one_way:
                if (checked)
                    flightType = 1;
                    returnDate = null;
                    searchFlightReturn.setText("");
                    if(searchFlightReturn.getError() != null){
                        searchFlightReturn.setError(null);
                    }
                    flightReturnContainer.startAnimation(fadeOut);
                    break;
        }
    }



    @Override
    public void onSetDate(DateTime date) {
        if(flightTime == REQUEST_DEPARTURE_TIME){
            departureDate = date;
            if(searchFlightDeparture.getError() != null){
                searchFlightDeparture.setError(null);
            }
            if(flightType == 0 && returnDate != null && date.isAfter(returnDate)){
                returnDate = null;
                searchFlightReturn.setText("");
            }

            searchFlightDeparture.setText(formatter.print(date));
        }
        else{
            returnDate = date;
            if(searchFlightReturn.getError() != null){
                searchFlightReturn.setError(null);
            }
            if(departureDate != null && date.isBefore(departureDate)){
                departureDate = null;
                searchFlightDeparture.setText("");
            }
            searchFlightReturn.setText(formatter.print(date));
        }
    }


    private void setCabinAndPassengers(int adults, int children, int infants, int cabin) {
        int travelers = adults + children + infants;
        String text = travelers > 1 ? getString(R.string.flight_travelers) : getString(R.string.flight_traveler);
        searchFlightClass.setText(String.format("%d %s, %s", travelers, text, cabinClass[cabin]));
    }

    private boolean isValidForm(){
        if(departurePlace == null){
            searchFlightFrom.setError("");
            Toast.makeText(this, R.string.flight_invalid_departure_place, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(destinationPlace == null){
            searchFlightTo.setError("");
            Toast.makeText(this, R.string.flight_invalid_destination_place, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(departureDate == null){
            searchFlightDeparture.setError("");
            Toast.makeText(this, R.string.flight_invalid_departure_date, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(flightType == 0 ){
            if(returnDate == null){
                searchFlightReturn.setError("");
                Toast.makeText(this, R.string.flight_invalid_return_date, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
