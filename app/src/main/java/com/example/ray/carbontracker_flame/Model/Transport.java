package com.example.ray.carbontracker_flame.Model;

/**
 * The Transport class represents A method of transportation
 */

public abstract class Transport {
    protected int transportType;
    protected int iconId;
    private boolean isSelectableOnMenu;

    Transport() {
        isSelectableOnMenu = true;
    }

    public boolean isSelectableOnMenu() {
        return isSelectableOnMenu;
    }

    public void makeUnselectableOnMenu() {
        isSelectableOnMenu = false;
    }

    public int getTransportType() {
        return transportType;
    }

    public abstract double getEmissionsPerCityKMInKG();

    public abstract double getEmissionsPerHighwayKMInKG();

    public abstract int getIconId();

    public abstract void setImageId(int imageId);
}
