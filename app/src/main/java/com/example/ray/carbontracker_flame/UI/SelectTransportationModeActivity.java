package com.example.ray.carbontracker_flame.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ray.carbontracker_flame.Model.Bus;
import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
import com.example.ray.carbontracker_flame.Model.SkyTrain;
import com.example.ray.carbontracker_flame.Model.TRANSPORT_TYPES;
import com.example.ray.carbontracker_flame.Model.WalkingAsTransport;
import com.example.ray.carbontracker_flame.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.ray.carbontracker_flame.UI.AddCarActivity.TAG_CAR_ACTIVITY_TYPE;
import static com.example.ray.carbontracker_flame.UI.AddCarActivity.TAG_POSITION_SELECTED;
import static com.example.ray.carbontracker_flame.UI.TransportationActivity.ACTIVITY_CODE_ADD;
import static com.example.ray.carbontracker_flame.UI.TransportationActivity.INITIALIZE_VALUE;

/**
 * Select TransportationModeActivity defines the behavior of the Select Transportation screen
 */
public class SelectTransportationModeActivity extends AppCompatActivity {
    public static final int REQUEST_GET_TRANSPORTATION_MODE = 1;
    public static final String TAG_TRANSPORT_TYPE_SELECTED = "transport_type_selected";

    private int journeyActivityCode;
    private int journeyPosition;
    private CarbonTrackerModel ctm;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_transportation_mode);

        ctm = CarbonTrackerModel.getInstance();
        ctx = this;

        setArgument();
        createActionBar();
        setUpCarButton();
        setUpWalkOrCycleButton();
        setupBusButton();
        setupSkyTrainButton();
    }

    private void createActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            String ERROR_MESSAGE = e.getMessage();
            Log.i("SelectTransModeAct", ERROR_MESSAGE);
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

    private void setArgument() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        journeyActivityCode = extras.getInt(JourneyActivity.TAG_JOURNEY_ACTIVITY_TYPE);
        journeyPosition = extras.getInt(JourneyActivity.TAG_JOURNEY_POSITION_SELECTED);
    }

    private void startRouteActivity(int buttonID, final int transportType) {
        Button btn = (Button) findViewById(buttonID);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (journeyActivityCode == JourneyActivity.ACTIVITY_CODE_ADD_JOURNEY) {
                    Intent intent = new Intent(SelectTransportationModeActivity.this, RouteActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(TAG_TRANSPORT_TYPE_SELECTED, transportType);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                } else if (journeyActivityCode == JourneyActivity.ACTIVITY_CODE_TRANSPORTATION_EDIT) {
                    SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
                    Calendar date = ctm.journeyCollection.getSavedJourneys().get(journeyPosition).getDate();
                    String dateFormat = form.format(date.getTime());
                    switch (transportType) {
                        case TRANSPORT_TYPES.WALKING_OR_CYCLING:
                            ctm.journeyCollection.getSavedJourneys().get(journeyPosition)
                                    .editTransportation(new WalkingAsTransport());
                            ctm.updateJourneyCarTypeInDatabase(
                                    ctm.journeyCollection.getSavedJourneys().get(journeyPosition).getJourneyId(),
                                    transportType, CarbonTrackerModel.INITIALIZE_VALUE,
                                    ctm.journeyCollection.getSavedJourneys().get(journeyPosition).getRoutePosition(),
                                    dateFormat, ctx);
//
                            finish();
                            TransportationActivity.firstActivity.finish();
                            break;
                        case TRANSPORT_TYPES.BUS:
                            ctm.journeyCollection.getSavedJourneys().get(journeyPosition)
                                    .editTransportation(new Bus());
                            ctm.updateJourneyCarTypeInDatabase(
                                    ctm.journeyCollection.getSavedJourneys().get(journeyPosition).getJourneyId(),
                                    transportType, CarbonTrackerModel.INITIALIZE_VALUE,
                                    ctm.journeyCollection.getSavedJourneys().get(journeyPosition).getRoutePosition(),
                                    dateFormat, ctx);
                            finish();
                            TransportationActivity.firstActivity.finish();
                            break;
                        case TRANSPORT_TYPES.SKYTRAIN:
                            ctm.journeyCollection.getSavedJourneys().get(journeyPosition)
                                    .editTransportation(new SkyTrain());
                            ctm.updateJourneyCarTypeInDatabase(
                                    ctm.journeyCollection.getSavedJourneys().get(journeyPosition).getJourneyId(),
                                    transportType, CarbonTrackerModel.INITIALIZE_VALUE,
                                    ctm.journeyCollection.getSavedJourneys().get(journeyPosition).getRoutePosition(),
                                    dateFormat, ctx);
                            finish();
                            TransportationActivity.firstActivity.finish();
                            break;
                    }
                }
            }
        });
    }

    private void setUpCarButton() {
        Button btn = (Button) findViewById(R.id.btnCar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTransportationModeActivity.this, AddCarActivity.class);
                Bundle extras = new Bundle();
                extras.putInt(TAG_CAR_ACTIVITY_TYPE, ACTIVITY_CODE_ADD);
                extras.putInt(TAG_POSITION_SELECTED, INITIALIZE_VALUE);
                extras.putInt(TAG_TRANSPORT_TYPE_SELECTED, TRANSPORT_TYPES.CAR);
                extras.putInt(JourneyActivity.TAG_JOURNEY_ACTIVITY_TYPE, journeyActivityCode);
                extras.putInt(JourneyActivity.TAG_JOURNEY_POSITION_SELECTED, journeyPosition);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setUpWalkOrCycleButton() {
        startRouteActivity(R.id.btnWalkingOrCycling, TRANSPORT_TYPES.WALKING_OR_CYCLING);
    }

    private void setupBusButton() {
        startRouteActivity(R.id.btnBus, TRANSPORT_TYPES.BUS);
    }

    private void setupSkyTrainButton() {
        startRouteActivity(R.id.btnSkyTrain, TRANSPORT_TYPES.SKYTRAIN);
    }
}