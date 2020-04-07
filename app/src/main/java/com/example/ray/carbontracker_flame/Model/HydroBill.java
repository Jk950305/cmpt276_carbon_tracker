package com.example.ray.carbontracker_flame.Model;

import java.util.Calendar;

/**
 * This class represents A Hydro BIll
 */

public class HydroBill extends UtilityBill {
    public HydroBill(Calendar startDate, Calendar endDate, double usersEmissionsInKG,
                     double usageInKwH, int numOfPeopleCovered) {
        super(startDate, endDate, usersEmissionsInKG, BILL_TYPE.HYDRO, usageInKwH,
                numOfPeopleCovered);
        cO2PerKWHRate = HYDRO_KG_CO2_KWH_RATE;
    }
}
