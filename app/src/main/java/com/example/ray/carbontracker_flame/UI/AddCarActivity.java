package com.example.ray.carbontracker_flame.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.carbontracker_flame.Model.Car;
import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
import com.example.ray.carbontracker_flame.R;

import java.util.InputMismatchException;

/**
 * AddCarActivity allows user to add and edit a car object
 */

public class AddCarActivity extends AppCompatActivity {

    public static final String TAG_CAR_NAME_SELECTED = "carName";
    public static final String TAG_CAR_ACTIVITY_TYPE = "ActivityType";
    public static final String TAG_POSITION_SELECTED = "positionSelected";
    public static final String TAG_IMAGEICON_ID = "iconId";
    public static final String TAG_MESSAGE = "AddCarActivity";
    public static final String DIALOG_TEXT = "MessageDialog";

    private Spinner spinner_make;
    private Spinner spinner_model;
    private Spinner spinner_year;
    private Spinner spinner_icons;

    private String[] carIconDescription = {"Sedan 1",
            "Sedan 2", "Sport 1", "Pick-up",
            "SUV", "Smart 1", "Smart 2", "Sport 2"};
    private int[] imageResourceList = {
            R.drawable.icar1,
            R.drawable.icar2,
            R.drawable.icar3,
            R.drawable.icar4,
            R.drawable.icar5,
            R.drawable.icar6,
            R.drawable.icar7,
            R.drawable.icar8};

    private String carNickname = "";

    private String carMakeSelected;
    private String carModelSelected;
    private String carYearSelected;
    private int imageIdSelected;

    private int activityCode;
    private int positionSelected;
    private int journeyActivityCode;
    private int journeyPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.add_car));
        }
        createActionBar();
        initialize();
        setupOKButton();
        setupCancelButton();
        setupSpinnerForMake();
    }

    private void createActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            String ERROR_MESSAGE = e.getMessage();
            Log.i("AddCarActivity", ERROR_MESSAGE);
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

    private void initialize() {
        carMakeSelected = "";
        carModelSelected = "";
        carYearSelected = "";
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        activityCode = extras.getInt(TAG_CAR_ACTIVITY_TYPE);
        positionSelected = extras.getInt(TAG_POSITION_SELECTED);
        journeyActivityCode = extras.getInt(JourneyActivity.TAG_JOURNEY_ACTIVITY_TYPE);
        journeyPosition = extras.getInt(JourneyActivity.TAG_JOURNEY_POSITION_SELECTED);
    }

    private void setupSpinnerForMake() {
        String[] description = CarbonTrackerModel.getInstance().getCarMakes();
        spinner_make = (Spinner) findViewById(R.id.spinner_make);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.carmake_list,
                description);
        spinner_make.setAdapter(adapter);
        spinner_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int posSelected, long id) {
                carMakeSelected = "" + parent.getItemAtPosition(posSelected);
                Log.i(TAG_MESSAGE, "" + parent.getItemAtPosition(posSelected) + " is selected ");
                setupSpinnerForModel(posSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupSpinnerForModel(int makeSelected) {
        String[] descriptionModel = CarbonTrackerModel.getInstance().getCarModelsFromMake(makeSelected);
        spinner_model = (Spinner) findViewById(R.id.spinner_model);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.carmake_list,
                descriptionModel);
        spinner_model.setAdapter(adapter);
        final int makeCarSelected = makeSelected;
        spinner_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int posSelected, long id) {
                carModelSelected = "" + parent.getItemAtPosition(posSelected);
                Log.i(TAG_MESSAGE, "" + parent.getItemAtPosition(posSelected) + " is selected ");
                setupSpinnerForYear(makeCarSelected, posSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupSpinnerForYear(int makeSelected, int modelSelected) {
        String[] descriptionYear = CarbonTrackerModel.getInstance().getCarYearsFromMakeModel(makeSelected, modelSelected);
        spinner_year = (Spinner) findViewById(R.id.spinner_year);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.carmake_list,
                descriptionYear);
        spinner_year.setAdapter(adapter);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int posSelected, long id) {
                carYearSelected = "" + parent.getItemAtPosition(posSelected);
                Log.i(TAG_MESSAGE, "" + parent.getItemAtPosition(posSelected) + " is selected ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        setupIconSpinner();
    }

    private void setupIconSpinner() {
        spinner_icons = (Spinner) findViewById(R.id.spinner_icons);
        ArrayAdapter<String> adapter = new IconsListAdapter();
        spinner_icons.setAdapter(adapter);
        spinner_icons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int posSelected, long id) {
                imageIdSelected = imageResourceList[posSelected]; // this will be used
                Log.i(TAG_MESSAGE, "" + imageIdSelected);
                Log.i(TAG_MESSAGE, "Car's Icon: " + parent.getItemAtPosition(posSelected));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void setupOKButton() {
        final Button okBtn = (Button) findViewById(R.id.btn_addCar_ok);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText car_name = (EditText) findViewById(R.id.edittext_name);
                try {
                    carNickname = car_name.getText().toString().trim();
                    if (carNickname.length() < 1)
                        throw new InputMismatchException();
                    findCarInCarData(carMakeSelected, carModelSelected, carYearSelected);
                    FragmentManager manager = getSupportFragmentManager();
                    CarsListChosenDialog dialog = new CarsListChosenDialog();
                    Bundle arguments = new Bundle();
                    arguments.putString(TAG_CAR_NAME_SELECTED, carNickname);
                    arguments.putInt(TAG_CAR_ACTIVITY_TYPE, activityCode);
                    arguments.putInt(TAG_POSITION_SELECTED, positionSelected);
                    arguments.putInt(TAG_IMAGEICON_ID, imageIdSelected);
                    // add 2 more extras for journey add and edit
                    arguments.putInt(JourneyActivity.TAG_JOURNEY_ACTIVITY_TYPE, journeyActivityCode);
                    arguments.putInt(JourneyActivity.TAG_JOURNEY_POSITION_SELECTED, journeyPosition);
                    CarsListChosenDialog.setArgument(arguments);
                    dialog.show(manager, DIALOG_TEXT);

                } catch (InputMismatchException e) {
                    Toast.makeText(
                            AddCarActivity.this,
                            R.string.please_input_carname,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findCarInCarData(String makeSelected,
                                  String modelSelected,
                                  String yearSelected) {
        CarbonTrackerModel.getInstance().findSelectedOptionCars(
                makeSelected,
                modelSelected,
                yearSelected);
    }

    private void setupCancelButton() {
        Button cancelBtn = (Button) findViewById(R.id.btn_addCar_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddCarActivity.this, R.string.cancel_add_car, Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        });
    }

    private class IconsListAdapter extends ArrayAdapter<String> {

        public IconsListAdapter() {
            super(AddCarActivity.this, R.layout.icon_lists, carIconDescription);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View iconView = convertView;
            if (iconView == null) {
                iconView = getLayoutInflater().inflate(R.layout.icon_lists, parent, false);
            }

            ImageView imageIcon = (ImageView) iconView.findViewById(R.id.choose_image_icon);
            imageIcon.setImageResource(imageResourceList[position]);

            TextView descriptionText = (TextView) iconView.findViewById(R.id.text_iconcars);
            descriptionText.setText(carIconDescription[position]);

            return iconView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }
    }
}