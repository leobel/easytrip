package org.freelectron.leobel.easytrip;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CabinPassengerActivity extends AppCompatActivity {

    private static String RESTRICTION_TEXT = "RESTRICTION_TEXT";
    public static String ADULTS_PARAM = "ADULTS_PARAM";
    public static String CHILDREN_PARAM = "CHILDREN_PARAM";
    public static String INFANTS_PARAM = "INFANTS_PARAM";
    public static String CABIN_CLASS_PARAM = "CABIN_CLASS_PARAM";


    private NumberPicker numberPickerAdults;
    private NumberPicker numberPickerChildren;
    private NumberPicker numberPickerInfants;

    private Integer adults;
    private Integer children;
    private Integer infants;
    private Integer cabin;
    private String errorText;

    private TextView restrictionText;
    private MenuItem saveItem;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cabin_passenger);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());

        numberPickerAdults = (NumberPicker) findViewById(R.id.number_picker_adults);
        numberPickerChildren = (NumberPicker) findViewById(R.id.number_picker_children);
        numberPickerInfants = (NumberPicker) findViewById(R.id.number_picker_infants);
        restrictionText = (TextView) findViewById(R.id.flight_passenger_restrictions);
        radioGroup = (RadioGroup) findViewById(R.id.flight_cabin_class_radio_group);

        Intent intent = getIntent();
        adults = intent.getIntExtra(FlightFormActivity.TRAVELERS_ADULTS, 1);
        children = intent.getIntExtra(FlightFormActivity.TRAVELERS_CHILDREN, 0);
        infants = intent.getIntExtra(FlightFormActivity.TRAVELERS_INFANTS, 0);
        cabin = intent.getIntExtra(FlightFormActivity.TRAVELERS_CABIN_CLASS, 0);

        if(savedInstanceState != null){
            adults = savedInstanceState.getInt(ADULTS_PARAM);
            children = savedInstanceState.getInt(CHILDREN_PARAM);
            infants = savedInstanceState.getInt(INFANTS_PARAM);
            cabin = savedInstanceState.getInt(CABIN_CLASS_PARAM);
            errorText = savedInstanceState.getString(RESTRICTION_TEXT, "");
        }

        numberPickerAdults.setMinValue(1);
        numberPickerAdults.setMaxValue(8);
        numberPickerAdults.setValue(adults);

        numberPickerChildren.setMinValue(0);
        numberPickerChildren.setMaxValue(8);
        numberPickerChildren.setValue(children);

        numberPickerInfants.setMinValue(0);
        numberPickerInfants.setMaxValue(8);
        numberPickerInfants.setValue(infants);

        restrictionText.setText(errorText);

        switch (cabin){
            case 0:
                radioGroup.check(R.id.flight_economy_class);
                break;
            case 1:
                radioGroup.check(R.id.flight_premium_economy_class);
                break;
            case 2:
                radioGroup.check(R.id.flight_business_class);
                break;
            case 3:
                radioGroup.check(R.id.flight_first_class);
                break;
        }

        numberPickerAdults.setOnValueChangedListener((numberPicker, oldValue, newValue) -> {
            if(newValue < numberPickerInfants.getValue()){
                restrictionText.setText(getString(R.string.infants_restriction));
            }
            else{
                restrictionText.setText("");
            }
        });

        numberPickerInfants.setOnValueChangedListener((numberPicker, oldValue, newValue) -> {
            if(newValue > numberPickerAdults.getValue()){
                restrictionText.setText(getString(R.string.infants_restriction));
                saveItem.setEnabled(false);
            }
            else{
                restrictionText.setText("");
                saveItem.setEnabled(true);
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ADULTS_PARAM, numberPickerAdults.getValue());
        outState.putInt(CHILDREN_PARAM, numberPickerChildren.getValue());
        outState.putInt(INFANTS_PARAM, numberPickerInfants.getValue());
        outState.putInt(CABIN_CLASS_PARAM, cabin);
        outState.putString(RESTRICTION_TEXT, restrictionText.getText().toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cabin_passenger, menu);
        saveItem = menu.findItem(R.id.cabin_passenger_save);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.cabin_passenger_save) {
            adults = numberPickerAdults.getValue();
            children= numberPickerChildren.getValue();
            infants = numberPickerInfants.getValue();
            Intent intent = new Intent();
            intent.putExtra(ADULTS_PARAM, adults);
            intent.putExtra(CHILDREN_PARAM, children);
            intent.putExtra(INFANTS_PARAM, infants);
            intent.putExtra(CABIN_CLASS_PARAM, cabin);
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.flight_economy_class:
                if (checked)
                    cabin = 0;
                break;
            case R.id.flight_premium_economy_class:
                if (checked)
                    cabin = 1;
                break;
            case R.id.flight_business_class:
                if (checked)
                    cabin = 2;
                    break;
            case R.id.flight_first_class:
                if (checked)
                    cabin = 3;
                    break;
        }
    }
}
