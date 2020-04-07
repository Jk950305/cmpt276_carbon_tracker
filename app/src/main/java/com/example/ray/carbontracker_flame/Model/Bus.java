package com.example.ray.carbontracker_flame.Model;

import static com.example.ray.carbontracker_flame.UI.DisplayEmissions.BUS_CO_2_PER_KM;

/**
 * The Bus class represents the app relevant data for taking the Bus as a mode of transportation
 */

public class Bus extends Transport {

    public Bus() {
        this.transportType = TRANSPORT_TYPES.BUS;
    }

    public void setImageId(int busId){
        this.iconId = busId;
    }

    public int getIconId(){
        return iconId;
    }

    @Override
    public double getEmissionsPerCityKMInKG() {
        return BUS_CO_2_PER_KM / 1000.0;
    }

    @Override
    public double getEmissionsPerHighwayKMInKG() {
        return BUS_CO_2_PER_KM / 1000.0;
    }
}