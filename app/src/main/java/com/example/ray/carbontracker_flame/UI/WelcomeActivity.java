package com.example.ray.carbontracker_flame.UI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ray.carbontracker_flame.Model.Bus;
import com.example.ray.carbontracker_flame.Model.Car;
import com.example.ray.carbontracker_flame.Model.CarData;
import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
import com.example.ray.carbontracker_flame.Model.DBAdapter;
import com.example.ray.carbontracker_flame.Model.HydroBill;
import com.example.ray.carbontracker_flame.Model.Journey;
import com.example.ray.carbontracker_flame.Model.NaturalGasBill;
import com.example.ray.carbontracker_flame.Model.Route;
import com.example.ray.carbontracker_flame.Model.SkyTrain;
import com.example.ray.carbontracker_flame.Model.TRANSPORT_TYPES;
import com.example.ray.carbontracker_flame.Model.UtilityBill;
import com.example.ray.carbontracker_flame.Model.WalkingAsTransport;
import com.example.ray.carbontracker_flame.R;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * The WelcomeActivity adds functionality to the Welcome Screen
 */

public class WelcomeActivity extends AppCompatActivity {
    public static final String SHAREDPREF_JOURNEY = "Journey Id";
    public static final String SHAREDPREF_ITEM_COUNTERJOURNEY = "counterJourneyId";
    public static final String SHAREDPREF_BILL = "BillId";
    public static final String SHAREDPREF_ITEM_COUNTERBILL = "counterBillId";
    public static final String SHAREDPREF_TYPE = "TypeId";
    public static final String SHAREDPREF_ITEM_BOOLEANTYPE = "booleanTypeId";

    public static final String SHAREDPREF_NOTI = "Notification SP";
    public static final String SHAREDPREF_TODAYS_JOURNEYCOUNT = "todaysJourneyCount";
    public static final String SHAREDPREF_LAST_BILLDATE = "lastBillDate";
    public static int todaysJourneyCount;
    public static long lastBillDate;

    public static final int DEFAULT_VALUE_FOR_ID = 1;
    public static final boolean DEFAULT_VALUE_FOR_TYPE = false;
    private CarbonTrackerModel ctm;
    // You need to manually change this
    // 1. If you reset the database version change the value "isDatabaseVersionReset " into true
    // 2. Run the App
    // 3. Please change the boolean "isDatabaseVersionReset" into false again.
    // Default value of isDatabaseVersionReset is false. This value when pushed should be FALSE!
    private boolean isDatabaseVersionReset = false;

    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        openDatabase();

        ctm = CarbonTrackerModel.getInstance();
        ctm.loadCarDataFromDisk(this);
        getLastCounterJourneyId();
        getLastCounterBillId();
        getLastBooleanTypeRelatableUnit();

