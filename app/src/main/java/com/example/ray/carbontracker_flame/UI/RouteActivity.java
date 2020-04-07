package com.example.ray.carbontracker_flame.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.example.ray.carbontracker_flame.Model.Bus;
import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
//import com.example.ray.carbontracker_flame.Model.DBJourneyAdapter;
//import com.example.ray.carbontracker_flame.Model.DBRouteAdapter;
import com.example.ray.carbontracker_flame.Model.Route;
import com.example.ray.carbontracker_flame.Model.SkyTrain;
import com.example.ray.carbontracker_flame.Model.TRANSPORT_TYPES;
import com.example.ray.carbontracker_flame.Model.WalkingAsTransport;
import com.example.ray.carbontracker_flame.R;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * The RouteActivity shows a list of routes
 */

public class RouteActivity extends AppCompatActivity {

    public static final String KEY_EDIT = "Edit";
    public static final String KEY_DELETE = "Delete";
    public static final String TAG_CAR_POSITION_SELECTED = "Car Position";
    public static final String ADD_DATE_TAG = "add date";
    public static int todaysJourneyCount;
    private static final int ADD_ROUTE_CODE = 0;
    private static final int EDIT_ROUTE_CODE = 1;
    private static final int DELETE_ROUTE_CODE = 2;
    public Calendar date;
    CarbonTrackerModel ctm;
    private int SELECTED_ROUTE_POS;
    private int indexCarSelected = -1;
    private int transportSelected = -1;

    public static Intent makeIntent(Context context) {
        return new Intent(context, RouteActivity.class);
    }

    public static Intent makeRouteActivityIntent(Context context) {
        return new Intent(context, RouteActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.routes));
        }
        ctm = CarbonTrackerModel.getInstance();
        try {
            getBundleFromTransport();
        } catch (Exception e) {
            e.printStackTrace();
        }
        createActionBar();
        populateListView();
        setupAddRouteButton();
        setupContextMenu();
        registerClickCallBack();
    }

    private void createActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            String ERROR_MESSAGE = e.getMessage();
            Log.i("RouteActivity", ERROR_MESSAGE);
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

    private void getBundleFromTransport() throws Exception {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int transportType = bundle.getInt(SelectTransportationModeActivity.TAG_TRANSPORT_TYPE_SELECTED);
        switch (transportType) {
            case TRANSPORT_TYPES.CAR:
                indexCarSelected = bundle.getInt(TAG_CAR_POSITION_SELECTED);
                transportSelected = TRANSPORT_TYPES.CAR;
                // Toast.makeText(this, "CAR selected", Toast.LENGTH_SHORT).show();
                break;
            case TRANSPORT_TYPES.WALKING_OR_CYCLING:
                transportSelected = TRANSPORT_TYPES.WALKING_OR_CYCLING;
                // Toast.makeText(this, "WALKING_OR_CYCLING selected", Toast.LENGTH_SHORT).show();
                break;
            case TRANSPORT_TYPES.BUS:
                transportSelected = TRANSPORT_TYPES.BUS;
                //Toast.makeText(this, "BUS selected", Toast.LENGTH_SHORT).show();
                break;
            case TRANSPORT_TYPES.SKYTRAIN:
                transportSelected = TRANSPORT_TYPES.SKYTRAIN;
                // Toast.makeText(this, "SKYTRAIN selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new Exception("Invalid entry point to this activity used");
        }
    }

    private void registerClickCallBack() {
        ListView list = (ListView) findViewById(R.id.lstRoutes);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //Start intent and pass in the position of the route selected
                //IMPORTANT, this index may not map 1 to 1 to the routesSaved list,
                // the shown list does not display Routes where isSelectableOnMenu is false;
                SELECTED_ROUTE_POS = convertAdapterIndexToRoutesIndex(position);
                DialogFragment dialogFragment = new DatePickerFragmentAddBillActivity();
                dialogFragment.show(getSupportFragmentManager(), ADD_DATE_TAG);

                //add the appropriate transport to the new journey
                // TRANSPORTATION TYPE, indexCarSelected (if it is not car, put -1), indexRoute, Date.
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.selectOptions2);
        menu.add(0, v.getId(), 0, KEY_EDIT);
        menu.add(0, v.getId(), 0, KEY_DELETE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == KEY_EDIT) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = convertAdapterIndexToRoutesIndex(info.position);

            Intent intent = new Intent(RouteActivity.this, EditRouteActivity.class);
            Bundle extras = new Bundle();
            extras.putInt("Route Position", position);
            intent.putExtras(extras);
            startActivityForResult(intent, EDIT_ROUTE_CODE);
        } else if (item.getTitle() == KEY_DELETE) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int positionInList = convertAdapterIndexToRoutesIndex(info.position);
            List<Route> routeList = ctm.getRouteCollection().getSavedRoutes();
            Route current = routeList.get(positionInList);

            // Update the selectable Route in database by pointing the selectable to false.
            // The way to delete the row is by updating the selectable to false.
            ctm.updateRouteInDatabase(positionInList + 1, current, this, false);

            ctm.getRouteCollection().deleteRoute(current);
            populateListView();

        } else {
            return false;
        }
        return true;
    }

    private int convertAdapterIndexToRoutesIndex(int adapterPosition) {
        List<Route> routeList = ctm.getRouteCollection().getSavedRoutes();
        int count = 0;
        int index = 0;
        while (count <= adapterPosition) {
            if (routeList.get(index).isSelectableOnMenu()) count++;
            index++;
        }
        index--;
        return index;
    }

    private void populateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, //context for the activity
                R.layout.route_list_view_layout, //Layout to use(create)
                ctm.getRouteCollection().getRouteDescriptions()); // Items to be displayed
        ListView list = (ListView) findViewById(R.id.lstRoutes);
        list.setAdapter(adapter);
    }

    private void setupAddRouteButton() {
        Button btn = (Button) findViewById(R.id.btnAddRoute);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AddRouteActivity.makeIntent(RouteActivity.this), ADD_ROUTE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_ROUTE_CODE:
                    break;
                case EDIT_ROUTE_CODE:
                    break;
                case DELETE_ROUTE_CODE:
                    break;
                default:
                    break;
            }
        }
        populateListView();
    }

    private void setupContextMenu() {
        ListView list = (ListView) findViewById(R.id.lstRoutes);
        registerForContextMenu(list);
    }

