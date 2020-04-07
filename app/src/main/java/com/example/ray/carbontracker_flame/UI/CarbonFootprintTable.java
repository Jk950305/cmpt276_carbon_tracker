package com.example.ray.carbontracker_flame.UI;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
import com.example.ray.carbontracker_flame.Model.CarbonUnitPrinter;
import com.example.ray.carbontracker_flame.Model.Journey;
import com.example.ray.carbontracker_flame.Model.TRANSPORT_TYPES;
import com.example.ray.carbontracker_flame.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * handles generating a table of journey data
 */

public class CarbonFootprintTable extends Fragment {
    private static String formatDate(Date date) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return months[cal.get(Calendar.MONTH)];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add(getString(R.string.Route));
        columnNames.add(getString(R.string.Date));
        columnNames.add(getString(R.string.transport));
        columnNames.add(getString(R.string.distance));
        columnNames.add(getString(R.string.emissions));
        addJourneyData(columnNames);

        GridLayout gridLayout = (GridLayout) getView().findViewById(R.id.grid_layout);
        for (int i = 0; i < columnNames.size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(columnNames.get(i));
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(0, 0, 0, 2);
            params.setGravity(Gravity.FILL_HORIZONTAL);
            textView.setPadding(0, 50, 70, 50);
            textView.setLayoutParams(params);
            textView.setBackgroundColor(getView().getResources().getColor(android.R.color.white));

            gridLayout.addView(textView);
        }
    }

    private void addJourneyData(ArrayList<String> array) {
        List<Journey> journeys = CarbonTrackerModel.getInstance().journeyCollection.getSavedJourneys();
        for (int i = 0; i < journeys.size(); i++) {
            Journey journey = journeys.get(i);
            //ROUTE
            array.add(journeys.get(i).getRouteTaken().getName());
            //DATE
            array.add(journeys.get(i).getDateToString());
            //TRANSPORT
            if (journeys.get(i).getCarDriven() != null) {
                array.add(journeys.get(i).getCarDriven().getCarNickname());
            } else {
                String transportName = getString(R.string.Other);
                int transportType = journeys.get(i).getTransportTaken().getTransportType();
                switch (transportType) {
                    case TRANSPORT_TYPES.CAR:
                        transportName = journeys.get(i).getCarDriven().getCarNickname();
                        break;
                    case TRANSPORT_TYPES.WALKING_OR_CYCLING:
                        transportName = getString(R.string.Walking);
                        break;
                    case TRANSPORT_TYPES.BUS:
                        transportName = getString(R.string.BUS);
                        break;
                    case TRANSPORT_TYPES.SKYTRAIN:
                        transportName = getString(R.string.SkyTrain);
                        break;
                }
                array.add(transportName);
            }
            //DISTANCE
            array.add(journey.getRouteTaken().getNumOfCityKilometers() +
                    journey.getRouteTaken().getNumOfHighWayKilometers() + "km");
            //EMISSIONS
            array.add(CarbonUnitPrinter.getConvertedNumStringWithUnits(DisplayEmissions.calculateEmissions(journey), 2));
        }
    }
}
