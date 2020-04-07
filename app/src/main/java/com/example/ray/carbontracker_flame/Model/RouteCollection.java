package com.example.ray.carbontracker_flame.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * UtilityBillCollection encapsulates operations on the saved routes
 */

public class RouteCollection {
    private final LinkedList<Route> savedRoutes;

    public RouteCollection() {
        savedRoutes = new LinkedList<>();
    }

    public void addRoute(Route route) {
        savedRoutes.add(route);
    }

    public void editRoute(Route route, double numOfCityKilometers, double numOfHighWayKilometers,
                          boolean isSelectableOnMenu) {
        route.setNumOfCityKilometers(numOfCityKilometers);
        route.setNumOfHighWayKilometers(numOfHighWayKilometers);
        route.setSelectableOnMenu(isSelectableOnMenu);
    }

    public void deleteRoute(Route route) {
        route.setSelectableOnMenu(false);
    }

    public ArrayList<String> getRouteDescriptions() {
        ArrayList<String> strings = new ArrayList<>();
        for (Route current : getSavedRoutes()) {
            if (current.isSelectableOnMenu()) {
                strings.add(current.getDescription());
            }
        }
        return strings;
    }

    public List<Route> getSavedRoutes() {
        return savedRoutes;
    }
}
