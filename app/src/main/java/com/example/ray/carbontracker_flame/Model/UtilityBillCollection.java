package com.example.ray.carbontracker_flame.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * UtilityBillCollection encapsulates operations on the saved bills
 */

public class UtilityBillCollection {

    private final LinkedList<UtilityBill> billsSaved;
    private Calendar lastBilledDate;

    public UtilityBillCollection() {
        billsSaved = new LinkedList<>();
        lastBilledDate = Calendar.getInstance();
    }

    public List<UtilityBill> getBillsSaved() {
        //only for reading, add/edit/delete using other methods
        return Collections.unmodifiableList(billsSaved);
        // return billsSaved;
    }

    public void editBill(UtilityBill utilityBill, Calendar startDate, Calendar endDate,
                         double usersEmissionsInKG, UtilityBill.BILL_TYPE billType,
                         double usageInKWH, int numOfPeopleInHome) {
        utilityBill.setStartDate(startDate);
        utilityBill.setEndDate(endDate);
        utilityBill.setTotalUsersEmissionsInKG(usersEmissionsInKG);
        utilityBill.setBillType(billType);
        utilityBill.setUsageInKWH(usageInKWH);
        utilityBill.setNumOfPeopleInHome(numOfPeopleInHome);
    }

    public void addBill(UtilityBill utilityBill, int uniqueBillId) {
        utilityBill.setUtilityBillId(uniqueBillId);
        billsSaved.add(utilityBill);
    }

    public void deleteBill(UtilityBill utilityBill) {
        billsSaved.remove(utilityBill);
    }

    public ArrayList<String> getBillsSavedDescription() {
        ArrayList<String> billsStrings = new ArrayList<>();
        for (UtilityBill bill : getBillsSaved()) {
            billsStrings.add(bill.getDescription());
        }
        return billsStrings;
    }

    //Expensive Operation please cache the value
    public double getNewestEmissionsRate() {
        if (getBillsSaved().isEmpty()) {
            return 0.0;
        } else {
            List<UtilityBill> utilityBillsSaved = getBillsSaved();
            double rate = utilityBillsSaved.get(0).getDailyEmissionsRate();
            Calendar latestDate = utilityBillsSaved.get(0).getEndDate();
            Calendar current;
            for (int i = 1; i < utilityBillsSaved.size(); i++) {
                current = utilityBillsSaved.get(i).getEndDate();
                if (current.after(latestDate)) {
                    latestDate = current;
                    rate = utilityBillsSaved.get(i).getDailyEmissionsRate();
                }
            }
            return rate;
        }
    }

    public void setLastBilledDate(Calendar date) {
        lastBilledDate = date;
    }

    public Calendar getLastBilledDate() {
        return lastBilledDate;
    }
}