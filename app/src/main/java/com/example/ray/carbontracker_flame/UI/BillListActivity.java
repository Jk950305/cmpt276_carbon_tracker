package com.example.ray.carbontracker_flame.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
//import com.example.ray.carbontracker_flame.Model.DBBillAdapter;
import com.example.ray.carbontracker_flame.Model.UtilityBill;
import com.example.ray.carbontracker_flame.R;

import java.util.List;

import static com.example.ray.carbontracker_flame.UI.RouteActivity.KEY_DELETE;
import static com.example.ray.carbontracker_flame.UI.RouteActivity.KEY_EDIT;

/**
 * This class defines the behavior for the BillsList activity.
 */
public class BillListActivity extends AppCompatActivity {

    public static final String INDEX_OF_BILL_TO_EDIT_KEY = "INDEX_OF_BILL";
    private static final int ADD_BILL_CODE = 0;
    private static final int EDIT_BILL_CODE = 1;
    private static final int DELETE_BILL_CODE = 2;
    CarbonTrackerModel ctm;

    public static Intent makeIntent(Context context) {
        return new Intent(context, BillListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);

        ctm = CarbonTrackerModel.getInstance();
        createActionBar();
        setupContextMenu();
        registerClickCallBack();
        setupAddNewBillButton();
        populateListView();
    }

    private void createActionBar() {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            String ERROR_MESSAGE = e.getMessage();
            Log.i("BillListActivity", ERROR_MESSAGE);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        closeBillDatabase();
    }


    private void setupContextMenu() {
        ListView list = (ListView) findViewById(R.id.lstBills);
        registerForContextMenu(list);
    }

    private void registerClickCallBack() {
        ListView list = (ListView) findViewById(R.id.lstBills);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //do nothing
            }
        });
    }

    private void populateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, //context for the activity
                //reuse route list layout
                R.layout.route_list_view_layout, //Layout to use(create)
                ctm.getUtilityBillCollection().getBillsSavedDescription()); // Items to be displayed
        ListView lstBills = (ListView) findViewById(R.id.lstBills);
        lstBills.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // menu.setHeaderTitle("Bill Operations");
        menu.add(KEY_EDIT);
        menu.add(KEY_DELETE);
    }

    private void setupAddNewBillButton() {
        Button btn = (Button) findViewById(R.id.btnAddBill);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddBillActivity.makeIntent(BillListActivity.this);
                startActivityForResult(intent, ADD_BILL_CODE);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = (info.position);
        if (item.getTitle() == KEY_EDIT) {
            int indexOfBillToEdit = info.position;
            Intent intent = AddBillActivity.makeIntent(this);
            intent.putExtra(INDEX_OF_BILL_TO_EDIT_KEY, indexOfBillToEdit);
            startActivityForResult(intent, EDIT_BILL_CODE);
            //Toast.makeText(BillListActivity.this, "Clicked on EDIT :", Toast.LENGTH_SHORT).show();
        } else if (item.getTitle() == KEY_DELETE) {
            List<UtilityBill> billsList = ctm.getUtilityBillCollection().getBillsSaved();
            UtilityBill utilityBill = billsList.get(position);

            ctm.deleteBillInDatabase(utilityBill.getUtilityBillId(), this);
//            billDbAdapter.deleteRow(utilityBill.getUtilityBillId());

            ctm.getUtilityBillCollection().deleteBill(utilityBill);
            populateListView();
        } else {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_BILL_CODE:
                    break;
                case EDIT_BILL_CODE:
                    break;
                case DELETE_BILL_CODE:
                    break;
            }
        }
        populateListView();
    }

//    private void openBillDatabase() {
//        billDbAdapter = new DBBillAdapter(this);
//        billDbAdapter.open();
//    }
//
//    private void closeBillDatabase() {
//        billDbAdapter.close();
//    }
}
