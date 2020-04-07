package com.example.ray.carbontracker_flame.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ray.carbontracker_flame.R;

/**
 * Handles functionality of the main menu
 */

public class MainActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createActionBar();
        setupButtonColour();
        setupCreateNewJourneyButton();
        setupBillsButton();
        setupDisplayCarbonFootPrintButton();
        setupSettingsButton();
        setupAboutButton();
    }

    private void setupButtonColour() {
        final int color = 0x45cc67;
        Button journeyBtn = (Button) findViewById(R.id.btnDisplayJourneys);
        Button billBtn = (Button) findViewById(R.id.btnBills);
        Button graphBtn = (Button) findViewById(R.id.btnDisplayCarbonFootPrint);
        Button settingsBtn = (Button) findViewById(R.id.btnSettings);
        Button aboutBtn = (Button) findViewById(R.id.btnAbout);
        journeyBtn.getBackground().setColorFilter(new LightingColorFilter(color, 0x0));
        billBtn.getBackground().setColorFilter(new LightingColorFilter(color, 0x0));
        graphBtn.getBackground().setColorFilter(new LightingColorFilter(color, 0x0));
        settingsBtn.getBackground().setColorFilter(new LightingColorFilter(color, 0x0));
        aboutBtn.getBackground().setColorFilter(new LightingColorFilter(color, 0x0));

    }

    private void setupAboutButton() {
        Button btnAbout = (Button) findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AboutActivity.makeIntent(MainActivity.this));
            }
        });
    }

    private void setupSettingsButton() {
        Button btnSettings = (Button) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SettingsActivity.makeIntent(MainActivity.this));
            }
        });
    }

    private void createActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            String ERROR_MESSAGE = e.getMessage();
            Log.i("MainActivity", ERROR_MESSAGE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }

    private void setupCreateNewJourneyButton() {
        Button btn_add_journey = (Button) findViewById(R.id.btnDisplayJourneys);
        btn_add_journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(JourneyActivity.makeJourneyActivityIntent(MainActivity.this));
            }
        });
    }

    private void setupBillsButton() {
        Button btn_add_journey = (Button) findViewById(R.id.btnBills);
        btn_add_journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BillListActivity.makeIntent(MainActivity.this));
            }
        });
    }

    private void setupDisplayCarbonFootPrintButton() {
        Button btn = (Button) findViewById(R.id.btnDisplayCarbonFootPrint);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CarbonFootprintActivity.makeIntent(MainActivity.this));

            }
        });
    }
}
