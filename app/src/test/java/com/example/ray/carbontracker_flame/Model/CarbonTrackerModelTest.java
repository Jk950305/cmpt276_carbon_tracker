package com.example.ray.carbontracker_flame.Model;

import org.junit.Test;

import java.util.GregorianCalendar;

/**
 * Tests the CarbonTrackerModel class
 */
public class CarbonTrackerModelTest {
    //TODO: implement equals methods in the Car,Journey,Route classes to make writing tests easier
    private CarbonTrackerModel ctm = CarbonTrackerModel.getInstance();
    private Car car = new Car("TestName", new CarData("Make", "Model", 2017, 1.1, "NA", 5, 5, "Test"));
    private Route route = new Route("routeName", 5.5, 6.6, true);
//    private Journey journey = new Journey(car, route, new GregorianCalendar());
//
//    @Test
//    public void getInstance() throws Exception {
//        CarbonTrackerModel ctm2 = CarbonTrackerModel.getInstance();
//        //  assertEquals(ctm2.hashCode(), ctm2.hashCode());
//    }
//
//    @Test
//    public void getCarDataList() throws Exception {
//        // assertTrue(ctm.getCarDataList().isEmpty());
//    }
//
//    @Test
//    public void getSavedCars() throws Exception {
//        // assertTrue(ctm.getSavedCars().isEmpty());
//    }
//
//    @Test
//    public void getSavedRoutes() throws Exception {
//        // assertTrue(ctm.getSavedRoutes().isEmpty());
//    }
//
//    @Test
//    public void getSavedJourneys() throws Exception {
//        //   assertTrue(ctm.getSavedJourneys().isEmpty());
//    }
//
//    @Test
//    public void getCarsChosenList() throws Exception {
//        // assertTrue(ctm.getCarsChosenList().isEmpty());
//    }
//
//    @Test
//    public void getCarMakes() throws Exception {
//    }
//
//    @Test
//    public void loadCarDataFromDisk() throws Exception {
//    }
//
//    @Test
//    public void deleteCar() throws Exception {
//
//    }
//
//    @Test
//    public void addJourney() throws Exception {
//       //  ctm.addJourney(journey);
////        assertTrue(ctm.getSavedJourneys().size() == 1);
//    }
//
//    @Test
//    public void addJourney1() throws Exception {
//        ctm.addJourney(car, route, new GregorianCalendar());
//        // assertTrue(ctm.getSavedJourneys().size() == 2);
//    }
//
//    @Test
//    public void addCar() throws Exception {
//
//    }
//
//    @Test
//    public void addRoute() throws Exception {
//
//    }
//
//    @Test
//    public void addRoute1() throws Exception {
//
//    }
//
//    @Test
//    public void editCar() throws Exception {
//
//    }
//
//    @Test
//    public void editRoute() throws Exception {
//
//    }
//
//    @Test
//    public void deleteRoute() throws Exception {
//
//    }
//
//    @Test
//    public void getRouteDescriptions() throws Exception {
//
//    }
//
//    @Test
//    public void getCarSavedDescription() throws Exception {
//
//    }
//
//    @Test
//    public void findSelectedOptionCars() throws Exception {
//
//    }
//
//    @Test
//    public void getCarModelsFromMake() throws Exception {
//
//    }
//
//    @Test
//    public void getCarYearsFromMakeModel() throws Exception {
//
//    }
//
//    @Test
//    public void getCarsChosenListDescription() throws Exception {
//
//    }

}