        setupMainMenuButton();
        displayCarDBRecordSet();
        displayRouteDBRecordSet();
        displayJourneyDBRecordSet();
        displayBillDBRecordSet();
        closeDatabase();

    }

    private void openDatabase() {
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
    }

    private void setupMainMenuButton() {
        Button btn = (Button) findViewById(R.id.btnGetStarted);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(NotificationActivity.makeIntent(WelcomeActivity.this));
                finish();
            }
        });
    }

    private void displayCarDBRecordSet() {
        Cursor cursorCar = dbAdapter.getAllRowsCar();

        if (cursorCar.moveToFirst()) {
            do {
                // int id = cursorCar.getInt(DBAdapter.COL_ROWID_CAR);
                // Log.i("ID_CAR", "id: " + id);
                String name = cursorCar.getString(DBAdapter.COL_CAR_NAME);
                String make = cursorCar.getString(DBAdapter.COL_CAR_MAKE);
                String model = cursorCar.getString(DBAdapter.COL_CAR_MODEL);
                int year = cursorCar.getInt(DBAdapter.COL_CAR_YEAR);
                double displ = cursorCar.getDouble(DBAdapter.COL_CAR_DISPL);
                String trans = cursorCar.getString(DBAdapter.COL_CAR_TRANS);
                int cityMPG = cursorCar.getInt(DBAdapter.COL_CAR_CMPG);
                int hwyMPG = cursorCar.getInt(DBAdapter.COL_CAR_HMPG);
                String fuelType = cursorCar.getString(DBAdapter.COL_CAR_FUEL);
                int iconId = cursorCar.getInt(DBAdapter.COL_CAR_ICONID);
                int isRemoved = cursorCar.getInt(DBAdapter.COL_CAR_SELECTABLE);

                CarData savedCarData = new CarData(
                        make, model, year,
                        displ, trans, cityMPG
                        , hwyMPG, fuelType
                );

                Car savedCar = new Car(name, savedCarData);
                ctm.carCollection.getSavedCars().add(savedCar);
                savedCar.setImageId(iconId);

                if (isRemoved == 0) {
                    ctm.carCollection.deleteCar(ctm.carCollection.getSavedCars().get(ctm.carCollection.getSavedCars().size() - 1));
                }

            } while (cursorCar.moveToNext());
        }
        cursorCar.close();
    }

    private void displayRouteDBRecordSet() {
        Cursor cursorRoute = dbAdapter.getAllRowsRoute();
        if (cursorRoute.moveToFirst()) {
            do {
                // int id = cursorRoute.getInt(DBAdapter.COL_ROWID_ROUTE);
                String routeName = cursorRoute.getString(DBAdapter.COL_ROUTE_NAME);
                double numOfCityKilometers = cursorRoute.getDouble(DBAdapter.COL_ROUTE_CITYKM);
                double numOfHwyKilometers = cursorRoute.getDouble(DBAdapter.COL_ROUTE_HWYKM);
                int isRemoved = cursorRoute.getInt(DBAdapter.COL_ROUTE_SELECTABLE);

                Route route;
                if (isRemoved == 1) {
                    route = new Route(routeName, numOfCityKilometers,
                            numOfHwyKilometers, true);
                } else {
                    route = new Route(routeName, numOfCityKilometers,
                            numOfHwyKilometers, false);
                }
                ctm.getRouteCollection().addRoute(route);

            } while (cursorRoute.moveToNext());
        }
        cursorRoute.close();
    }

    private void displayJourneyDBRecordSet() {
        Cursor cursorJourney = dbAdapter.getAllRowsJourney();
        if (cursorJourney.moveToFirst()) {
            do {
                int journeyId = cursorJourney.getInt(DBAdapter.COL_ROWID_JOURNEY);

                int transportationType = cursorJourney.getInt(DBAdapter.COL_JOURNEY_TRANSPORT_TYPE);

                int indexCarSelected = cursorJourney.getInt(DBAdapter.COL_JOURNEY_INDEX_CAR);

                int indexRouteSelected = cursorJourney.getInt(DBAdapter.COL_JOURNEY_INDEX_ROUTE);

                String date = cursorJourney.getString(DBAdapter.COL_JOURNEY_DATE);
                Calendar journeyCalendar = getCalendarFromDate(date);

                Car carJourney;
                Route routeJourney;
                Journey journeySaved;
                switch (transportationType) {
                    case TRANSPORT_TYPES.CAR:
                        carJourney = ctm.carCollection.getSavedCars().get(indexCarSelected);
                        routeJourney = ctm.getRouteCollection().getSavedRoutes().get(indexRouteSelected);
                        journeySaved = new Journey(carJourney,
                                routeJourney, journeyCalendar);
                        journeySaved.setJourneyId(journeyId);

                        ctm.journeyCollection.getSavedJourneys().add(journeySaved);
                        break;
                    case TRANSPORT_TYPES.WALKING_OR_CYCLING:
                        routeJourney = ctm.getRouteCollection().getSavedRoutes().get(indexRouteSelected);
                        journeySaved = new Journey(new WalkingAsTransport(),
                                routeJourney, journeyCalendar);
                        journeySaved.setJourneyId(journeyId);

                        ctm.journeyCollection.getSavedJourneys().add(journeySaved);
                        break;
                    case TRANSPORT_TYPES.BUS:
                        routeJourney = ctm.getRouteCollection().getSavedRoutes().get(indexRouteSelected);
                        journeySaved = new Journey(new Bus(),
                                routeJourney, journeyCalendar);
                        journeySaved.setJourneyId(journeyId);

                        ctm.journeyCollection.getSavedJourneys().add(journeySaved);
                        break;
                    case TRANSPORT_TYPES.SKYTRAIN:
                        routeJourney = ctm.getRouteCollection().getSavedRoutes().get(indexRouteSelected);
                        journeySaved = new Journey(new SkyTrain(),
                                routeJourney, journeyCalendar);
                        journeySaved.setJourneyId(journeyId);

                        ctm.journeyCollection.getSavedJourneys().add(journeySaved);
                        break;
                }
                addCarAndRoutePositionToJourney(indexCarSelected, indexRouteSelected);
            } while (cursorJourney.moveToNext());
        }
        cursorJourney.close();
        // Try and see if there is any error.
        if (isDatabaseVersionReset) {
            resetCounterForJourneyId();
        }
    }

    private void displayBillDBRecordSet() {
        Cursor cursorBill = dbAdapter.getAllRowsBill();
        if (cursorBill.moveToFirst()) {
            do {
                int billId = cursorBill.getInt(DBAdapter.COL_ROWID_BILLS);
                String startDate = cursorBill.getString(DBAdapter.COL_BILL_START_DATE);
                Calendar start = getCalendarFromDate(startDate);
                String endDate = cursorBill.getString(DBAdapter.COL_BILL_END_DATE);
                Calendar end = getCalendarFromDate(endDate);
                double usersEmissionInKg = cursorBill.getDouble(DBAdapter.COL_BILL_EMISSIONS);
                String type = cursorBill.getString(DBAdapter.COL_BILL_TYPE);
                double usageInKWH = cursorBill.getDouble(DBAdapter.COL_BILL_USAGEKWH);
                int numOfPeopleInHome = cursorBill.getInt(DBAdapter.COL_BILL_NUMPEOPLE);

                UtilityBill utilityBill;
                if (type.equals(UtilityBill.BILL_TYPE.HYDRO.toString())) {
                    utilityBill = new HydroBill(start, end, usersEmissionInKg,
                            usageInKWH, numOfPeopleInHome);
                    ctm.getUtilityBillCollection().addBill(utilityBill, billId);
                } else if (type.equals(UtilityBill.BILL_TYPE.NATURAL_GAS.toString())) {
                    utilityBill = new NaturalGasBill(start, end, usersEmissionInKg,
                            usageInKWH, numOfPeopleInHome);
                    ctm.getUtilityBillCollection().addBill(utilityBill, billId);
                } else {
                    Log.wtf("WelcomeActivity", "Error Loading");
                }

            } while (cursorBill.moveToNext());
        }
        cursorBill.close();

        // Try and see if there is any error.
        if (isDatabaseVersionReset) {
            resetCounterBillId();
        }
    }

    private void addCarAndRoutePositionToJourney(int indexCar, int indexRoute) {
        int lastAddedElement = ctm.journeyCollection.getSavedJourneys().size() - 1;
        ctm.journeyCollection.getSavedJourneys().get(lastAddedElement).setCarPosition(indexCar);
        ctm.journeyCollection.getSavedJourneys().get(lastAddedElement).setRoutePosition(indexRoute);
    }

    private Calendar getCalendarFromDate(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] dateFormatted = date.split("/");
        calendar.set(Integer.parseInt(dateFormatted[0]),
                Integer.parseInt(dateFormatted[1]) - 1, // month starts from 0 - 11, January is 0.
                Integer.parseInt(dateFormatted[2]));

        return calendar;
    }

    private void getLastCounterJourneyId() {
        SharedPreferences prefs = getSharedPreferences(SHAREDPREF_JOURNEY, MODE_PRIVATE);
        int lastCounterJourneyId = prefs.getInt(SHAREDPREF_ITEM_COUNTERJOURNEY, DEFAULT_VALUE_FOR_ID);
        Log.i("GET_COUNTER", "CounterJourneyId: " + lastCounterJourneyId);
        ctm.setCounterForJourneyId(lastCounterJourneyId);
    }

    private void getLastCounterBillId() {
        SharedPreferences prefs = getSharedPreferences(SHAREDPREF_BILL, MODE_PRIVATE);
        int lastBillCounter = prefs.getInt(SHAREDPREF_ITEM_COUNTERBILL, DEFAULT_VALUE_FOR_ID);
        Log.i("GET_COUNTER", "CounterBillId: " + lastBillCounter);
        ctm.setCounterForBillId(lastBillCounter);
    }

    private void getLastBooleanTypeRelatableUnit() {
        SharedPreferences prefs = getSharedPreferences(SHAREDPREF_TYPE, MODE_PRIVATE);
        boolean lastSavedType = prefs.getBoolean(SHAREDPREF_ITEM_BOOLEANTYPE, DEFAULT_VALUE_FOR_TYPE);
        Log.i("GET_COUNTER", "lastSavedType: " + lastSavedType);
        ctm.setUsingRelatableUnit(lastSavedType);
    }

    private void resetCounterForJourneyId() {
        Log.i("LOG", "Reset counter bill is called, it is assumed that the journey list is empty or has just been reset");
        ctm.resetCounterForJourneyId();
        SharedPreferences prefs = getSharedPreferences(SHAREDPREF_JOURNEY, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(WelcomeActivity.SHAREDPREF_ITEM_COUNTERJOURNEY, ctm.getCounterForJourneyId());
        edit.apply();
    }

    private void resetCounterBillId() {
        Log.i("LOG", "Reset counter bill is called, it is assumed that the bill list is empty or has just been reset");
        ctm.resetCounterForBillId();
        SharedPreferences prefs = getSharedPreferences(SHAREDPREF_BILL, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(SHAREDPREF_ITEM_COUNTERBILL, ctm.getCounterForBillId());
        edit.apply();
    }

    private void closeDatabase() {
        dbAdapter.close();
    }
}
