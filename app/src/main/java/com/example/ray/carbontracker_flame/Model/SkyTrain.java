package com.example.ray.carbontracker_flame.Model;

import static com.example.ray.carbontracker_flame.UI.DisplayEmissions.SKY_TRAIN_CO_2_PER_KM;

/**
 * Represents the app relevant data for taking the SkyTrain as a mode of transportation
 */

public class SkyTrain extends Transport {
    public SkyTrain() {
        this.transportType = TRANSPORT_TYPES.SKYTRAIN;
    }

    public void setImageId(int skyTrainId) {
        this.iconId = skyTrainId;
    }

    public int getIconId() {
        return iconId;
    }

    @Override
    public double getEmissionsPerCityKMInKG() {
        return SKY_TRAIN_CO_2_PER_KM / 1000.0;
    }

    @Override
    public double getEmissionsPerHighwayKMInKG() {
        return SKY_TRAIN_CO_2_PER_KM / 1000.0;
    }
}
