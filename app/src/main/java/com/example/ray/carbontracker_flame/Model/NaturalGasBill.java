package com.example.ray.carbontracker_flame.Model;

import java.util.Calendar;

/**
 * This class represents a natural gas bill
 */

public class NaturalGasBill extends UtilityBill {
    public NaturalGasBill(Calendar startDate, Calendar endDate, double usersEmissionsInKG,
                          double usageInKwH, int numOfPeopleCovered) {
        super(startDate, endDate, usersEmissionsInKG, BILL_TYPE.NATURAL_GAS, usageInKwH,
                numOfPeopleCovered);
        cO2PerKWHRate = NATURAL_GAS_KG_CO2_KWH_RATE;
    }
}
