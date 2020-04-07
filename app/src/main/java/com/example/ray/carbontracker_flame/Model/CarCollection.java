package com.example.ray.carbontracker_flame.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * CarCollection encapsulates operations on the Cars saved in the model
 */

public class CarCollection {
    private final LinkedList<Car> carsSaved;

    public CarCollection() {
        carsSaved = new LinkedList<>();
    }

    public LinkedList<Car> getCarsSaved() {
        return carsSaved;
    }

    public List<Car> getSavedCars() {
        return getCarsSaved();
    }

    public void deleteCar(Car car) {
        car.makeUnselectableOnMenu();
    }

    public void addCar(Car car, CarbonTrackerModel carbonTrackerModel) {
        carbonTrackerModel.getCarCollection().getCarsSaved().add(car);
    }

    public void editCar(Car car, String carNickname, CarData carData) {
        car.setCarNickname(carNickname);
        car.setCarData(carData);
    }

    public ArrayList<String> getCarSavedDescription() {
        ArrayList<String> carSavedStrings = new ArrayList<>();
        for (Car current : getSavedCars()) {
            if (current.isSelectableOnMenu()) {
                carSavedStrings.add(current.getCarDescription());
            }
        }
        return carSavedStrings;
    }
}
