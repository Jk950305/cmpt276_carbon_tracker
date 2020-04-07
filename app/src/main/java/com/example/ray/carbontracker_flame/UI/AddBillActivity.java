package com.example.ray.carbontracker_flame.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
//import com.example.ray.carbontracker_flame.Model.DBBillAdapter;
import com.example.ray.carbontracker_flame.Model.CarbonUnitPrinter;
import com.example.ray.carbontracker_flame.Model.HydroBill;
import com.example.ray.carbontracker_flame.Model.NaturalGasBill;
import com.example.ray.carbontracker_flame.Model.UtilityBill;
import com.example.ray.carbontracker_flame.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Defines behavior for the AddBill Screen
 */
public class AddBillActivity extends AppCompatActivity {

    public static final String START_DATE_TAG = "Select Start Date";
    public static final String END_DATE_TAG = "Select End Date";
    public static final String[] monthNames =
            {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
    private final static double HYDRO_KG_CO2_KWH_RATE = 9000.0 / 1000000.0;
    private final static double NATURAL_GAS_KG_CO2_KWH_RATE = 56.1 * 0.0036;
    private final CarbonTrackerModel ctm = CarbonTrackerModel.getInstance();
    private long lastBillDate;
    private Calendar startDate;
    private Calendar endDate;

//    private DBBillAdapter billDbAdapter;

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddBillActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        createActionBar();
        setupRadioButtons();
        setUpEditTexts();
        setupOkButton();
        setupUIToHoldPrevValues();
    }

    private void createActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            String ERROR_MESSAGE = e.getMessage();
            Log.i("AddBillActivity", ERROR_MESSAGE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }

    private void setupUIToHoldPrevValues() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int i = (int) extras.get(BillListActivity.INDEX_OF_BILL_TO_EDIT_KEY);
            UtilityBill bill = ctm.getUtilityBillCollection().getBillsSaved().get(i);
            startDate = bill.getStartDate();
            endDate = bill.getEndDate();

