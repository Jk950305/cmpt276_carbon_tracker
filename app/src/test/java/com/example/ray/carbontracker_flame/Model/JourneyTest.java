package com.example.ray.carbontracker_flame.Model;

import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * JourneyTest tests the Journey classes methods
 */
public class JourneyTest {
    private Car car = new Car("TestName", new CarData("Make", "Model", 2017, 1.1, "NA", 5, 5, "Test"));
    private Route route = new Route("routeName", 5.5, 6.6, true);
   // private Journey journey = new Journey(car, route, new GregorianCalendar());

    @Test
    public void getCarDriven() throws Exception {
//        Car carDriven = journey.getCarDriven();
//        assertEquals(carDriven.getCarNickname(), "TestName");
//        assertEquals(carDriven.getCarDescription(), car.getCarDescription());
//        assertEquals(carDriven.isSelectableOnMenu(), car.isSelectableOnMenu());

    }

    @Test
    public void getRouteTaken() throws Exception {
//        Route routeTaken = journey.getRouteTaken();
//        assertEquals(routeTaken.getDescription(), route.getDescription());
//        assertEquals(routeTaken.getName(), route.getName());
//        assertEquals(routeTaken.getNumOfCityKilometers(), route.getNumOfCityKilometers(), 0.01);
//        assertEquals(routeTaken.getNumOfHighWayKilometers(), route.getNumOfHighWayKilometers(), 0.01);
//        assertEquals(routeTaken.isSelectableOnMenu(), route.isSelectableOnMenu());
    }

    @Test
    public void getDate() throws Exception {
       // assertEquals(journey.getDate(), "testDate");
    }

}