package com.example.ray.carbontracker_flame.Model;

import android.content.Context;

import com.example.ray.carbontracker_flame.App;
import com.example.ray.carbontracker_flame.R;

/**
 * The Route represents the distance traveled in city and highway km
 */

public class Route {
    private String routeName;
    private double numOfCityKilometers;
    private double numOfHighWayKilometers;
    private boolean isSelectableOnMenu;
    private CarbonTrackerModel ctm = CarbonTrackerModel.getInstance();
    private Context applicationContext;

    public Route(String routeName, double numOfCityKilometers, double numOfHighWayKilometers, boolean isSelectableOnMenu) {
        this.routeName = routeName;
        this.numOfCityKilometers = numOfCityKilometers;
        this.numOfHighWayKilometers = numOfHighWayKilometers;
        this.isSelectableOnMenu = isSelectableOnMenu;
    }

    public boolean isSelectableOnMenu() {
        return isSelectableOnMenu;
    }

    public void setSelectableOnMenu(boolean selectableOnMenu) {
        isSelectableOnMenu = selectableOnMenu;
    }

    public String getName() {
        return this.routeName;
    }

    public double getNumOfHighWayKilometers() {
        return numOfHighWayKilometers;
    }

    public void setNumOfHighWayKilometers(double numOfHighWayKilometers) {
        this.numOfHighWayKilometers = numOfHighWayKilometers;
    }

    public double getNumOfCityKilometers() {
        return numOfCityKilometers;
    }

    public void setNumOfCityKilometers(double numOfCityKilometers) {
        this.numOfCityKilometers = numOfCityKilometers;
    }

    public String getDescription() {
        applicationContext = App.getInstance();
        return App.getInstance().getString(R.string.route_name_info) + routeName +
                "\n" + applicationContext.getString(R.string.city_km) + numOfCityKilometers +
                "\n" + applicationContext.getString(R.string.highway_km) + numOfHighWayKilometers;
    }
}
