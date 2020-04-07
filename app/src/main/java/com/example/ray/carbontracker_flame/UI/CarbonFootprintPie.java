package com.example.ray.carbontracker_flame.UI;

import android.graphics.Color;
import android.os.Bundle;

import com.example.ray.carbontracker_flame.Model.Car;
import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
import com.example.ray.carbontracker_flame.Model.CarbonUnitPrinter;
import com.example.ray.carbontracker_flame.Model.Journey;
import com.example.ray.carbontracker_flame.Model.Route;
import com.example.ray.carbontracker_flame.Model.TRANSPORT_TYPES;
import com.example.ray.carbontracker_flame.Model.UtilityBill;
import com.example.ray.carbontracker_flame.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import org.joda.time.DateTime;

/**
 * Handles generating a pie chart and stacked bar graphs based on data
 */

public class CarbonFootprintPie extends Fragment {

    ArrayList<Double> emissions;
    Calendar cal = Calendar.getInstance();
    PieChart chart;
    HorizontalBarChart barChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pie, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        calculateAllJourneyEmissions();
        chart = (PieChart) getView().findViewById(R.id.chart);
        barChart = (HorizontalBarChart) getView().findViewById(R.id.bar_chart);
        //setupPieChart();
        showSingleDateChart(0);
        //setVisibleChart(R.id.bar_chart);
        //showLast28DaysChart();
        setupSwitch();
        setTodayDate();
        setupCalendarBtn();
        setup28DaysBtn();
        setup365DaysBtn();
    }

    private void setupSwitch() {
        Switch pieSwitch = (Switch) getView().findViewById(R.id.pie_switch);
        //pieSwitch.setChecked(true); toggle
        pieSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Route

                } else {
                    //Mode
                }
            }
        });
    }

    private void setup28DaysBtn() {
        Button btn = (Button) getView().findViewById(R.id.days28_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setVisibleChart(R.id.bar_chart);
                // showLast28DaysChartEXPERIMENT();
                showSingleDateChart(28);
            }
        });
    }

    private void setup365DaysBtn() {
        Button btn = (Button) getView().findViewById(R.id.days365_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleDateChart(365);
            }
        });
    }

    private void setTodayDate() {
        int x = Calendar.YEAR;
        cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
    }

    private void setupCalendarBtn() {
        EditText calendarInput = (EditText) getView().findViewById(R.id.pie_date_input);
        calendarInput.setOnKeyListener(null);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date calTime = Calendar.getInstance().getTime();
        final Fragment thisFrag = this;
        calendarInput.setText(String.format("%s", dateFormat.format(calTime)));
        cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        calendarInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bundle date = new Bundle();
                    date.putInt(getString(R.string.year), year);
                    date.putInt(getString(R.string.month), month);
                    date.putInt(getString(R.string.day), day);
                    DialogFragment dateFragment = new DatePickerFragmentGraph();
                    dateFragment.setTargetFragment(thisFrag, 0);
                    dateFragment.setArguments(date);
                    dateFragment.show(getFragmentManager(), getString(R.string.date_picker));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void calculateAllJourneyEmissions() {
        emissions = new ArrayList<>();
        for (Journey journey : CarbonTrackerModel.getInstance().getJourneyCollection().getSavedJourneys()) {
            emissions.add(CarbonUnitPrinter.getConvertedNum(DisplayEmissions.calculateEmissions(journey)));
        }
    }

    private void setupPieChart() {
        //populating a list of pie entries
        calculateAllJourneyEmissions();
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < emissions.size(); i++) {
            pieEntries.add(new PieEntry(emissions.get(i).floatValue()));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, getString(R.string.carbon_footprint));
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(14);
        dataSet.setValueTextColor(Color.WHITE);
        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart) getView().findViewById(R.id.chart);
        chart.setNoDataText(getString(R.string.not_data));
        chart.setData(data);
        chart.invalidate();
        chart.animateY(2000);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);

    }

    public void onDateReceived(int year, int month, int dayOfMonth) {
        EditText calendarInput = (EditText) getView().findViewById(R.id.pie_date_input);
        calendarInput.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
        cal.set(year, month, dayOfMonth);
        setVisibleChart(R.id.chart);
        showSingleDateChart(0);
        Log.d("DEBUG", "ray");
    }

    public void showSingleDateChart(int daysToInclude) {
        CarbonTrackerModel model = CarbonTrackerModel.getInstance();
        //delete this later will be obsolete
        emissions = new ArrayList<>();

        double busEmissions = 0;
        double skyTrainEmissions = 0;
        HashMap<String, Double> routes = new HashMap<>();
        HashMap<Car, Double> cars = new HashMap<>();

        DateTime journeyDate;
        DateTime selectedDate = new DateTime(cal);
        Switch pieSwitch = (Switch) getView().findViewById(R.id.pie_switch);
        if (pieSwitch.isChecked()) {
            //Display routes
            for (Journey journey : model.getJourneyCollection().getSavedJourneys()) {
                journeyDate = new DateTime(journey.getDate());
                DateTime dayLimit = journeyDate.minusDays(daysToInclude);

                if (daysToInclude != 0 && journeyDate.isAfter(dayLimit) && journeyDate.isBeforeNow()) {
                    //show range of days
                    String key = journey.getRouteTaken().getName();
                    if (routes.containsKey(key)) {
                        // already in hashmap
                        routes.put(journey.getRouteTaken().getName(), routes.get(key) +
                                CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG()));
                    } else {
                        // not in hashmap
                        routes.put(key, CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG()));
                    }
                }
                if (daysToInclude == 0 &&
                        journeyDate.withTimeAtStartOfDay().isEqual(selectedDate.withTimeAtStartOfDay())) {
                    // show single day
                    String key = journey.getRouteTaken().getName();
                    if (routes.containsKey(key)) {
                        // already in hashmap
                        routes.put(journey.getRouteTaken().getName(), routes.get(key) +
                                CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG()));
                    } else {
                        // not in hashmap
                        routes.put(key, CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG()));
                    }
                }
            }

        } else {
            //Mode
            for (Journey journey : model.getJourneyCollection().getSavedJourneys()) {
                journeyDate = new DateTime(journey.getDate());
                DateTime dayLimit = journeyDate.minusDays(daysToInclude);
                if (daysToInclude != 0 && journeyDate.isAfter(dayLimit) && journeyDate.isBeforeNow()) {
                    //show range of days
                    switch (journey.getTransportTaken().getTransportType()) {
                        case TRANSPORT_TYPES.CAR:
                            Car key = journey.getCarDriven();
                            if (cars.containsKey(key)) {
                                cars.put(key, cars.get(key) +
                                        CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG()));
                            } else {
                                //not already in the cars hashtable
                                cars.put(key, CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG()));
                            }
                            //break down
                            break;
                        case TRANSPORT_TYPES.BUS:
                            busEmissions += CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG());
                            break;
                        case TRANSPORT_TYPES.SKYTRAIN:
                            skyTrainEmissions += CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG());
                        default:
                            break;
                    }
                }
                if (daysToInclude == 0 &&
                        journeyDate.withTimeAtStartOfDay().isEqual(selectedDate.withTimeAtStartOfDay())) {
                    // show single day
                    switch (journey.getTransportTaken().getTransportType()) {
                        case TRANSPORT_TYPES.CAR:
                            Car key = journey.getCarDriven();
                            if (cars.containsKey(key)) {
                                cars.put(key, cars.get(key) +
                                        CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG()));
                            } else {
                                //not already in the cars hashtable
                                cars.put(key, CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG()));
                            }
                            break;
                        case TRANSPORT_TYPES.BUS:
                            busEmissions += CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG());
                            break;
                        case TRANSPORT_TYPES.SKYTRAIN:
                            skyTrainEmissions += CarbonUnitPrinter.getConvertedNum(journey.getEmissionsInKG());
                        default:
                            break;
                    }
                }
            }
        }

        double dayHydroEmissions = 0;
        double dayNaturalGasEmissions = 0;
        // add up utilities to corresponding variable
        for (UtilityBill bill : model.getUtilityBillCollection().getBillsSaved()) {
            DateTime billEndDate = new DateTime(bill.getEndDate());
            DateTime billStartDate = new DateTime(bill.getStartDate());
            // if selected date is in between bill dates
            if (selectedDate.isAfter(billStartDate.minusDays(1)) && selectedDate.isBefore(billEndDate.plusDays(1))) {
                if (bill.getBillType() == UtilityBill.BILL_TYPE.HYDRO) {
                    dayHydroEmissions += CarbonUnitPrinter.getConvertedNum(bill.getDailyEmissionsRate());
                } else if (bill.getBillType() == UtilityBill.BILL_TYPE.NATURAL_GAS) {
                    dayNaturalGasEmissions += CarbonUnitPrinter.getConvertedNum(bill.getDailyEmissionsRate());
                }
            }
        }

        //create pie chart
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < emissions.size(); i++) {
            pieEntries.add(new PieEntry(emissions.get(i).floatValue()));
        }
        // add each bill to pie chart
        if (dayHydroEmissions != 0) {
            pieEntries.add(new PieEntry((float) dayHydroEmissions, "Electricity"));
        }
        if (dayNaturalGasEmissions != 0) {
            pieEntries.add(new PieEntry((float) dayNaturalGasEmissions, "Natural Gas"));
        }
        //add each transportation type