            updateEditTexts();
        }
    }

    private void setupRadioButtons() {
        RadioButton rdoHydro = (RadioButton) findViewById(R.id.rdoHydro);
        rdoHydro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //display your personal usage
                    setUserEmissionsDisplay(calcUsersEmissionsUsage(HYDRO_KG_CO2_KWH_RATE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        RadioButton rdoGas = (RadioButton) findViewById(R.id.rdoNaturalGas);
        rdoGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //display your personal usage
                    setUserEmissionsDisplay(calcUsersEmissionsUsage(NATURAL_GAS_KG_CO2_KWH_RATE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpEditTexts() {
        EditText edtStartDate = (EditText) findViewById(R.id.edtStartDate);
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerFragmentAddBillActivity();
                dialogFragment.show(getSupportFragmentManager(), START_DATE_TAG);
            }
        });
        EditText edtEndDate = (EditText) findViewById(R.id.edtEndDate);
        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerFragmentAddBillActivity();
                dialogFragment.show(getSupportFragmentManager(), END_DATE_TAG);
            }
        });
    }

    private void setupOkButton() {
        Button btn = (Button) findViewById(R.id.btnOk_AddBill);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewBill();
            }
        });
    }

    private void createNewBill() {
        try {
            //make sure the endDate is after the startDate and that both dates have been selected
            if (startDate == null || endDate == null) {
                Toast.makeText(this, R.string.select_start_end_dates, Toast.LENGTH_SHORT).show();
            } else if (!startDate.before(endDate)) {
                Toast.makeText(this, R.string.end_date_error1, Toast.LENGTH_SHORT).show();
            } else {
                double cO2PerKWHRate;
                // Get Bill type using radio buttons
                RadioButton rdoHydo = (RadioButton) findViewById(R.id.rdoHydro);
                RadioButton rdoGas = (RadioButton) findViewById(R.id.rdoNaturalGas);
                boolean isHydroChecked = rdoHydo.isChecked();
                boolean isGasChecked = rdoGas.isChecked();
                if (isHydroChecked) {
                    cO2PerKWHRate = HYDRO_KG_CO2_KWH_RATE;
                } else if (isGasChecked) {
                    cO2PerKWHRate = NATURAL_GAS_KG_CO2_KWH_RATE;
                } else {
                    Toast.makeText(this, R.string.missing_bill_type_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                double usersEmissionsInKG = calcUsersEmissionsUsage(cO2PerKWHRate);
                EditText edtUsageInKWH = (EditText) findViewById(R.id.edtAmountUsed);
                // EditText edtUsageInKWH = this.edtUsageInKWH;
                double usageInKWH = Double.parseDouble(edtUsageInKWH.getText().toString());
                EditText edtNumOfPeopleInHome = (EditText) findViewById(R.id.edtNumOfPeopleInHome);
                int numOfPeopleInHome = Integer.parseInt(edtNumOfPeopleInHome.getText().toString());

                //display the users personal usage usage
                setUserEmissionsDisplay(usersEmissionsInKG);

                //create the Bill and store it in the model
                UtilityBill utilityBill;
                UtilityBill.BILL_TYPE billType;
                if (isHydroChecked) {
                    utilityBill = new HydroBill(startDate, endDate, usersEmissionsInKG,
                            usageInKWH, numOfPeopleInHome); // change the 0 later
                    billType = UtilityBill.BILL_TYPE.HYDRO;
                } else if (isGasChecked) {
                    utilityBill = new NaturalGasBill(startDate, endDate, usersEmissionsInKG,
                            usageInKWH, numOfPeopleInHome); // change the 0 later
                    billType = UtilityBill.BILL_TYPE.NATURAL_GAS;
                } else {
                    Toast.makeText(this, R.string.please_select_bill_type, Toast.LENGTH_SHORT).show();
                    return;
                }

                String startDateStr = getStringFromDate(startDate);
                String endDateStr = getStringFromDate(endDate);
                String billTypeStr = billType.toString();
                Bundle extras = getIntent().getExtras();
                int uniqueBillId = ctm.getCounterForBillId();

                if (extras != null) {
                    int i = (int) extras.get(BillListActivity.INDEX_OF_BILL_TO_EDIT_KEY);
                    UtilityBill currentBill = ctm.getUtilityBillCollection().getBillsSaved().get(i);
                    ctm.getUtilityBillCollection().editBill(currentBill, startDate, endDate, usersEmissionsInKG, billType, usageInKWH, numOfPeopleInHome);

                    int billId = currentBill.getUtilityBillId();

                    ctm.updateBillToDatabase(billId,
                            startDateStr, endDateStr,
                            usersEmissionsInKG, billTypeStr,
                            usageInKWH, numOfPeopleInHome, this);

                } else {
                    ctm.getUtilityBillCollection().addBill(utilityBill, uniqueBillId);

                    ctm.addBillToDatabase(startDateStr, endDateStr,
                            usersEmissionsInKG, billTypeStr,
                            usageInKWH, numOfPeopleInHome, this);

                    addAndSaveCounterBillId();
                }

                saveLastBillDate();

                finish();

                //display tips
                Intent intent = DisplayTips.makeIntent(AddBillActivity.this);
                startActivity(intent);


            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.invalid_input, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String getStringFromDate(Calendar startDate) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
        return form.format(startDate.getTime());
    }

    private void setUserEmissionsDisplay(double usersEmissionsInKG) {
        //round to 2 decimal places
        String usersEmissionsString =
                CarbonUnitPrinter.getConvertedNumStringWithUnits(usersEmissionsInKG, 2);

        //display the emissions in the appropriate unit
        TextView txtDailyUsage = (TextView) findViewById(R.id.txtDailyEmissionsDisplay);
        txtDailyUsage.setText(usersEmissionsString);
    }

    private double calcUsersEmissionsUsage(double cO2PerKWHRate) {
        //calc your emissions usage
        EditText edtUsageInKWH = (EditText) findViewById(R.id.edtAmountUsed);
        double usageInKWH = Double.parseDouble(edtUsageInKWH.getText().toString());
        EditText edtNumOfPeopleInHome = (EditText) findViewById(R.id.edtNumOfPeopleInHome);
        int numOfPeopleInHome = Integer.parseInt(edtNumOfPeopleInHome.getText().toString());
        return (usageInKWH / (double) numOfPeopleInHome) * cO2PerKWHRate;
    }


    public void setStartDate(int year, int month, int day) {
        startDate = new GregorianCalendar(year, month, day);
    }

    public void setEndDate(int year, int month, int day) {
        endDate = new GregorianCalendar(year, month, day);
    }

    public void updateEditTexts() {
        //get start date
        EditText edtStartDate = (EditText) findViewById(R.id.edtStartDate);
        //get end date
        EditText edtEndDate = (EditText) findViewById(R.id.edtEndDate);
        if (startDate != null) {
            edtStartDate.setText(monthNames[startDate.get(Calendar.MONTH)] +
                    ", " + startDate.get(Calendar.DAY_OF_MONTH) +
                    ", " + startDate.get(Calendar.YEAR));
        }
        if (endDate != null) {
            edtEndDate.setText(monthNames[endDate.get(Calendar.MONTH)] +
                    ", " + endDate.get(Calendar.DAY_OF_MONTH) +
                    ", " + endDate.get(Calendar.YEAR));
        }
    }

    private void addAndSaveCounterBillId() {
        // add the journey ID because we are creating a new journey.
        ctm.addCounterForBillId();
        SharedPreferences prefs = getSharedPreferences(WelcomeActivity.SHAREDPREF_BILL, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(WelcomeActivity.SHAREDPREF_ITEM_COUNTERBILL, ctm.getCounterForBillId());
        edit.apply();
    }

    private void saveLastBillDate() {
        lastBillDate = Calendar.getInstance().getTimeInMillis();
        //lastBillDate = endDate.getTimeInMillis();
        SharedPreferences setting = getSharedPreferences(NotificationActivity.SHAREDPREF_NOTI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putLong(NotificationActivity.SHAREDPREF_LAST_BILLDATE, lastBillDate);
        editor.apply();
    }
}
