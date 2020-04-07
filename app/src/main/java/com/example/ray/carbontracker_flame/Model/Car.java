package com.example.ray.carbontracker_flame.Model;

import android.util.Log;

import static com.example.ray.carbontracker_flame.UI.DisplayEmissions.CO2_PER_GALLON;
import static com.example.ray.carbontracker_flame.UI.DisplayEmissions.MILES_IN_1KM;

/**
 * The Car class represents a Car Object with its make, model and other statistics
 */

public class Car extends Transport {

    private String carNickname;
    private CarData carData;

    public Car(String carNickname, CarData carData) {
        super();
        this.carNickname = carNickname;
        this.carData = carData;
        this.transportType = TRANSPORT_TYPES.CAR;
    }

    public CarData getCarData() {
        return carData;
    }

    public void setImageId(int id) {
        this.iconId = id;
    }

    public int getIconId() {
        return iconId;
    }

    void setCarData(CarData carData) {
        this.carData = carData;
    }

    public String getCarNickname() {
        return carNickname;
    }

    void setCarNickname(String carNickname) {
        this.carNickname = carNickname;
    }

    public String getCarDescription() {
        String carDescription = "" + this.carNickname;
        carDescription += "\n" + this.carData.getDescription();
        return carDescription;
    }

    @Override
    public double getEmissionsPerCityKMInKG() {
        double cityMPG = getCarData().getCityMPG();
        double gallonsPerMile = 1 / cityMPG;
        double gallonsPerKM = gallonsPerMile * MILES_IN_1KM;
        //return emissions for 1 KM
        return gallonsPerKM * CO2_PER_GALLON;
    }

    @Override
    public double getEmissionsPerHighwayKMInKG() {
        double highwayMPG = getCarData().getHighwayMPG();
        double gallonsPerMile = 1 / highwayMPG;
        double gallonsPerKM = gallonsPerMile * MILES_IN_1KM;
        //return emissions for 1 KM
        return gallonsPerKM * CO2_PER_GALLON;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        Car car = (Car) o;
        Log.d("DEBUG", "comparing..");
        return this.carData.getModel().equals(car.getCarData().getModel()) &&
                this.carData.getTransmission().equals(car.getCarData().getTransmission());
    }
}