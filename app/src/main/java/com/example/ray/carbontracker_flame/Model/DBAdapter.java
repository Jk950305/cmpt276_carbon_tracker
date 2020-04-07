package com.example.ray.carbontracker_flame.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBAdapter provides functionality with saving Car, Route, Journey, and Bill data to the database
 */

public class DBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // constant name for Car
    // DB Fields Car
    public static final String KEY_ROWID_CAR = "_idCars";
    public static final String KEY_CAR_NAME = "carNickname";
    public static final String KEY_CAR_MAKE = "carMake";
    public static final String KEY_CAR_MODEL = "carModel";
    public static final String KEY_CAR_YEAR = "carYear";
    public static final String KEY_CAR_DISPL = "carDispl"; // displacement in liters
    public static final String KEY_CAR_TRANS = "carTrans"; // transmission
    public static final String KEY_CAR_CMPG = "carMPGCity"; // cityMPG
    public static final String KEY_CAR_HMPG = "carMPGHwy"; // highwayMPG
    public static final String KEY_CAR_FUEL = "carFuelType"; // fuelType
    public static final String KEY_CAR_ICONID = "carIconId";
    public static final String KEY_CAR_SELECTABLE = "carSelectable"; // true or false, is the car removed?

    // ID Array for Cars
    public static final int COL_ROWID_CAR = 0;
    public static final int COL_CAR_NAME = 1;
    public static final int COL_CAR_MAKE = 2;
    public static final int COL_CAR_MODEL = 3;
    public static final int COL_CAR_YEAR = 4;
    public static final int COL_CAR_DISPL = 5;
    public static final int COL_CAR_TRANS = 6;
    public static final int COL_CAR_CMPG = 7;
    public static final int COL_CAR_HMPG = 8;
    public static final int COL_CAR_FUEL = 9;
    public static final int COL_CAR_ICONID = 10;
    public static final int COL_CAR_SELECTABLE = 11;

    // constant name for Route
    // DB Fields Route
    public static final String KEY_ROWID_ROUTE = "_idRoutes";
    public static final String KEY_ROUTE_NAME = "routeName";
    public static final String KEY_ROUTE_CITYKM = "cityKM";
    public static final String KEY_ROUTE_HWYKM = "highwayKM";
    public static final String KEY_ROUTE_SELECTABLE = "routeSelectable";

    // ID Array for Routes
    public static final int COL_ROWID_ROUTE = 0;
    public static final int COL_ROUTE_NAME = 1;
    public static final int COL_ROUTE_CITYKM = 2;
    public static final int COL_ROUTE_HWYKM = 3;
    public static final int COL_ROUTE_SELECTABLE = 4;


    // constant name for Journey
    // DB Fields Journey
    public static final String KEY_ROWID_JOURNEY = "_idJourneys";
    public static final String KEY_JOURNEY_TRANSPORT_TYPE = "journeyName";
    public static final String KEY_JOURNEY_INDEX_CAR = "journeyIndCar";
    public static final String KEY_JOURNEY_INDEX_ROUTE = "journeyIndRoute";
    public static final String KEY_JOURNEY_DATE = "journeyDate";


    // ID Array for Journeys
    public static final int COL_ROWID_JOURNEY = 0;
    public static final int COL_JOURNEY_TRANSPORT_TYPE = 1;
    public static final int COL_JOURNEY_INDEX_CAR = 2;
    public static final int COL_JOURNEY_INDEX_ROUTE = 3;
    public static final int COL_JOURNEY_DATE = 4;

    // constant name for Bills
    // DB Fields Bills
    public static final String KEY_ROWID_BILLS = "_idBills";
    public static final String KEY_BILL_START_DATE = "billStDay";
    public static final String KEY_BILL_END_DATE = "billEndDay";
    public static final String KEY_BILL_EMISSIONS = "usersEmissionsInKG";
    public static final String KEY_BILL_TYPE = "billType";
    public static final String KEY_BILL_USAGEKWH = "billUsageInKWH";
    public static final String KEY_BILL_NUMOFPEOPLE = "billNumberOfPeople";

    // ID Array for Bills
    public static final int COL_ROWID_BILLS = 0;
    public static final int COL_BILL_START_DATE = 1;
    public static final int COL_BILL_END_DATE = 2;
    public static final int COL_BILL_EMISSIONS = 3;
    public static final int COL_BILL_TYPE = 4;
    public static final int COL_BILL_USAGEKWH = 5;
    public static final int COL_BILL_NUMPEOPLE = 6;

    // for Cars
    public static final String[] ALL_KEYS_CARS = new String[]
            {KEY_ROWID_CAR,
                    KEY_CAR_NAME, KEY_CAR_MAKE, KEY_CAR_MODEL, KEY_CAR_YEAR,
                    KEY_CAR_DISPL, KEY_CAR_TRANS, KEY_CAR_CMPG, KEY_CAR_HMPG,
                    KEY_CAR_FUEL, KEY_CAR_ICONID, KEY_CAR_SELECTABLE
            };

    // for Routes
    public static final String[] ALL_KEYS_ROUTES = new String[]
            {KEY_ROWID_ROUTE,
                    KEY_ROUTE_NAME,
                    KEY_ROUTE_CITYKM,
                    KEY_ROUTE_HWYKM,
                    KEY_ROUTE_SELECTABLE
            };

    // for Journeys
    public static final String[] ALL_KEYS_JOURNEYS = new String[]
            {KEY_ROWID_JOURNEY,
                    KEY_JOURNEY_TRANSPORT_TYPE,
                    KEY_JOURNEY_INDEX_CAR, KEY_JOURNEY_INDEX_ROUTE,
                    KEY_JOURNEY_DATE
            };

    // for Bills
    public static final String[] ALL_KEYS_BILLS = new String[]
            {KEY_ROWID_BILLS,
                    KEY_BILL_START_DATE, KEY_BILL_END_DATE, KEY_BILL_EMISSIONS,
                    KEY_BILL_TYPE, KEY_BILL_USAGEKWH, KEY_BILL_NUMOFPEOPLE
            };


    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "savedDatabase";

    // 4 Tables
    public static final String DATABASE_TABLE_CAR = "savedCarTable";
    public static final String DATABASE_TABLE_ROUTE = "savedRouteTable";
    public static final String DATABASE_TABLE_JOURNEY = "savedJourneyTable";
    public static final String DATABASE_TABLE_BILLS = "savedBillsTable";

    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 9;

    // create table for Car
    private static final String DATABASE_CREATE_SQL_CAR =
            "create table " + DATABASE_TABLE_CAR
                    + " (" + KEY_ROWID_CAR + " integer primary key autoincrement, "

                    + KEY_CAR_NAME + " text not null, "
                    + KEY_CAR_MAKE + " text not null, "
                    + KEY_CAR_MODEL + " text not null, "
                    + KEY_CAR_YEAR + " integer not null, "
                    + KEY_CAR_DISPL + " real not null, "
                    + KEY_CAR_TRANS + " text not null, "
                    + KEY_CAR_CMPG + " integer not null, "
                    + KEY_CAR_HMPG + " integer not null, "
                    + KEY_CAR_FUEL + " text not null, "
                    + KEY_CAR_ICONID + " integer not null, "
                    + KEY_CAR_SELECTABLE + " integer not null"

                    // Rest  of creation:
                    + ");";

    // create table for Route
    private static final String DATABASE_CREATE_SQL_ROUTE =
            "create table " + DATABASE_TABLE_ROUTE
                    + " (" + KEY_ROWID_ROUTE + " integer primary key autoincrement, "

                    + KEY_ROUTE_NAME + " text not null, "
                    + KEY_ROUTE_CITYKM + " real not null, "
                    + KEY_ROUTE_HWYKM + " real not null, "
                    + KEY_ROUTE_SELECTABLE + " integer not null"

                    // Rest  of creation:
                    + ");";

    // create table for Journey
    private static final String DATABASE_CREATE_SQL_JOURNEY =
            "create table " + DATABASE_TABLE_JOURNEY
                    + " (" + KEY_ROWID_JOURNEY + " integer primary key autoincrement, "

                    + KEY_JOURNEY_TRANSPORT_TYPE + " integer not null, "
                    + KEY_JOURNEY_INDEX_CAR + " integer not null, "
                    + KEY_JOURNEY_INDEX_ROUTE + " integer not null, "
                    + KEY_JOURNEY_DATE + " text not null"

                    // Rest  of creation:
                    + ");";

    // create table for Bill
    private static final String DATABASE_CREATE_SQL_BILLS =
            "create table " + DATABASE_TABLE_BILLS
                    + " (" + KEY_ROWID_BILLS + " integer primary key autoincrement, "

                    + KEY_BILL_START_DATE + " text not null, "
                    + KEY_BILL_END_DATE + " text not null, "
                    + KEY_BILL_EMISSIONS + " real not null, "
                    + KEY_BILL_TYPE + " text not null, "
                    + KEY_BILL_USAGEKWH + " real not null, "
                    + KEY_BILL_NUMOFPEOPLE + " integer not null"

                    // Rest  of creation:
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database. (Insert Cars)
    public long insertRowCars(String name, String make, String model,
                              int year, double displacementInLiters, String transmission,
                              int cityMPG, int highwayMPG, String fuelType, int iconId,
                              boolean selectable) {
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CAR_NAME, name);
        initialValues.put(KEY_CAR_MAKE, make);
        initialValues.put(KEY_CAR_MODEL, model);
        initialValues.put(KEY_CAR_YEAR, year);
        initialValues.put(KEY_CAR_DISPL, displacementInLiters);
        initialValues.put(KEY_CAR_TRANS, transmission);
        initialValues.put(KEY_CAR_CMPG, cityMPG);
        initialValues.put(KEY_CAR_HMPG, highwayMPG);
        initialValues.put(KEY_CAR_FUEL, fuelType);
        initialValues.put(KEY_CAR_ICONID, iconId);
        initialValues.put(KEY_CAR_SELECTABLE, selectable);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE_CAR, null, initialValues);
    }

    // Add a new set of values to the database. (Insert Routes)
    public long insertRowRoutes(String routeName, double numOfCityKM, double numOfHwyKM,
                                boolean selectable) {
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROUTE_NAME, routeName);
        initialValues.put(KEY_ROUTE_CITYKM, numOfCityKM);
        initialValues.put(KEY_ROUTE_HWYKM, numOfHwyKM);
        initialValues.put(KEY_ROUTE_SELECTABLE, selectable);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE_ROUTE, null, initialValues);
    }

    // Add a new set of values to the database. (Insert Journeys)
    public long insertRowJourneys(int transportationType, int carSelected,
                                  int routeSelected, String date) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_JOURNEY_TRANSPORT_TYPE, transportationType);
        initialValues.put(KEY_JOURNEY_INDEX_CAR, carSelected);
        initialValues.put(KEY_JOURNEY_INDEX_ROUTE, routeSelected);
        initialValues.put(KEY_JOURNEY_DATE, date);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE_JOURNEY, null, initialValues);
    }

    // Add a new set of values to the database. (Insert Bills)
    public long insertRowBills(String startDate, String endDate, double usersEmissionsInKG,
                               String billType, double usageInKWH, int numOfPeople) {

        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_BILL_START_DATE, startDate);
        initialValues.put(KEY_BILL_END_DATE, endDate);
        initialValues.put(KEY_BILL_EMISSIONS, usersEmissionsInKG);
        initialValues.put(KEY_BILL_TYPE, billType);
        initialValues.put(KEY_BILL_USAGEKWH, usageInKWH);
        initialValues.put(KEY_BILL_NUMOFPEOPLE, numOfPeople);

        // Insert it into the database.
        return db.insert(DATABASE_TABLE_BILLS, null, initialValues);
    }

    // Delete a row from the Car Table's database, by rowId (primary key)
    // For Cars
    public boolean deleteRowCar(long rowId) {
        String where = KEY_ROWID_CAR + "=" + rowId;
        return db.delete(DATABASE_TABLE_CAR, where, null) != 0;
    }

    // Delete a row from the Route Table's database, by rowId (primary key)
    // For Routes
    public boolean deleteRowRoute(long rowId) {
        String where = KEY_ROWID_ROUTE + "=" + rowId;
        return db.delete(DATABASE_TABLE_ROUTE, where, null) != 0;
    }

    // Delete a row from the Journey Table's database, by rowId (primary key)
    // For Journeys
    public boolean deleteRowJourney(long rowId) {
        String where = KEY_ROWID_JOURNEY + "=" + rowId;
        return db.delete(DATABASE_TABLE_JOURNEY, where, null) != 0;
    }

    // Delete a row from the Bills Table's database, by rowId (primary key)
    // For Bills
    public boolean deleteRowBill(long rowId) {
        String where = KEY_ROWID_BILLS + "=" + rowId;
        return db.delete(DATABASE_TABLE_BILLS, where, null) != 0;
    }

    // Delete All data in the database
    // For Cars
    public void deleteAllCar() {
        Cursor c = getAllRowsCar();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID_CAR);
        if (c.moveToFirst()) {
            do {
                deleteRowCar(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // For Routes
    public void deleteAllRoute() {
        Cursor c = getAllRowsRoute();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID_ROUTE);
        if (c.moveToFirst()) {
            do {
                deleteRowRoute(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // For Journeys
    public void deleteAllJourney() {
        Cursor c = getAllRowsJourney();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID_JOURNEY);
        if (c.moveToFirst()) {
            do {
                deleteRowJourney(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // For Bills
    public void deleteAllBills() {
        Cursor c = getAllRowsBill();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID_BILLS);
        if (c.moveToFirst()) {
            do {
                deleteRowBill(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    // For Cars
    public Cursor getAllRowsCar() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE_CAR, ALL_KEYS_CARS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // For Routes
    public Cursor getAllRowsRoute() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE_ROUTE, ALL_KEYS_ROUTES,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // For Journey
    public Cursor getAllRowsJourney() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE_JOURNEY, ALL_KEYS_JOURNEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // For Routes
    public Cursor getAllRowsBill() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE_BILLS, ALL_KEYS_BILLS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    // For Cars
    public boolean updateCarRow(long rowId, String name, String make, String model,
                                int year, double displacementInLiters, String transmission,
                                int cityMPG, int highwayMPG, String fuelType, int iconId,
                                boolean selectable) {

        String where = KEY_ROWID_CAR + "=" + rowId;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_CAR_NAME, name);
        newValues.put(KEY_CAR_MAKE, make);
        newValues.put(KEY_CAR_MODEL, model);
        newValues.put(KEY_CAR_YEAR, year);
        newValues.put(KEY_CAR_DISPL, displacementInLiters);
        newValues.put(KEY_CAR_TRANS, transmission);
        newValues.put(KEY_CAR_CMPG, cityMPG);
        newValues.put(KEY_CAR_HMPG, highwayMPG);
        newValues.put(KEY_CAR_FUEL, fuelType);
        newValues.put(KEY_CAR_ICONID, iconId);
        newValues.put(KEY_CAR_SELECTABLE, selectable);

        // Insert it into the database.
        return db.update(DATABASE_TABLE_CAR, newValues, where, null) != 0;
    }

    // For Route
    public boolean updateRouteRow(long rowId,
                                  String routeName, double numOfCityKM,
                                  double numOfHwyKM, boolean selectable) {
        String where = KEY_ROWID_ROUTE + "=" + rowId;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_ROUTE_NAME, routeName);
        newValues.put(KEY_ROUTE_CITYKM, numOfCityKM);
        newValues.put(KEY_ROUTE_HWYKM, numOfHwyKM);
        newValues.put(KEY_ROUTE_SELECTABLE, selectable);

        // Insert it into the database.
        return db.update(DATABASE_TABLE_ROUTE, newValues, where, null) != 0;
    }

    // For Journey
    public boolean updateJourneyRow(long rowId, int transportationType,
                                    int carSelected, int routeSelected,
                                    String date) {
        String where = KEY_ROWID_JOURNEY + "=" + rowId;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_JOURNEY_TRANSPORT_TYPE, transportationType);
        newValues.put(KEY_JOURNEY_INDEX_CAR, carSelected);
        newValues.put(KEY_JOURNEY_INDEX_ROUTE, routeSelected);
        newValues.put(KEY_JOURNEY_DATE, date);

        // Insert it into the database.
        return db.update(DATABASE_TABLE_JOURNEY, newValues, where, null) != 0;
    }

    // For Bills
    public boolean updateBillsRow(long rowId,
                                  String startDate, String endDate, double usersEmissionsInKG,
                                  String billType, double usageInKWH, int numOfPeople) {

        String where = KEY_ROWID_BILLS + "=" + rowId;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_BILL_START_DATE, startDate);
        newValues.put(KEY_BILL_END_DATE, endDate);
        newValues.put(KEY_BILL_EMISSIONS, usersEmissionsInKG);
        newValues.put(KEY_BILL_TYPE, billType);
        newValues.put(KEY_BILL_USAGEKWH, usageInKWH);
        newValues.put(KEY_BILL_NUMOFPEOPLE, numOfPeople);

        // Insert it into the database.
        return db.update(DATABASE_TABLE_BILLS, newValues, where, null) != 0;
    }

    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            // Cars
            _db.execSQL(DATABASE_CREATE_SQL_CAR);
            // Route
            _db.execSQL(DATABASE_CREATE_SQL_ROUTE);
            // Journey
            _db.execSQL(DATABASE_CREATE_SQL_JOURNEY);
            // Bills
            _db.execSQL(DATABASE_CREATE_SQL_BILLS);

        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            // Cars
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_CAR);
            // Route
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ROUTE);
            // Journey
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_JOURNEY);
            // Bills
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_BILLS);


            // Recreate new database:
            onCreate(_db);
        }
    }
}

