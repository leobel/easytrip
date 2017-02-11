package org.freelectron.leobel.easytrip.widgets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.freelectron.leobel.easytrip.models.DatePickerListener;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import timber.log.Timber;


/**
 * Created by leobel on 1/25/17.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener  {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(c.getTimeInMillis());

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // month must be in range [1,12]
        DateTime date = new DateTime(year, month + 1, day, 0, 0);
        ((DatePickerListener) getActivity()).onSetDate(date);
    }
}
