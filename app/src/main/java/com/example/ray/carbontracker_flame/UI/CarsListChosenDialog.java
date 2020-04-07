package com.example.ray.carbontracker_flame.UI;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ray.carbontracker_flame.Model.Car;
import com.example.ray.carbontracker_flame.Model.CarData;
import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
//import com.example.ray.carbontracker_flame.Model.DBCarAdapter;
//import com.example.ray.carbontracker_flame.Model.DBJourneyAdapter;
import com.example.ray.carbontracker_flame.Model.Journey;
import com.example.ray.carbontracker_flame.Model.TRANSPORT_TYPES;
import com.example.ray.carbontracker_flame.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Show the list to choose a specific car if there are multiple types of the Car.
 * It will enter this menu to choose especially for cases when Car's make, model, and year is the same.
 */

public class CarsListChosenDialog extends AppCompatDialogFragment {
    public static final String TAG_CAR_NAME_SELECTED = "carName";
    public static final String TAG_ACTIVITY_TYPE = "ActivityType";
    public static final String TAG_POSITION_SELECTED = "positionSelected";
    public static final String LIST_DIALOG = "CarsListChosenDialog";
    public static final int ACTIVITY_CODE_ADD = 1000;
    public static final int ACTIVITY_CODE_EDIT = 1001;

    private View view;

    private static String carName;
    private static int activityCode;
    private static int positionSelected;
    private static int journeyActivityCode;
    private static int journeyPosition;
    private static int imageIdSelected;

    public static void setArgument(Bundle args) {
        carName = args.getString(TAG_CAR_NAME_SELECTED);
        activityCode = args.getInt(TAG_ACTIVITY_TYPE);
        positionSelected = args.getInt(TAG_POSITION_SELECTED);
        imageIdSelected = args.getInt(AddCarActivity.TAG_IMAGEICON_ID);
        //add 2 more.
        journeyActivityCode = args.getInt(JourneyActivity.TAG_JOURNEY_ACTIVITY_TYPE);
        journeyPosition = args.getInt(JourneyActivity.TAG_JOURNEY_POSITION_SELECTED);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Create the view
        view = LayoutInflater.from(getActivity()).
                inflate(R.layout.choose_car_layout, null);
        initializeTextView();
        showChosenCarsList();
        createCaseWhenCarChosenIsClicked();

        // Build the Choice Dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.please_click)
                .setView(view)
                .create();
    }

    private void initializeTextView() {
        TextView car_make = (TextView) view.findViewById(R.id.make_chosen);
        String message_makes = "" + CarbonTrackerModel.getInstance().getCarsChosenList().get(0).getMake();
        car_make.setText(message_makes);

        TextView car_model = (TextView) view.findViewById(R.id.model_chosen);
        String message_models = "" + CarbonTrackerModel.getInstance().getCarsChosenList().get(0).getModel();
        car_model.setText(message_models);

        TextView car_year = (TextView) view.findViewById(R.id.year_chosen);
        String message_years = "" + CarbonTrackerModel.getInstance().getCarsChosenList().get(0).getYear();
        car_year.setText(message_years);
    }

