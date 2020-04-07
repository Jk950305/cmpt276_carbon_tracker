package com.example.ray.carbontracker_flame.Model;

import com.example.ray.carbontracker_flame.R;

/**
 * CarbonUnitPrinter takes in a carbon amount in kg Co2 and returns the unit converted
 * to the unit selected by the user and appends a string showing the unit chosen
 */

public class CarbonUnitPrinter {

    private static final String KG_S_CO2 = " kg(s) Co2";
    private static final String TREE_DAY_S_CO2 = " Tree Day(s) Co2";
    // data from http://www.arborenvironmentalalliance.com/carbon-tree-facts.asp
    //21.7724  kg per Tree year
    private static final double KG_CO2_TO_TREE_YEAR = 1.0 / 21.7724;
    private static final double KG_CO2_TO_TREE_MONTH = KG_CO2_TO_TREE_YEAR * 12.0;
    private static final double KG_CO2_TO_TREE_DAY = KG_CO2_TO_TREE_MONTH * 30.44;
    private static CarbonTrackerModel ctm = CarbonTrackerModel.getInstance();

    private CarbonUnitPrinter() {
    }

    public static double getConvertedNum(double numInKG_CO2) {
        if (ctm.isUsingRelatableUnit()) {
            return numInKG_CO2 * KG_CO2_TO_TREE_DAY;
        } else {
            return numInKG_CO2;
        }
    }

    public static String getUnitString() {
        if (ctm.isUsingRelatableUnit()) {
            return TREE_DAY_S_CO2;
        } else {
            return KG_S_CO2;
        }
    }

    public static String getConvertedNumStringWithUnits(double usersEmissionsInKG, int numOfDecimalPlaces) {
        double num = getConvertedNum(usersEmissionsInKG, numOfDecimalPlaces);
        if (ctm.isUsingRelatableUnit()) {
            return num + TREE_DAY_S_CO2;
        } else {
            return num + KG_S_CO2;
        }
    }

    private static double getConvertedNum(double usersEmissionsInKG, int numOfDecimalPlaces) {
        double num = getConvertedNum(usersEmissionsInKG);
        num = (int) (num * Math.pow(10, numOfDecimalPlaces));
        num /= Math.pow(10, numOfDecimalPlaces);
        return num;
    }
}
