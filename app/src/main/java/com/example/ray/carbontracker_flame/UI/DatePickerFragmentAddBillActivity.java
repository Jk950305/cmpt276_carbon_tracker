package com.example.ray.carbontracker_flame.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ray.carbontracker_flame.Model.Journey;
import com.example.ray.carbontracker_flame.R;

import java.util.Calendar;

import static com.example.ray.carbontracker_flame.UI.AddBillActivity.END_DATE_TAG;
import static com.example.ray.carbontracker_flame.UI.AddBillActivity.START_DATE_TAG;
import static com.example.ray.carbontracker_flame.UI.JourneyActivity.CHANGE_DATE_TAG;
import static com.example.ray.carbontracker_flame.UI.RouteActivity.ADD_DATE_TAG;
import static com.example.ray.carbontracker_flame.UI.JourneyActivity.CURRENT_JOURNEY_TAG;

/**
 * This fragment allows the user to pick a date and updates the calendar in its calling activity
 */

public class DatePickerFragmentAddBillActivity extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    String tag;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // set the default date to the current date
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        tag = getTag();
        // Create and return a new DatePickerDialog object
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (tag.equals(CHANGE_DATE_TAG)) {
            JourneyActivity journeyActivity = (JourneyActivity) getActivity();
            journeyActivity.changeDate(year, month, day);
            journeyActivity.updateDate(CURRENT_JOURNEY_TAG);
            dismiss();

        } else if (tag.equals(ADD_DATE_TAG)) {
            RouteActivity routeActivity = (RouteActivity) getActivity();
            Calendar thisDate = routeActivity.changeDate(year, month, day);
            routeActivity.saveJourney(thisDate);
            dismiss();

        } else {
            //set the date
            AddBillActivity addBillActivity = (AddBillActivity) getActivity();
            switch (tag) {
                case START_DATE_TAG:
                    addBillActivity.setStartDate(year, month, day);
                    break;
                case END_DATE_TAG:
                    addBillActivity.setEndDate(year, month, day);
                    break;
            }
            addBillActivity.updateEditTexts();
            dismiss();
        }

    }

}