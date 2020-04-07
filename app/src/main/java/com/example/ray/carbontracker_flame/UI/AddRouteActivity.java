package com.example.ray.carbontracker_flame.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
//import com.example.ray.carbontracker_flame.Model.DBRouteAdapter;
import com.example.ray.carbontracker_flame.Model.Route;
import com.example.ray.carbontracker_flame.R;

/**
 * AddRouteActivity allows user to add a route
 */

public class AddRouteActivity extends AppCompatActivity {

    public static final String SAVE_ROUTE = "SaveRoute";
    private CarbonTrackerModel ctm;

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddRouteActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.add_route));
        }
        ctm = CarbonTrackerModel.getInstance();
        createActionBar();
        setupCancelButton();
        setupSaveButton();
    }

    private void createActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            String ERROR_MESSAGE = e.getMessage();
            Log.i("AddRouteActivity", ERROR_MESSAGE);
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

    private void setupCancelButton() {
        Button btn = (Button) findViewById(R.id.btnCancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close this activity
                finish();
            }
        });
    }

    private void setupSaveButton() {
        Button btn = (Button) findViewById(R.id.btnSave);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //save route
                    saveRoute();
                    //close this activity
                    finish();
                } catch (StringIndexOutOfBoundsException e) {
                    Log.i(SAVE_ROUTE, e.getMessage());
                    Toast.makeText(AddRouteActivity.this, "Invalid Input, Route Name is empty!", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Log.i(SAVE_ROUTE, e.getMessage());
                    Toast.makeText(AddRouteActivity.this, "Invalid Input, Both distance are zeroes", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.wtf(SAVE_ROUTE, e.getMessage());
                    Toast.makeText(AddRouteActivity.this, "Something has an invalid input.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveRoute() {
        EditText edtRouteName = (EditText) findViewById(R.id.edtRouteName);
        String routeNameString = edtRouteName.getText().toString();
        if (routeNameString.length() == 0) {
            throw new StringIndexOutOfBoundsException("Empty String!");
        }

        EditText edtCityKM = (EditText) findViewById(R.id.edtCityDistance);
        String cityKMString = edtCityKM.getText().toString();
        if (edtCityKM.getText().toString().matches("")) {
            cityKMString = "0.0";
        } else {
            edtCityKM.setText(cityKMString);
        }
        double cityKM = Double.parseDouble(cityKMString);

        EditText edtHighwayKM = (EditText) findViewById(R.id.edtHighWayDistance);
        String highwayKMString = edtHighwayKM.getText().toString();
        if (edtHighwayKM.getText().toString().matches("")) {
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

        Route route = new Route(routeNameString, cityKM, highwayKM, true);

        ctm.addRouteToDatabase(route, this);

        ctm.getRouteCollection().addRoute(route);
    }

}
