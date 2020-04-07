package com.example.ray.carbontracker_flame.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ray.carbontracker_flame.R;

import java.util.Calendar;

/**
 * This fragment allows user to pick a date for the display pie chart activity
 */

public class DatePickerFragmentGraph extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Calendar cal = Calendar.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current date as the default date in the date picker
        setDateFromBundle();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                CarbonFootprintPie parentFragment = (CarbonFootprintPie) getTargetFragment();
                parentFragment.onDateReceived(year, monthOfYear, dayOfMonth);
            }
        };

        //Create a new DatePickerDialog instance and return it
        /*
            DatePickerDialog Public Constructors - Here we uses first one
            public DatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
            public DatePickerDialog (Context context, int theme, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
         */
        return new DatePickerDialog(getActivity(), listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        Toast.makeText(getContext(), "date selected: " + year + " " + month + " " + dayOfMonth, Toast.LENGTH_LONG).show();
//        EditText calendarInput = (EditText) getActivity().findViewById(R.id.pie_date_input);
//        calendarInput.setText(year + "/" + month + "/" + dayOfMonth);
    }

    public void setDateFromBundle() {
        Bundle setDate = this.getArguments();
        int year = setDate.getInt(getString(R.string.year1));
        int month = setDate.getInt(getString(R.string.moth1));
        int day = setDate.getInt(getString(R.string.day1));
        this.cal.set(year, month, day);
    }
}

