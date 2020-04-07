package com.example.ray.carbontracker_flame.Model;

import static com.example.ray.carbontracker_flame.UI.DisplayEmissions.WALKING_CO_2_PER_KM;

/**
 * Represents the app relevant data for walking as a mode of transportation
 */

public class WalkingAsTransport extends Transport {

    public WalkingAsTransport() {
        this.transportType = TRANSPORT_TYPES.WALKING_OR_CYCLING;
    }

    public void setImageId(int cycleWalkingId) {
        this.iconId = cycleWalkingId;
    }

    public int getIconId() {
        return iconId;
    }

    @Override
    public double getEmissionsPerCityKMInKG() {
        return WALKING_CO_2_PER_KM / 1000.0;
    }

    @Override
    public double getEmissionsPerHighwayKMInKG() {
        return WALKING_CO_2_PER_KM / 1000.0;
    }
}
