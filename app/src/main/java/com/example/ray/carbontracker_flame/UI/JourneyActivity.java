package com.example.ray.carbontracker_flame.UI;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.carbontracker_flame.Model.Bus;
import com.example.ray.carbontracker_flame.Model.Car;
import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
//import com.example.ray.carbontracker_flame.Model.DBJourneyAdapter;
import com.example.ray.carbontracker_flame.Model.Journey;
import com.example.ray.carbontracker_flame.Model.Route;
import com.example.ray.carbontracker_flame.Model.SkyTrain;
import com.example.ray.carbontracker_flame.Model.TRANSPORT_TYPES;
import com.example.ray.carbontracker_flame.Model.WalkingAsTransport;
import com.example.ray.carbontracker_flame.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Displays a list of journeys
 */

public class JourneyActivity extends AppCompatActivity {
    public static final int ACTIVITY_CODE_TRANSPORTATION_EDIT = 1010;
    public static final int ACTIVITY_CODE_ADD_JOURNEY = 1011;
    public static final String TAG_JOURNEY_ACTIVITY_TYPE = "ActivityJourneyType";
    public static final String TAG_JOURNEY_POSITION_SELECTED = "journeyPosSelected";
    public static final String CHANGE_DATE_TAG = "Change Date";
    private static final int EDIT_ROUTE_CODE = 1;
    private static final int EDIT_VEHICLE_CODE = 2;
    private static final int ADD_JOURNEY_CODE = 3;
    private static final String TAG_POSITION_SELECTED = "positionSelected";
    public static int CURRENT_JOURNEY_TAG;
    private Calendar date;
    private Calendar createdDate;

    private CarbonTrackerModel ctm = CarbonTrackerModel.getInstance();


    public static Intent makeJourneyActivityIntent(Context context) {
        return new Intent(context, JourneyActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        createActionBar();
        populateListView();
        createJourneyButton();
        setupContextMenu();
        registerClickCallBack();
    }

    private void createActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            String ERROR_MESSAGE = e.getMessage();
            Log.i("JourneyActivity", ERROR_MESSAGE);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        populateListView();
    }

