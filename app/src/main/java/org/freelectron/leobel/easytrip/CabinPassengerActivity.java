package org.freelectron.leobel.easytrip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.NumberPicker;
import android.widget.TextView;

public class CabinPassengerActivity extends AppCompatActivity {

    public static String ADULTS_PARAM = "ADULTS_PARAM";
    public static String CHILDREN_PARAM = "CHILDREN_PARAM";
    public static String INFANTS_PARAM = "INFANTS_PARAM";


    private NumberPicker numberPickerAdults;
    private NumberPicker numberPickerChildren;
    private NumberPicker numberPickerInfants;

    private Integer adults;
    private Integer children;
    private Integer infants;

    private TextView restrictionText;

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

        Intent intent = getIntent();

        adults = intent.getIntExtra(ADULTS_PARAM, 1);
        children = intent.getIntExtra(CHILDREN_PARAM, 0);
        infants = intent.getIntExtra(INFANTS_PARAM, 0);

        numberPickerAdults.setMinValue(1);
        numberPickerAdults.setMaxValue(8);
        numberPickerAdults.setValue(adults);

        numberPickerChildren.setMinValue(0);
        numberPickerChildren.setMaxValue(8);
        numberPickerChildren.setValue(children);

        numberPickerInfants.setMinValue(0);
        numberPickerInfants.setMaxValue(8);
        numberPickerInfants.setValue(infants);

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
            }
            else{
                restrictionText.setText("");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cabin_passenger, menu);
        return true;
    }
}
