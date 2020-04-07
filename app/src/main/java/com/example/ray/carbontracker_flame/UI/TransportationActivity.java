package com.example.ray.carbontracker_flame.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.example.ray.carbontracker_flame.Model.Car;
import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
import com.example.ray.carbontracker_flame.Model.Journey;
import com.example.ray.carbontracker_flame.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Shows list of transportation
 */

public class TransportationActivity extends AppCompatActivity {

    public static final String KEY_EDIT = "Edit";
    public static final String KEY_DELETE = "Delete";
    public static final int ACTIVITY_CODE_ADD = 1000;
    public static final int ACTIVITY_CODE_EDIT = 1001;
    public static final String TAG_CAR_POSITION_SELECTED = "Car Position";
    static final int INITIALIZE_VALUE = -1;
    private static final String TAG_ACTIVITY_TYPE = "ActivityType";
    private static final String TAG_POSITION_SELECTED = "positionSelected";
    public static Activity firstActivity;
    private CarbonTrackerModel ctm;
    private int journeyActivityCode;
    private int journeyPosition;

    public static Intent makeIntent(Context context) {
        return new Intent(context, TransportationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation);

        firstActivity = this;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.transportation));
        }
        ctm = CarbonTrackerModel.getInstance();

        createActionBar();
        setArgument();
        createAddTransportButton();
        showCarCollection();
        setupContextMenu();
        createTransportClickBehavior();
    }

    private void createActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            String ERROR_MESSAGE = e.getMessage();
            Log.i("TransportationActivity", ERROR_MESSAGE);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        showCarCollection();
    }


    private void createAddTransportButton() {
        Button add_car_btn = (Button) findViewById(R.id.btnAddTransport);
        add_car_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TransportationActivity.this, R.string.addTransportToast,
                        Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(TransportationActivity.this, SelectTransportationModeActivity.class);
                Bundle extras = new Bundle();
                extras.putInt(TAG_ACTIVITY_TYPE, ACTIVITY_CODE_ADD);
                extras.putInt(TAG_POSITION_SELECTED, INITIALIZE_VALUE);
                extras.putInt(JourneyActivity.TAG_JOURNEY_ACTIVITY_TYPE, journeyActivityCode);
                extras.putInt(JourneyActivity.TAG_JOURNEY_POSITION_SELECTED, journeyPosition);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    private void showCarCollection() {
        LinkedList<Car> carListEdited = ctm.getCarSavedCollectionForAdapter();
        ArrayAdapter<Car> carListAdapter = new CarsListAdapter(carListEdited);
        ListView listV = (ListView) findViewById(R.id.car_collection);
        listV.setAdapter(carListAdapter);
    }

    private class CarsListAdapter extends ArrayAdapter<Car> {

        private LinkedList<Car> carListEdited;

        public CarsListAdapter(LinkedList<Car> editedCarList) {
            super(TransportationActivity.this, R.layout.car_lists, editedCarList);
            carListEdited = editedCarList;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View carView = convertView;

            int positionSelected = convertAdapterIndexToRoutesIndex(position);
            Car currentCar = ctm.carCollection.getSavedCars().get(positionSelected);
            if (carView == null) {
                carView = getLayoutInflater().inflate(R.layout.car_lists, parent, false);
            }
            ImageView imageView = (ImageView) carView.findViewById(R.id.image_icon);
            TextView descriptionText = (TextView) carView.findViewById(R.id.text_in_car_lists);
            imageView.setImageResource(currentCar.getIconId());
            descriptionText.setText(currentCar.getCarDescription());

            return carView;
        }
    }

    private void createTransportClickBehavior() {
        ListView carList = (ListView) findViewById(R.id.car_collection);
        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                if (journeyActivityCode == JourneyActivity.ACTIVITY_CODE_ADD_JOURNEY) {
                    Intent intent = new Intent(TransportationActivity.this, RouteActivity.class);
                    Bundle extras = new Bundle();
                    int index = convertAdapterIndexToRoutesIndex(position);
                    extras.putInt(TAG_CAR_POSITION_SELECTED, index);
                    intent.putExtras(extras);
                    startActivity(intent);
                } else if (journeyActivityCode == JourneyActivity.ACTIVITY_CODE_TRANSPORTATION_EDIT) {

                    int index = convertAdapterIndexToRoutesIndex(position);
                    Car car = ctm.carCollection.getSavedCars().get(index);
                    ctm.journeyCollection.getSavedJourneys().get(journeyPosition).editCar(car);

                    SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
                    Calendar date = ctm.journeyCollection.getSavedJourneys().get(journeyPosition).getDate();
                    String dateFormat = form.format(date.getTime());
                    Journey journey = ctm.journeyCollection.getSavedJourneys().get(journeyActivityCode);
                    ctm.updateJourneyToDatabaseEditCar(journey, index, firstActivity);
                    finish();
                }
            }
        });
    }

    private void setupContextMenu() {
        ListView list = (ListView) findViewById(R.id.car_collection);
        registerForContextMenu(list);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(KEY_EDIT);
        menu.add(KEY_DELETE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == KEY_EDIT) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = convertAdapterIndexToRoutesIndex(info.position);
            Intent intent = new Intent(TransportationActivity.this, AddCarActivity.class);
            Bundle extras = new Bundle();
            extras.putInt(TAG_ACTIVITY_TYPE, ACTIVITY_CODE_EDIT);
            extras.putInt(TAG_POSITION_SELECTED, position);
            //add 2 more keys for journeyActivity
            extras.putInt(JourneyActivity.TAG_JOURNEY_ACTIVITY_TYPE, journeyActivityCode);
            extras.putInt(JourneyActivity.TAG_JOURNEY_POSITION_SELECTED, journeyPosition);

            intent.putExtras(extras);
            startActivity(intent);
        } else if (item.getTitle() == KEY_DELETE) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = convertAdapterIndexToRoutesIndex(info.position);

            Log.i("DEBUGGING", "ID that will be deleted:" + (position + 1));

            List<Car> carsList = ctm.carCollection.getSavedCars();
            Car currentCar = carsList.get(position);
            // Update the selectable Car in database by pointing the selectable to false.
            // On the other words, the way to delete the row is by updating the selectable to false.
            // id will be always position + 1, since when the item is deleted. It will just give a boolean false to the CarList.
            ctm.updateCarInDatabase(position + 1, currentCar, this, false);
            ctm.carCollection.deleteCar(currentCar);

            showCarCollection();
        } else {
            return false;
        }
        return true;
    }

    private int convertAdapterIndexToRoutesIndex(int adapterPosition) {
        List<Car> carsList = ctm.carCollection.getSavedCars();
        int count = 0;
        int index = 0;
        Log.i("TAG", "size: " + carsList.size());
        while (count <= adapterPosition) {
            if (carsList.get(index).isSelectableOnMenu()) count++;
            index++;
        }
        index--;
        return index;
    }

}