    private void registerClickCallBack() {
        ListView list = (ListView) findViewById(R.id.lstJourney);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //IDK Which feature goes here.
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.selectOptions);
        menu.add(0, v.getId(), 0, R.string.editTransportation);
        menu.add(0, v.getId(), 0, R.string.editRoute);
        menu.add(0, v.getId(), 0, R.string.editDate);
        menu.add(0, v.getId(), 0, R.string.Delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == getString(R.string.editTransportation)) {
            //Toast.makeText(JourneyActivity.this, "Edit Vehicle", Toast.LENGTH_SHORT).show();
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            List<Journey> journeys = ctm.journeyCollection.getSavedJourneys();
            Journey current = journeys.get(position);

            Intent intent = new Intent(JourneyActivity.this, TransportationActivity.class);

            Bundle extras = new Bundle();
            extras.putInt(TAG_JOURNEY_ACTIVITY_TYPE, ACTIVITY_CODE_TRANSPORTATION_EDIT);
            extras.putInt(TAG_JOURNEY_POSITION_SELECTED, position);
            intent.putExtras(extras);
            startActivityForResult(intent, EDIT_VEHICLE_CODE);

        } else if (item.getTitle() == getString(R.string.editRoute)) {
            Toast.makeText(JourneyActivity.this, R.string.EditRoute, Toast.LENGTH_SHORT).show();
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            List<Journey> journeys = ctm.journeyCollection.getSavedJourneys();
            List<Route> routes = ctm.getRouteCollection().getSavedRoutes();
            Journey current = journeys.get(position);
            int routePosition = routes.indexOf(current.getRouteTaken());
            Intent intent = new Intent(JourneyActivity.this, EditRouteActivity.class);
            Bundle extras = new Bundle();
            extras.putInt("Route Position", routePosition);
            intent.putExtras(extras);
            startActivityForResult(intent, EDIT_ROUTE_CODE);

        } else if (item.getTitle() == getString(R.string.editDate)) {
            Toast.makeText(JourneyActivity.this, R.string.EditDate, Toast.LENGTH_SHORT).show();

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //Toast.makeText(JourneyActivity.this, "Edit Date", Toast.LENGTH_SHORT).show();
            int position = info.position;
            CURRENT_JOURNEY_TAG = position;
            DialogFragment dialogFragment = new DatePickerFragmentAddBillActivity();
            dialogFragment.show(getSupportFragmentManager(), CHANGE_DATE_TAG);
        } else if (item.getTitle() == getString(R.string.delete2)) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            List<Journey> journeys = ctm.journeyCollection.getSavedJourneys();
            ctm.deleteJourneyInDatabase(journeys.get(position).getJourneyId(), this);
            // journeys.remove(position);
            ctm.journeyCollection.deleteJourney(position);
        } else {
            return false;
        }
        populateListView();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        populateListView();
    }

    private void setupContextMenu() {
        ListView list = (ListView) findViewById(R.id.lstJourney);
        registerForContextMenu(list);
    }

    private void createJourneyButton() {
        Button btn = (Button) findViewById(R.id.btnAddJourney);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //AddJourney
                Intent intent = new Intent(JourneyActivity.this, TransportationActivity.class);

                Bundle extras = new Bundle();
                extras.putInt(TAG_JOURNEY_ACTIVITY_TYPE, ACTIVITY_CODE_ADD_JOURNEY);
                extras.putInt(TAG_JOURNEY_POSITION_SELECTED, -1);
                intent.putExtras(extras);

                startActivityForResult(intent, ADD_JOURNEY_CODE);
            }
        });
    }

    private void populateListView() {
        /*
        ArrayList<Journey> journeys = ctm.journeyCollection.getJourneyDescriptions(ctm);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.journey_list_view_layout,
                journeys
        );
        ListView list = (ListView) findViewById(R.id.lstJourney);
        list.setAdapter(adapter);
         */
        ArrayAdapter<Journey> journeyAdapter = new JourneyListAdapter();
        ListView list = (ListView) findViewById(R.id.lstJourney);
        list.setAdapter(journeyAdapter);

    }

    private class JourneyListAdapter extends ArrayAdapter<Journey> {

        public JourneyListAdapter() {
            super(JourneyActivity.this, R.layout.car_lists, ctm.journeyCollection.getSavedJourneys());
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View journeyView = convertView;
            if (journeyView == null) {
                journeyView = getLayoutInflater().inflate(R.layout.car_lists, parent, false);
            }

            // Find the current car to work with
            Journey currentJourney = ctm.journeyCollection.getSavedJourneys().get(position);
            int transportTypeJourney = currentJourney.getTransportTaken().getTransportType();

            ImageView imageView = (ImageView) journeyView.findViewById(R.id.image_icon);
            switch (transportTypeJourney) {
                case TRANSPORT_TYPES.CAR:
                    imageView.setImageResource(currentJourney.getCarDriven().getIconId());
                    break;
                case TRANSPORT_TYPES.WALKING_OR_CYCLING:
                    imageView.setImageResource(R.drawable.icycle);
                    break;
                case TRANSPORT_TYPES.BUS:
                    imageView.setImageResource(R.drawable.ibus);
                    break;
                case TRANSPORT_TYPES.SKYTRAIN:
                    imageView.setImageResource(R.drawable.itrain);
                    break;
            }

            TextView descriptionText = (TextView) journeyView.findViewById(R.id.text_in_car_lists);
            descriptionText.setText(currentJourney.getDescription());

            return journeyView;
        }
    }


    public void changeDate(int year, int month, int day) {
        date = new GregorianCalendar(year, month, day);
    }

    public void updateDate(int position) {
        Journey current = ctm.journeyCollection.getSavedJourneys().get(position);

        //edit the database, when the user also edits the journey.
        current.setDate(date);
        SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);

        Log.i("DEBUG", "transportType: " + current.getTransportTaken().getTransportType());
        Log.i("DEBUG", "getCarPosition: " + current.getCarPosition());
        Log.i("DEBUG", "getRoutePosition: " + current.getRoutePosition());
        Log.i("DEBUG", "Date: " + form.format(date.getTime()));

        String dateFormatted = form.format(date.getTime());
        ctm.updateJourneyToDatabaseEditCalendar(current, dateFormatted, this);

        populateListView();
    }

}