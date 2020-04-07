package com.example.ray.carbontracker_flame.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
import com.example.ray.carbontracker_flame.R;

/**
 * Settings activity allows user to change app units
 */

public class SettingsActivity extends AppCompatActivity {
    CarbonTrackerModel ctm = CarbonTrackerModel.getInstance();

    public static Intent makeIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupRadioButtons();
        setupApplyButton();
    }

    private void setupRadioButtons() {
        RadioButton rdoKgCo2 = (RadioButton) findViewById(R.id.rdoKgCo2);
        rdoKgCo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctm.setUsingRelatableUnit(false);
                saveBooleanTypeUsingSharedPrefs(false);
            }
        });
        RadioButton rdoTreeDays = (RadioButton) findViewById(R.id.rdoTreeDays);
        rdoTreeDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctm.setUsingRelatableUnit(true);
                saveBooleanTypeUsingSharedPrefs(true);
            }
        });

        if (ctm.isUsingRelatableUnit()) {
            rdoTreeDays.setChecked(true);
        } else {
            rdoKgCo2.setChecked(true);
        }

    }

    private void saveBooleanTypeUsingSharedPrefs(boolean value) {
        SharedPreferences prefs = getSharedPreferences(WelcomeActivity.SHAREDPREF_TYPE, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(WelcomeActivity.SHAREDPREF_ITEM_BOOLEANTYPE, value);
        edit.apply();
    }

    private void setupApplyButton() {
        Button btnApply = (Button) findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
