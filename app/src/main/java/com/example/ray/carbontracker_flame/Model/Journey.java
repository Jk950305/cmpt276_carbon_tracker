package com.example.ray.carbontracker_flame.Model;


import com.example.ray.carbontracker_flame.App;
import com.example.ray.carbontracker_flame.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * The Journey class represents a trip with a mode of transportation and route(s)
 */
public class Journey {
    private Transport transportTaken;
    private Car carDriven;
    private Route routeTaken;
    private Calendar date;
    private Calendar createdDate;
    private int journeyId;
    private int routePosition;
    private int carPosition;
    private App appContext;

    //TODO:transition completely to using Transport

    //possibly pass in the new ID later, since journey will need ID
    public Journey(Car carDriven, Route routeTaken, Calendar date) {
        this.transportTaken = carDriven;
        this.carDriven = carDriven;
        this.routeTaken = routeTaken;
        this.date = date;
        this.appContext = App.getInstance();
        this.createdDate = Calendar.getInstance();
    }

    public Journey(Transport transport, Route routeTaken, Calendar date) {
        this.transportTaken = transport;
        this.carDriven = null;
        this.routeTaken = routeTaken;
        this.date = date;
        this.createdDate = Calendar.getInstance();
        this.appContext = App.getInstance();
    }

    public Car getCarDriven() {
        return carDriven;
    }

    public Route getRouteTaken() {
        return routeTaken;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getCarPosition() {
        return carPosition;
    }

    public Calendar getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public void setCarPosition(int carPosition) {
        this.carPosition = carPosition;
    }

    public int getRoutePosition() {
        return routePosition;
    }

    public void setRoutePosition(int routePosition) {
        this.routePosition = routePosition;
    }

    public int getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(int journeyId) {
        this.journeyId = journeyId;
    }

    public String getDateToString() {
        SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
        return "" + form.format(this.date.getTime());
    }

    public String getDescription() {

        return appContext.getString(R.string.vehicle) + getTransportName() +
                "\n" + appContext.getString(R.string.route) + routeTaken.getName() +
                "\n" + appContext.getString(R.string.date) + getDateToString();
    }

    private String getTransportName() {
        int transportType = getTransportTaken().getTransportType();
        switch (transportType) {
            case TRANSPORT_TYPES.CAR:
                return getCarDriven().getCarNickname();
            case TRANSPORT_TYPES.WALKING_OR_CYCLING:
                return appContext.getString(R.string.walk_cylce);
            case TRANSPORT_TYPES.BUS:
                return appContext.getString(R.string.bus_string);
            case TRANSPORT_TYPES.SKYTRAIN:
                return appContext.getString(R.string.skytrain_string);
            default:
                return appContext.getString(R.string.error);
        }
    }

    public Transport getTransportTaken() {
        return transportTaken;
    }

    public double getEmissionsInKG() {
        Route route = getRouteTaken();
        Transport transport = getTransportTaken();

        return (route.getNumOfCityKilometers() * transport.getEmissionsPerCityKMInKG() +
                route.getNumOfHighWayKilometers() * transport.getEmissionsPerHighwayKMInKG());
    }

    public void editTransportation(Transport transportTaken) {
        this.transportTaken = transportTaken;
    }

    public void editCar(Car carDriven) {
        this.carDriven = carDriven;
        this.transportTaken = carDriven;
    }
}