//        if (carEmissions != 0) {
//            pieEntries.add(new PieEntry((float) carEmissions, "Car"));
//        }
        if (pieSwitch.isChecked()) {
            //route
            for (Map.Entry<String, Double> entry : routes.entrySet()) {
                pieEntries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
            }
        } else {
            //mode
            for (Map.Entry<Car, Double> entry : cars.entrySet()) {
                pieEntries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey().getCarData().getMake()));
            }
        }
        if (busEmissions != 0) {
            pieEntries.add(new PieEntry((float) busEmissions, "Bus"));
        }
        if (skyTrainEmissions != 0) {
            pieEntries.add(new PieEntry((float) skyTrainEmissions, "Skytrain"));
        }
        // set dataSet properties
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(14);
        dataSet.setValueTextColor(Color.WHITE);
        PieData data = new PieData(dataSet);
        // add units to values
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (value == 0) {
                    return "";
                }
                if (CarbonTrackerModel.getInstance().isUsingRelatableUnit()) {
                    return String.format("%.1f", value) + " TD";
                } else {
                    return String.format("%.1f", value) + "kg";
                }
            }
        });

        // setup legend
        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setTextSize(11f);

        chart.setNoDataText("No Data");
        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate();
        chart.animateY(2000);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
    }

    public void showLast28DaysChart() {
        CarbonTrackerModel model = CarbonTrackerModel.getInstance();
        emissions = new ArrayList<>();
        DateTime journeyDate;
        DateTime selectedDate = new DateTime(cal);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        float index = 1f;
        for (Journey journey : model.getJourneyCollection().getSavedJourneys()) {
            journeyDate = new DateTime(journey.getDate());
            float transportEmissions = 0;
            float utilityEmissions = (float) model.getUtilityBillCollection().getNewestEmissionsRate();

            DateTime dayLimit = journeyDate.minusDays(28);
            if (journeyDate.isAfter(dayLimit) && journeyDate.isBeforeNow()) {
                switch (journey.getTransportTaken().getTransportType()) {
                    case TRANSPORT_TYPES.BUS:
                        transportEmissions = (float) journey.getEmissionsInKG();
                        break;
                    case TRANSPORT_TYPES.CAR:
                        transportEmissions = (float) journey.getEmissionsInKG();
                        break;
                    case TRANSPORT_TYPES.SKYTRAIN:
                        transportEmissions = (float) journey.getEmissionsInKG();
                        break;
                    case TRANSPORT_TYPES.WALKING_OR_CYCLING:
                        break;
                    default:
                        assert (false);
                }

                barEntries.add(new BarEntry(index,
                        new float[]{transportEmissions, utilityEmissions}));
                index++;
            }
        }
        //final String[] strings = {"mon", "tue", "wed", "thurs", "fri"};
        BarDataSet dataSet = new BarDataSet(barEntries, "");
        dataSet.setValueTextSize(14);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setColors(new int[]{R.color.colorPrimary, R.color.colorAccent}, getView().getContext());
        dataSet.setStackLabels(new String[]{
                getString(R.string.transportation1), getString(R.string.utilities)
        });
        BarData data = new BarData(dataSet);

        barChart.setNoDataText("No Data");
        barChart.setData(data);
        barChart.setPinchZoom(false);
        // format right axis
//        IAxisValueFormatter formatter = new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return strings[(int) value];
//            }
//
//        };
//        barChart.getXAxis().setValueFormatter(formatter);
//        barChart.getXAxis().setGranularity(1f);


        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setEnabled(true);

        barChart.animateY(2000);
        Description desc = new Description();
        desc.setText("");
        barChart.setDescription(desc);
        barChart.invalidate();
    }

    public void setVisibleChart(int id) {
        View pieChart = getView().findViewById(R.id.chart);
        View barChart = getView().findViewById(R.id.bar_chart);
        pieChart.setVisibility(getView().INVISIBLE);
        barChart.setVisibility(getView().INVISIBLE);

        switch (id) {
            case R.id.chart:
                chart.setVisibility(getView().VISIBLE);
                break;
            case R.id.bar_chart:
                barChart.setVisibility(getView().VISIBLE);
                break;
        }

    }

    void showLast365DaysChart() {
        Log.d("DEBUG", "showing 365 chart");
        CarbonTrackerModel model = CarbonTrackerModel.getInstance();
        emissions = new ArrayList<>();
        DateTime journeyDate;
        DateTime selectedDate = new DateTime(cal);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        float index = 1f;
        for (Journey journey : model.getJourneyCollection().getSavedJourneys()) {
            journeyDate = new DateTime(journey.getDate());
            float transportEmissions = 0;
            float utilityEmissions = (float) model.getUtilityBillCollection().getNewestEmissionsRate();

            DateTime dayLimit = journeyDate.minusDays(365);
            if (journeyDate.isAfter(dayLimit) && journeyDate.isBeforeNow()) {
                switch (journey.getTransportTaken().getTransportType()) {
                    case TRANSPORT_TYPES.BUS:
                        transportEmissions = (float) journey.getEmissionsInKG();
                        break;
                    case TRANSPORT_TYPES.CAR:
                        transportEmissions = (float) journey.getEmissionsInKG();
                        break;
                    case TRANSPORT_TYPES.SKYTRAIN:
                        transportEmissions = (float) journey.getEmissionsInKG();
                        break;
                    case TRANSPORT_TYPES.WALKING_OR_CYCLING:
                        break;
                    default:
                        assert (false);
                }

                barEntries.add(new BarEntry(index,
                        new float[]{transportEmissions, utilityEmissions}));
                index++;
            }
        }
        BarDataSet dataSet = new BarDataSet(barEntries, "");
        dataSet.setValueTextSize(14);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setColors(new int[]{R.color.colorPrimary, R.color.colorAccent}, getView().getContext());
        dataSet.setStackLabels(new String[]{
                getString(R.string.transportation2), getString(R.string.util)
        });
        BarData data = new BarData(dataSet);

        barChart.setNoDataText(getString(R.string.nodata3));
        barChart.setData(data);
        barChart.setPinchZoom(false);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setEnabled(true);

        barChart.animateY(2000);
        Description desc = new Description();
        desc.setText("");
        barChart.setDescription(desc);
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
                    dayJourneyTotalEmissions += journey.getEmissionsInKG();
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
                        dayNaturalGasEmissions += bill.getDailyEmissionsRate();
                    }
                }
            }
            barEntries.add(new BarEntry(index,
                    new float[]{(float) dayJourneyTotalEmissions, (float) dayHydroEmissions}));
            index++;
        }

        //final String[] strings = {"mon", "tue", "wed", "thurs", "fri"};
        BarDataSet dataSet = new BarDataSet(barEntries, "");
        dataSet.setValueTextSize(14);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setColors(new int[]{R.color.colorPrimary, R.color.colorAccent}, getView().getContext());
        dataSet.setStackLabels(new String[]{
                getString(R.string.Transportation), getString(R.string.Utilities)
        });
        BarData data = new BarData(dataSet);


        barChart.setNoDataText(getString(R.string.nodata));
        barChart.setData(data);
        barChart.setPinchZoom(false);
        barChart.getAxisRight().setInverted(true);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setEnabled(true);

        LimitLine llYAxis = new LimitLine(10f, "Test Line");
        llYAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llYAxis.setTextSize(14f);
        llYAxis.setLineWidth(2f);
        barChart.getAxisRight().addLimitLine(llYAxis);

        barChart.animateY(2000);
        Description desc = new Description();
        desc.setText("");
        barChart.setDescription(desc);
        barChart.invalidate();
    }

}
