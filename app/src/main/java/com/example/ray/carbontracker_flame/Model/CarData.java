package com.example.ray.carbontracker_flame.Model;

import android.content.Context;

import com.example.ray.carbontracker_flame.App;
import com.example.ray.carbontracker_flame.R;

/**
 * CarData holds data about a specific car and its emissions information,
 * including make,model,year,etc.
 */

public class CarData {
    private String make;
    private String model;
    private int year;
    private double displacementInLiters;
    private String transmission;
    private int cityMPG;
    private int highwayMPG;
    private String fuelType;
    private Context applicationContext;

    public CarData(String make, String model, int year, double displacementInLiters, String transmission, int cityMPG, int highwayMPG, String fuelType) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.displacementInLiters = displacementInLiters;
        this.transmission = transmission;
        this.cityMPG = cityMPG;
        this.highwayMPG = highwayMPG;
        this.fuelType = fuelType;
        applicationContext = App.getInstance();
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getDisplacementInLiters() {
        return displacementInLiters;
    }

    public String getTransmission() {
        return transmission;
    }

    @Override
    public String toString() {
        return "CarData{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", displacementInLiters=" + displacementInLiters +
                ", transmission='" + transmission + '\'' +
                ", cityMPG=" + cityMPG +
                ", highwayMPG=" + highwayMPG +
                ", fuelType='" + fuelType + '\'' +
                '}';
    }

    public int getCityMPG() {
        return cityMPG;
    }

    public int getHighwayMPG() {
        return highwayMPG;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getDescription() {
        return "" + make + ", " + model + ", " +
                year + "\n" +
                applicationContext.getString(R.string.displacement) + displacementInLiters + "\n" +
                applicationContext.getString(R.string.transmission) + transmission + "\n" +
                applicationContext.getString(R.string.cityMPG) + cityMPG + ", " +
                applicationContext.getString(R.string.highwayMPG) + highwayMPG + "\n" +
                applicationContext.getString(R.string.fuelType) + fuelType;
    }
}
