package com.example.ray.carbontracker_flame.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
import com.example.ray.carbontracker_flame.Model.CarbonUnitPrinter;
import com.example.ray.carbontracker_flame.Model.Journey;
import com.example.ray.carbontracker_flame.R;

/**
 * Displays the emissions of the journey to after selecting a route
 */

public class DisplayEmissions extends AppCompatActivity {
    public static final double MILES_IN_1KM = 0.621371;
    public static final double CO2_PER_GALLON = 8.91;
    public static final double ELECTRIC_CAR_CO2_PER_KM = 0.0;
    public static final double BUS_CO_2_PER_KM = 89.0;
    public static final double WALKING_CO_2_PER_KM = 0.0;
    //source for SkyTrain Emissions data
    //http://www.translink.ca/-/media/Documents/about_translink/corporate_overview/sustainability/translink_2012_sustainability_report.PDF
    public static final double SKY_TRAIN_CO_2_PER_KM = 50.4;
    CarbonTrackerModel model = CarbonTrackerModel.getInstance();

    public static Intent makeIntent(Context context) {
        return new Intent(context, DisplayEmissions.class);
    }

    public static double calculateEmissions(Journey journey) {
        double emissionsKg = journey.getEmissionsInKG();
        return round(emissionsKg, 1);
    }

    private static double round(double value, int precision) {
        //http://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_emissions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.journey_emissions));
        }
        createActionBar();
        setEmissionText();
        setupBackToMainButton();
    }

    private void createActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            String ERROR_MESSAGE = e.getMessage();
            Log.i("DisplayEmissions", ERROR_MESSAGE);
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

    private void setupBackToMainButton() {
        Button backToMainBtn = (Button) findViewById(R.id.backtomain_btn);
        backToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();
            }
        });
    }

    private void setEmissionText() {
        TextView emissionsTxt = (TextView) findViewById(R.id.emissions_txt);
        Journey lastJourney = model.journeyCollection.getSavedJourneys().get(model.journeyCollection.getSavedJourneys().size() - 1);
        double emissionsInKg;
        if (lastJourney == null) {
            emissionsInKg = 0;
        } else {
            emissionsInKg = calculateEmissions(lastJourney);
        }

        String emissionText = CarbonUnitPrinter.getConvertedNumStringWithUnits(emissionsInKg, 2);
        emissionsTxt.setText(emissionText);
    }

    @Override
    public void onBackPressed() {
        goToMain();
    }

    private void goToMain() {
        Intent intent = MainActivity.makeIntent(DisplayEmissions.this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();

        startActivity(intent);
        Intent intent2 = DisplayTips.makeIntent(DisplayEmissions.this);
        startActivity(intent2);
    }
}