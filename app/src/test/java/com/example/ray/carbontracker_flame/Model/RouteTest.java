package com.example.ray.carbontracker_flame.Model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the Route classes methods
 */
public class RouteTest {
    private Route route = new Route("routeName", 5.5, 6.6, true);


    @Test
    public void testConstructor() throws Exception {
        final String newRouteName = "routeName2";
        Route newRoute = new Route(newRouteName, 5.52, 6.62, true);
        assertEquals(newRoute.getName(), newRouteName);
        assertEquals(newRoute.getNumOfCityKilometers(), 5.52, .01);
        assertEquals(newRoute.getNumOfHighWayKilometers(), 6.62, .01);
    }

    @Test
    public void isSelectableOnMenu() throws Exception {
        assertEquals(route.isSelectableOnMenu(), true);
    }

    @Test
    public void setSelectableOnMenu() throws Exception {
        route.setSelectableOnMenu(false);
        assertEquals(route.isSelectableOnMenu(), false);
    }

    @Test
    public void getName() throws Exception {
        assertEquals(route.getName(), "routeName");
    }

    @Test
    public void getNumOfHighWayKilometers() throws Exception {
        assertEquals(route.getNumOfHighWayKilometers(), 6.6, 0.01);
    }

    @Test
    public void setNumOfHighWayKilometers() throws Exception {
        route.setNumOfHighWayKilometers(7.6);
        assertEquals(route.getNumOfHighWayKilometers(), 7.6, 0.01);
    }

    @Test
    public void getNumOfCityKilometers() throws Exception {
        assertEquals(route.getNumOfCityKilometers(), 5.5, 0.01);
    }

    @Test
    public void setNumOfCityKilometers() throws Exception {
        route.setNumOfCityKilometers(7.6);
        assertEquals(route.getNumOfCityKilometers(), 7.6, 0.01);
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals(route.getDescription(),
                "Route{" +
                        "routeName='" + route.getName() + '\'' +
                        ", numOfCityKilometers=" + route.getNumOfCityKilometers() +
                        ", numOfHighWayKilometers=" + route.getNumOfHighWayKilometers() +
                        ", isSelectableOnMenu=" + route.isSelectableOnMenu() +
                        '}'
        );

    }

}