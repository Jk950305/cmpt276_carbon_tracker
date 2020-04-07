package com.example.ray.carbontracker_flame.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
import com.example.ray.carbontracker_flame.Model.Route;
import com.example.ray.carbontracker_flame.R;

/**
 * Allows user to edit a route
 */

public class EditRouteActivity extends AppCompatActivity {

    public static final String ROUTE_POSITION_KEY = "Route Position";
    private CarbonTrackerModel ctm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_route);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.edit_car));
        }
        createActionBar();

        ctm = CarbonTrackerModel.getInstance();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int routePosition = extras.getInt(ROUTE_POSITION_KEY);

        setupHints(routePosition);
        setupCancelButton();
        setupSaveButton(routePosition);
    }

    private void createActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            String ERROR_MESSAGE = e.getMessage();
            Log.i("EditRouteActivity", ERROR_MESSAGE);
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

    private void setupHints(int routePosition) {
        final String selectedRoutesName = ctm.getRouteCollection().getSavedRoutes().get(routePosition).getName();
        final double numOfCityKilometers = ctm.getRouteCollection().getSavedRoutes().get(routePosition).getNumOfCityKilometers();
        final double numOfHighWayKilometers = ctm.getRouteCollection().getSavedRoutes().get(routePosition).getNumOfHighWayKilometers();
        ((TextView) findViewById(R.id.txtNameInEditAct)).setText(selectedRoutesName);
        ((TextView) findViewById(R.id.editCityDistanceInEditAct)).setHint("" + numOfCityKilometers);
        ((TextView) findViewById(R.id.editHighWayDistanceInEditAct)).setHint("" + numOfHighWayKilometers);
    }

    private void setupCancelButton() {
        Button btn = (Button) findViewById(R.id.btnCancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupSaveButton(final int position) {
        Button btn = (Button) findViewById(R.id.btnSave);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRoute(position);
            }
        });
    }

    private void saveRoute(int position) {
        try {
            EditText edtCityKM = (EditText) findViewById(R.id.editCityDistanceInEditAct);
            String cityKMString = edtCityKM.getText().toString();
            if (cityKMString.matches("")) {
                cityKMString = "0.0";
            } else {
                edtCityKM.setText(cityKMString);
            }
            double cityKM = Double.parseDouble(cityKMString);

            EditText edtHighwayKM = (EditText) findViewById(R.id.editHighWayDistanceInEditAct);
            String highwayKMString = edtHighwayKM.getText().toString();
            if (highwayKMString.matches("")) {
                highwayKMString = "0.0";
            } else {
                edtHighwayKM.setText(highwayKMString);
            }
            double highwayKM = Double.parseDouble(highwayKMString);

            if (cityKM == 0 && highwayKM == 0) {
                throw new NumberFormatException("Highway and City cannot be both zeroes");
            }
            // Set the default value on the text view if succesful
            edtCityKM.setText(cityKMString);
            edtHighwayKM.setText(highwayKMString);

            Route route = ctm.getRouteCollection().getSavedRoutes().get(position);

            Route newRoute = new Route(route.getName(), cityKM, highwayKM, route.isSelectableOnMenu());
            ctm.updateRouteInDatabase(position + 1, newRoute, this, true);

            ctm.getRouteCollection().editRoute(route, cityKM, highwayKM, route.isSelectableOnMenu());

            finish();

        } catch (NumberFormatException e) {
            Log.i("SaveRoute", e.getMessage());
            Toast.makeText(EditRouteActivity.this, "Both distance cannot be zeroes or empty.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.wtf("SaveRoute", e.getMessage());
            Toast.makeText(EditRouteActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
        }

    }
}
