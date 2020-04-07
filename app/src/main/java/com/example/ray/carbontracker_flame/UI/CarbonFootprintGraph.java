package com.example.ray.carbontracker_flame.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
import com.example.ray.carbontracker_flame.Model.CarbonUnitPrinter;
import com.example.ray.carbontracker_flame.Model.Journey;
import com.example.ray.carbontracker_flame.Model.JourneyCollection;
import com.example.ray.carbontracker_flame.Model.UtilityBill;
import com.example.ray.carbontracker_flame.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.joda.time.DateTime;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Handles creating a stacked bar graph with model data
 */

public class CarbonFootprintGraph extends Fragment {

    private HorizontalBarChart barChart;
    ArrayList<Double> emissions;
    private Calendar cal = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        barChart = (HorizontalBarChart) getView().findViewById(R.id.graph);
        barChart.getDescription().setEnabled(false);

        //setupChartData();
        showLast28DaysChartEXPERIMENT();
        setup28DaysBtn();
        setup365DaysBtn();
    }

    private void setup28DaysBtn() {
        Button btn = (Button) getView().findViewById(R.id.days28_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLast28DaysChartEXPERIMENT();
            }
        });
    }

    private void setup365DaysBtn() {
        Button btn = (Button) getView().findViewById(R.id.days365_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show365graph();
            }
        });
    }

    private void show365graph() {
        CarbonTrackerModel model = CarbonTrackerModel.getInstance();
        emissions = new ArrayList<>();
        DateTime journeyDate;
        DateTime selectedDate = new DateTime(cal);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        double[] monthJourneyEmissions = new double[12];
        double[] monthUtilitiesEmissions = new double[12];

        float index = 1f;
        DateTime currentDate = new DateTime(cal).plusDays(1);
        DateTime dateLimit = new DateTime(cal).minusDays(365);

        // if in year then get month and put in corrosponding array e.g. jan -> [0]
        for (Journey journey : model.getJourneyCollection().getSavedJourneys()) {
            // check if in year
            journeyDate = new DateTime(journey.getDate());
            if (journeyDate.isAfter(dateLimit.minusDays(1)) && journeyDate.isBeforeNow()) {
                Log.d("DEBUG", journey.getDate().get(Calendar.MONTH) + "");
                monthJourneyEmissions[journey.getDate().get(Calendar.MONTH)] += journey.getEmissionsInKG();
            }
        }
        for (UtilityBill bill : model.getUtilityBillCollection().getBillsSaved()) {
            DateTime billEndDate = new DateTime(bill.getEndDate());
            DateTime billStartDate = new DateTime(bill.getStartDate());
            // if selected date is in between bill dates
            if (selectedDate.isAfter(billStartDate.minusDays(1)) && selectedDate.isBefore(billEndDate.plusDays(1))) {
                monthUtilitiesEmissions[bill.getStartDate().get(Calendar.MONTH)] += bill.getDailyEmissionsRate();
            }
        }
        for (int i = 0; i < monthJourneyEmissions.length; i++) {
            barEntries.add(new BarEntry(i, new float[]{(float) monthJourneyEmissions[i],
                    (float) monthUtilitiesEmissions[i]}));
        }

//        while (dateLimit.isBefore(currentDate)) {
//            currentDate = new DateTime(currentDate).minusDays(1);
//            //Log.d("DEBUG", "date time decremented, currentDateTime: " + currentDate.toString());
//            double dayJourneyTotalEmissions = 0;
//            double dayHydroEmissions = 0;
//            double dayNaturalGasEmissions = 0;
//            for (Journey journey : model.getJourneyCollection().getSavedJourneys()) {
//                journeyDate = new DateTime(journey.getDate());
//                if (journeyDate.withTimeAtStartOfDay().isEqual(currentDate.withTimeAtStartOfDay())) {
//                    dayJourneyTotalEmissions += journey.getEmissionsInKG();
//                }
//            }
//            for (UtilityBill bill : model.getUtilityBillCollection().getBillsSaved()) {
//                DateTime billEndDate = new DateTime(bill.getEndDate());
//                DateTime billStartDate = new DateTime(bill.getStartDate());
//                // if selected date is in between bill dates
//                if (selectedDate.isAfter(billStartDate.minusDays(1)) && selectedDate.isBefore(billEndDate.plusDays(1))) {
//                    if (bill.getBillType() == UtilityBill.BILL_TYPE.HYDRO) {
//                        dayHydroEmissions += bill.getDailyEmissionsRate();
//                    } else if (bill.getBillType() == UtilityBill.BILL_TYPE.NATURAL_GAS) {
//                        dayNaturalGasEmissions += bill.getDailyEmissionsRate();
//                    }
//                }
//            }
//            barEntries.add(new BarEntry(currentDate.getDayOfMonth(),
//                    new float[] {(float) dayJourneyTotalEmissions, (float) dayHydroEmissions}));
//            index++;
//        }

        BarDataSet dataSet = new BarDataSet(barEntries, "");
        dataSet.setValueTextSize(14);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setColors(new int[]{R.color.colorPrimary, R.color.colorAccent}, getView().getContext());
        dataSet.setStackLabels(new String[]{
                "Transportation", "Utilities"
        });
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (value == 0) {
                    return "";
                }
                return String.format("%.1f", value);
            }
        });

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setEnabled(true);

        barChart.getAxisRight().removeAllLimitLines();
        LimitLine llYAxis = new LimitLine((float) CarbonFootprintActivity.AVG_CANADIAN_EMISSION_DAY * 30,
                "Average Canadian Emissions");
        Log.d("DEBUG", "emissions avg: " + CarbonFootprintActivity.AVG_CANADIAN_EMISSION_DAY * 30);
        llYAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llYAxis.setTextSize(14f);
        llYAxis.setLineWidth(2f);

        LimitLine parisAccordLine = new LimitLine((float) CarbonFootprintActivity.PARIS_ACCORD_TARGET_DAY * 30,
                "Paris Accord Target");
        parisAccordLine.setTextSize(14f);
        parisAccordLine.setLineWidth(2f);
        parisAccordLine.setLineColor(Color.CYAN);
        barChart.getAxisRight().addLimitLine(llYAxis);
        barChart.getAxisRight().addLimitLine(parisAccordLine);
        barChart.getAxisRight().setSpaceTop(3000f);
        barChart.getAxisLeft().setSpaceTop(3000f);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
                    "Oct", "Nov", "Dec"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return months[(int) value];
            }
        });

        BarData data = new BarData(dataSet);
        barChart.setNoDataText("No Data");
        barChart.setData(data);
        barChart.setPinchZoom(false);

        barChart.animateY(2000);
        Description desc = new Description();
        desc.setText("");
        barChart.setDescription(desc);
        barChart.invalidate();

    }

    private void setupChartData() {
        CombinedData data = new CombinedData();
        //set data here
        barChart.invalidate();
    }

    public void showLast28DaysChartEXPERIMENT() {
        CarbonTrackerModel model = CarbonTrackerModel.getInstance();
        emissions = new ArrayList<>();
        DateTime journeyDate;
        DateTime selectedDate = new DateTime(cal);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        float index = 1f;
        DateTime currentDate = new DateTime(cal).plusDays(1);
        DateTime dateLimit = new DateTime(cal).minusDays(28);
        while (dateLimit.isBefore(currentDate)) {
            currentDate = new DateTime(currentDate).minusDays(1);
            //Log.d("DEBUG", "date time decremented, currentDateTime: " + currentDate.toString());
            double dayJourneyTotalEmissions = 0;
            double dayHydroEmissions = 0;
            double dayNaturalGasEmissions = 0;
            for (Journey journey : model.getJourneyCollection().getSavedJourneys()) {
                journeyDate = new DateTime(journey.getDate());
                if (journeyDate.withTimeAtStartOfDay().isEqual(currentDate.withTimeAtStartOfDay())) {
                    dayJourneyTotalEmissions += CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG());
                }
            }
            for (UtilityBill bill : model.getUtilityBillCollection().getBillsSaved()) {
                DateTime billEndDate = new DateTime(bill.getEndDate());
                DateTime billStartDate = new DateTime(bill.getStartDate());
                // if selected date is in between bill dates
                if (selectedDate.isAfter(billStartDate.minusDays(1)) && selectedDate.isBefore(billEndDate.plusDays(1))) {
                    if (bill.getBillType() == UtilityBill.BILL_TYPE.HYDRO) {
                        dayHydroEmissions += bill.getDailyEmissionsRate();
                    } else if (bill.getBillType() == UtilityBill.BILL_TYPE.NATURAL_GAS) {
                        dayNaturalGasEmissions += CarbonUnitPrinter.getConvertedNum(bill.getDailyEmissionsRate());
                    }
                }
            }
            barEntries.add(new BarEntry(currentDate.getDayOfMonth(),
                    new float[]{(float) dayJourneyTotalEmissions, (float) dayHydroEmissions}));
            index++;
        }

        BarDataSet dataSet = new BarDataSet(barEntries, "");
        dataSet.setValueTextSize(14);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setColors(new int[]{R.color.colorPrimary, R.color.colorAccent}, getView().getContext());
        dataSet.setStackLabels(new String[]{
                "Transportation", "Utilities"
        });
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (value == 0) {
                    return "";
                }
                return String.format("%.1f", value);
            }
        });

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setEnabled(true);

        LimitLine llYAxis = new LimitLine((float) CarbonFootprintActivity.AVG_CANADIAN_EMISSION_DAY,
                "Average Canadian Emissions");
        llYAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llYAxis.setTextSize(14f);
        llYAxis.setLineWidth(2f);

        LimitLine parisAccordLine = new LimitLine((float) CarbonFootprintActivity.PARIS_ACCORD_TARGET_DAY,
                "Paris Accord Target");
        parisAccordLine.setTextSize(14f);
        parisAccordLine.setLineWidth(2f);
        parisAccordLine.setLineColor(Color.CYAN);
        barChart.getAxisRight().addLimitLine(llYAxis);
        barChart.getAxisRight().addLimitLine(parisAccordLine);
        barChart.getAxisRight().setSpaceTop(50f);
        barChart.getAxisLeft().setSpaceTop(50f);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "Day" + String.format(" %d", (int) value);
            }
        });

        BarData data = new BarData(dataSet);
        barChart.setNoDataText("No Data");
        barChart.setData(data);
        barChart.setPinchZoom(false);

        barChart.animateY(2000);
        Description desc = new Description();
        desc.setText("");
        barChart.setDescription(desc);
        barChart.invalidate();
    }
}
