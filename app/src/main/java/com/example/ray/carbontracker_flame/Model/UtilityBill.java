package com.example.ray.carbontracker_flame.Model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.ray.carbontracker_flame.App;
import com.example.ray.carbontracker_flame.R;

import java.util.Calendar;

import static com.example.ray.carbontracker_flame.UI.AddBillActivity.monthNames;

/**
 * This class represents the Data kept in a Utilities Bill
 */

public abstract class UtilityBill {


    final static double HYDRO_KG_CO2_KWH_RATE = 9000.0 / 1000000.0;
    final static double NATURAL_GAS_KG_CO2_KWH_RATE = 56.1 * 0.0036;
    private final static int NUM_OF_MILLISECONDS_IN_A_DAY = 1000 * 60 * 60 * 24;
    //TODO:PUSH ADD BILL emmisions calculations up to Bill classes
    protected double cO2PerKWHRate;
    private Calendar startDate;
    private Calendar endDate;
    private double totalUsersEmissionsInKG;
    private int numOfDays;
    private double dailyEmissionsRate;
    private BILL_TYPE billType;
    private double usageInKwH;
    private int numOfPeopleCovered;
    private int utilityBillId;
    private double usageInKWH;
    private int numOfPeopleInHome;
    private Context applicationContext;

    public UtilityBill(Calendar startDate, Calendar endDate, double totalUsersEmissionsInKG, BILL_TYPE billType,
                       double usageInKwH, int numOfPeopleCovered) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalUsersEmissionsInKG = totalUsersEmissionsInKG;
        this.billType = billType;
        this.usageInKwH = usageInKwH;
        this.numOfPeopleCovered = numOfPeopleCovered;
        calculateNumOfDays(startDate, endDate);
        calculateDailyEmissionsRate();
        applicationContext = App.getInstance();
    }

    public void setUsageInKWH(double usageInKWH) {
        this.usageInKWH = usageInKWH;
    }

    public int getUtilityBillId() {
        return utilityBillId;
    }

    // will be used later
    public void setUtilityBillId(int utilityBillId) {
        this.utilityBillId = utilityBillId;
    }

    public void setTotalUsersEmissionsInKG(double totalUsersEmissionsInKG) {
        this.totalUsersEmissionsInKG = totalUsersEmissionsInKG;
        calculateDailyEmissionsRate();
    }

    private void calculateDailyEmissionsRate() {
        dailyEmissionsRate = totalUsersEmissionsInKG / (double) numOfDays;
    }

    private void calculateNumOfDays(Calendar startDate, Calendar endDate) {
        //calc num Of days, get the difference in milliseconds and convert to days
        numOfDays = (int) ((endDate.getTimeInMillis() - startDate.getTimeInMillis()) /
                NUM_OF_MILLISECONDS_IN_A_DAY);
    }

    public double getDailyEmissionsRate() {
        return dailyEmissionsRate;
    }

    public double getUsersEmissionsInKG() {
        return totalUsersEmissionsInKG;
    }

    public String getDescription() {

        String startDateDescription = getCalendarDescription(startDate);
        String endDateDescription = getCalendarDescription(endDate);

        double roundedUsageInKwH = usageInKwH;
        roundedUsageInKwH = truncateToTwoDecimalPlaces(roundedUsageInKwH);

        //emissions are truncated in getConvertedNumStringWithUnits method
        double totalEmissions = (totalUsersEmissionsInKG * numOfPeopleCovered);

        return billType.toString() +
                System.lineSeparator() +
                applicationContext.getString(R.string.from) + startDateDescription + " to " + endDateDescription +
                ", " + numOfPeopleCovered + applicationContext.getString(R.string.ppl_used) +
                +roundedUsageInKwH + applicationContext.getString(R.string.kwh_emitted) +
                CarbonUnitPrinter.getConvertedNumStringWithUnits(totalEmissions, 2);
    }

    @NonNull
    private String getCalendarDescription(Calendar calendar) {
        return (monthNames[calendar.get(Calendar.MONTH)] +
                ", " + calendar.get(Calendar.DAY_OF_MONTH) +
                ", " + calendar.get(Calendar.YEAR));
    }

    private double truncateToTwoDecimalPlaces(double number) {
        number = Math.round(number * 100.0);
        number = number / 100.0;
        return number;
    }

    @Override
    public String toString() {
        return applicationContext.getString(R.string.utility_bill) +
                applicationContext.getString(R.string.cO2PerKWHRate) + cO2PerKWHRate +
                applicationContext.getString(R.string.startDate) + startDate +
                applicationContext.getString(R.string.enddate) + endDate +
                applicationContext.getString(R.string.totalusers_emissions) + totalUsersEmissionsInKG +
                applicationContext.getString(R.string.numOfDays) + numOfDays +
                applicationContext.getString(R.string.dailyEmissionsRate) + dailyEmissionsRate +
                applicationContext.getString(R.string.billType) + billType +
                applicationContext.getString(R.string.usageInKwH) + usageInKwH +
                applicationContext.getString(R.string.numOfPeopleCovered) + numOfPeopleCovered +
                '}';
    }

    public BILL_TYPE getBillType() {
        return billType;
    }

    public void setBillType(BILL_TYPE billType) {
        this.billType = billType;
        if (billType == BILL_TYPE.HYDRO) {
            cO2PerKWHRate = HYDRO_KG_CO2_KWH_RATE;
        } else {
            cO2PerKWHRate = NATURAL_GAS_KG_CO2_KWH_RATE;
        }
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
        calculateNumOfDays(startDate, endDate);
        calculateDailyEmissionsRate();
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
        calculateNumOfDays(startDate, endDate);
        calculateDailyEmissionsRate();
    }

    public double getUsageInKwH() {
        return usageInKwH;
    }

    public int getNumOfPeopleCovered() {
        return numOfPeopleCovered;
    }

    public int getNumOfPeopleInHome() {
        return numOfPeopleInHome;
    }

    public void setNumOfPeopleInHome(int numOfPeopleInHome) {
        this.numOfPeopleInHome = numOfPeopleInHome;
    }

    public enum BILL_TYPE {
        HYDRO,
        NATURAL_GAS
    }
}