    private void showChosenCarsList() {
        String[] myChosenCars = CarbonTrackerModel.getInstance().getCarsChosenListDescription();
        ArrayAdapter<String> carChosenAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.car_list,
                myChosenCars);
        ListView list = (ListView) view.findViewById(R.id.chosen_car_list);
        list.setAdapter(carChosenAdapter);
    }

    private void createCaseWhenCarChosenIsClicked() {
        ListView car_chosen_list = (ListView) view.findViewById(R.id.chosen_car_list);
        Log.i("TAG", "journeyActivityCode: " + journeyActivityCode);
        car_chosen_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                if (journeyActivityCode == JourneyActivity.ACTIVITY_CODE_ADD_JOURNEY) {
                    // BIG IF ELSE, IF ACTIVITIYJOURNED ADD OR ACTIVITY JOURNEY EDIT
                    JourneyAddForAddAndEditCar(position);
                    dismiss();
                    (getActivity()).finish();
                    //closeCarDatabase();
                } else if (journeyActivityCode == JourneyActivity.ACTIVITY_CODE_TRANSPORTATION_EDIT) {
                    Log.i("TAG", "Entering Transportation Edit...");
                    JourneyEditForAddAndEditCar(position, journeyPosition);
                }
            }
        });
    }

    private void JourneyEditForAddAndEditCar(int position, int journeyPosition) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
        Calendar date = CarbonTrackerModel.getInstance().journeyCollection.getSavedJourneys().get(journeyPosition).getDate();
        String dateFormat = form.format(date.getTime());

        if (activityCode == ACTIVITY_CODE_ADD) {
            Log.i("TAG", "Entering Transportation Edit, Activity Add...");
            CarData car_data = CarbonTrackerModel.getInstance().getCarsChosenList().get(position);
            Car car_chosen = new Car(carName, car_data);
            car_chosen.setImageId(imageIdSelected);

            // Add Car to the Singleton and to the Database
            CarbonTrackerModel.getInstance().carCollection.addCar(car_chosen, CarbonTrackerModel.getInstance());
            CarbonTrackerModel.getInstance().addCarToDatabase(car_chosen, getActivity());


            // Edit Journey to the Singleton and update the Journey to the Database
            CarbonTrackerModel.getInstance().journeyCollection.getSavedJourneys()
                    .get(journeyPosition)
                    .editCar(car_chosen);

            Journey journeyChosen = CarbonTrackerModel.getInstance().journeyCollection.getSavedJourneys().get(journeyPosition);
            CarbonTrackerModel.getInstance().updateJourneyToDatabaseAddCar(
                    journeyChosen, car_chosen,
                    getActivity());

        } else if (activityCode == ACTIVITY_CODE_EDIT) {
            Log.i("TAG", "Entering Transportation Edit, Activity Edit...");
            CarData car_data = CarbonTrackerModel.getInstance().getCarsChosenList().get(position);
            Car car_chosen = new Car(carName, car_data);
            car_chosen.setImageId(imageIdSelected);

            // Edit Car to the Singleton and update the Car to the Database
            CarbonTrackerModel.getInstance().addCarToDatabase(car_chosen, getActivity());

            CarbonTrackerModel.getInstance().updateCarInDatabase(positionSelected + 1,
                    car_chosen, getActivity(), true);

            // Edit Journey (Car) to the Singleton and Edit the Car to the Database
            CarbonTrackerModel.getInstance().journeyCollection.getSavedJourneys()
                    .get(journeyPosition)
                    .editCar(car_chosen);

            Journey journeySelected = CarbonTrackerModel.getInstance().
                    journeyCollection.getSavedJourneys().get(journeyPosition);

            CarbonTrackerModel.getInstance().updateJourneyToDatabaseEditCar(journeySelected,
                    positionSelected, getActivity());

        } else {
            Log.wtf("Error",
                    "Something really big is error." +
                            " An error that should not happen in the CarsListChosenDialog," +
                            "on the activityCode");
        }
        dismiss();

        (getActivity()).finish();
        TransportationActivity.firstActivity.finish();

    }

    private void JourneyAddForAddAndEditCar(int position) {
        if (activityCode == ACTIVITY_CODE_ADD) {
            CarData car_data = CarbonTrackerModel.getInstance().getCarsChosenList().get(position);
            Car car_chosen = new Car(carName, car_data);
            car_chosen.setImageId(imageIdSelected);

            // Add Car to the Singleton and to the Database
            CarbonTrackerModel.getInstance().carCollection.addCar(car_chosen, CarbonTrackerModel.getInstance());
            CarbonTrackerModel.getInstance().addCarToDatabase(car_chosen, getActivity());


        } else if (activityCode == ACTIVITY_CODE_EDIT) {
            Log.i("TAG", "Entering the Activity Code2");
            CarData car_data = CarbonTrackerModel.getInstance().getCarsChosenList().get(position);
            Car car_chosen = new Car(carName, car_data);
            car_chosen.setImageId(imageIdSelected);

            // Edit the Car in the Singleton and in the Database
            CarbonTrackerModel.getInstance().carCollection.editCar(
                    CarbonTrackerModel.getInstance().carCollection.getSavedCars().get(positionSelected),
                    car_chosen.getCarNickname(),
                    car_chosen.getCarData()
            );

            CarbonTrackerModel.getInstance().updateCarInDatabase(positionSelected + 1,
                    car_chosen, getActivity(), true);

        } else {
            Log.wtf("Error",
                    "Something really big is error." +
                            " An error that should not happen in the CarsListChosenDialog," +
                            "on the activityCode");
        }
    }

}