//    private void openRouteDatabase() {
//        routeDbAdapter = new DBRouteAdapter(this);
//        routeDbAdapter.open();
//    }

//    private void openJourneyDatabase() {
//        journeyDbAdapter = new DBJourneyAdapter(this);
//        journeyDbAdapter.open();
//    }

//    private void closeJourneyDatabase() {
//        journeyDbAdapter.close();
//    }

    public Calendar changeDate(int year, int month, int day) {
        date = new GregorianCalendar(year, month, day);
        return date;
    }

    public void saveJourney(Calendar selectedDate) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
        date = selectedDate;
        String dateFormatted = form.format(date.getTime());
        int uniqueIdJourney = ctm.getCounterForJourneyId();

        ctm.addJourneyToDatabase(transportSelected, indexCarSelected,
                SELECTED_ROUTE_POS, dateFormatted, this);

        Route route_selected = ctm.getRouteCollection().getSavedRoutes().get(SELECTED_ROUTE_POS);
        switch (transportSelected) {
            case TRANSPORT_TYPES.CAR:
                ctm.journeyCollection.addJourney(ctm.carCollection.getSavedCars().get(indexCarSelected), route_selected, date, uniqueIdJourney);
                break;
            case TRANSPORT_TYPES.WALKING_OR_CYCLING:
                ctm.journeyCollection.addJourney(new WalkingAsTransport(), route_selected, date, uniqueIdJourney);
                break;
            case TRANSPORT_TYPES.BUS:
                ctm.journeyCollection.addJourney(new Bus(), route_selected, date, uniqueIdJourney);
                break;
            case TRANSPORT_TYPES.SKYTRAIN:
                ctm.journeyCollection.addJourney(new SkyTrain(), route_selected, date, uniqueIdJourney);
                break;
        }
        addCarAndRoutePositionToJourney();
        getTodaysJourneyCount();
        incrementTodaysJourneyCount();
        Toast.makeText(RouteActivity.this, getString(R.string.journeyCreatedOn) + form.format(date.getTime()), Toast.LENGTH_SHORT).show();

        addAndSaveCounterJourneyId();
        // closing journey database
//        closeJourneyDatabase();
        startActivity(DisplayEmissions.makeIntent(RouteActivity.this));
    }

    private void addCarAndRoutePositionToJourney() {
        int lastElement = ctm.journeyCollection.getSavedJourneys().size() - 1;
        Log.i("TEST_AFTER_SWITCH", "indexCar: " + indexCarSelected);
        Log.i("TEST_AFTER_SWITCH", "indexRoute: " + SELECTED_ROUTE_POS);
        ctm.journeyCollection.getSavedJourneys().get(lastElement).setCarPosition(indexCarSelected);
        ctm.journeyCollection.getSavedJourneys().get(lastElement).setRoutePosition(SELECTED_ROUTE_POS);
    }

    private void addAndSaveCounterJourneyId() {
        // add the journey ID because we are creating a new journey.
        ctm.addCounterForJourneyId();
        SharedPreferences prefs = getSharedPreferences(WelcomeActivity.SHAREDPREF_JOURNEY, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(WelcomeActivity.SHAREDPREF_ITEM_COUNTERJOURNEY, ctm.getCounterForJourneyId());
        edit.apply();
    }

    private void incrementTodaysJourneyCount() {
        SharedPreferences setting = getSharedPreferences(NotificationActivity.SHAREDPREF_NOTI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt(NotificationActivity.SHAREDPREF_TODAYS_JOURNEYCOUNT, todaysJourneyCount + 1);
        editor.apply();
    }

    private void getTodaysJourneyCount() {
        SharedPreferences setting = getSharedPreferences(NotificationActivity.SHAREDPREF_NOTI, Context.MODE_PRIVATE);
        todaysJourneyCount = setting.getInt(NotificationActivity.SHAREDPREF_TODAYS_JOURNEYCOUNT, 0);
    }

}
