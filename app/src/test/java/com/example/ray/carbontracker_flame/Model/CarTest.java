package com.example.ray.carbontracker_flame.Model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * CarTest tests all the methods of the Car class
 */
public class CarTest {
    private Car car = new Car("TestName", new CarData("Make", "Model", 2017, 1.1, "NA", 5, 5, "Test"));

    @Test
    public void getCarData() throws Exception {
        CarData carData = car.getCarData();
        assertEquals(carData.getMake(), "Make");
        assertEquals(carData.getModel(), "Model");
        assertEquals(carData.getYear(), 2017);
        assertEquals(carData.getDisplacementInLiters(), 1.1, .01);
        assertEquals(carData.getTransmission(), "NA");
        assertEquals(carData.getCityMPG(), 5);
        assertEquals(carData.getHighwayMPG(), 5);
        assertEquals(carData.getFuelType(), "Test");

        assertEquals(carData.toString(),
                "CarData{" +
                        "make='" + carData.getMake() + '\'' +
                        ", model='" + carData.getModel() + '\'' +
                        ", year=" + carData.getYear() +
                        ", displacementInLiters=" + carData.getDisplacementInLiters() +
                        ", transmission='" + carData.getTransmission() + '\'' +
                        ", cityMPG=" + carData.getCityMPG() +
                        ", highwayMPG=" + carData.getHighwayMPG() +
                        ", fuelType='" + carData.getFuelType() + '\'' +
                        '}'
        );

        assertEquals(carData.getDescription(),
                "" + carData.getMake() + ", " + carData.getModel() + ", " +
                        carData.getYear() + "\n" +
                        "displacement in Liters= " + carData.getDisplacementInLiters() + "\n" +
                        "transmission= " + carData.getTransmission() + "\n" +
                        "city MPG= " + carData.getCityMPG() + ", " +
                        "highway MPG= " + carData.getHighwayMPG() + "\n" +
                        "Fuel Type=" + carData.getFuelType()
        );
    }

    @Test
    public void setCarData() throws Exception {
        car.setCarData(new CarData("Make2", "Model2", 20172, 1.12, "NA2", 52, 52, "Test2"));
        CarData carData = car.getCarData();
        assertEquals(carData.getMake(), "Make2");
        assertEquals(carData.getModel(), "Model2");
        assertEquals(carData.getYear(), 20172);
        assertEquals(carData.getDisplacementInLiters(), 1.12, .01);
        assertEquals(carData.getTransmission(), "NA2");
        assertEquals(carData.getCityMPG(), 52);
        assertEquals(carData.getHighwayMPG(), 52);
        assertEquals(carData.getFuelType(), "Test2");

        assertEquals(carData.toString(),
                "CarData{" +
                        "make='" + carData.getMake() + '\'' +
                        ", model='" + carData.getModel() + '\'' +
                        ", year=" + carData.getYear() +
                        ", displacementInLiters=" + carData.getDisplacementInLiters() +
                        ", transmission='" + carData.getTransmission() + '\'' +
                        ", cityMPG=" + carData.getCityMPG() +
                        ", highwayMPG=" + carData.getHighwayMPG() +
                        ", fuelType='" + carData.getFuelType() + '\'' +
                        '}'
        );

        assertEquals(carData.getDescription(),
                "" + carData.getMake() + ", " + carData.getModel() + ", " +
                        carData.getYear() + "\n" +
                        "displacement in Liters= " + carData.getDisplacementInLiters() + "\n" +
                        "transmission= " + carData.getTransmission() + "\n" +
                        "city MPG= " + carData.getCityMPG() + ", " +
                        "highway MPG= " + carData.getHighwayMPG() + "\n" +
                        "Fuel Type=" + carData.getFuelType()
        );
    }

    @Test
    public void isSelectableOnMenu() throws Exception {
        assertEquals(car.isSelectableOnMenu(), true);
        car.makeUnselectableOnMenu();
        assertEquals(car.isSelectableOnMenu(), false);
    }

    @Test
    public void getCarNickname() throws Exception {
        assertEquals(car.getCarNickname(), "TestName");
    }

    @Test
    public void getCarDescription() throws Exception {
        String carDescription = "" + car.getCarNickname();
        carDescription += "\n" + car.getCarData().getDescription();
        assertEquals(car.getCarDescription(), carDescription);
    }

    @Test
    public void setCarNickname() throws Exception {
        car.setCarNickname("TestName2");
        assertEquals(car.getCarNickname(), "TestName2");
    }

}