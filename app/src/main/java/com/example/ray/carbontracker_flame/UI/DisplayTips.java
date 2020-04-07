package com.example.ray.carbontracker_flame.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ray.carbontracker_flame.Model.CarbonTrackerModel;
import com.example.ray.carbontracker_flame.Model.Tip;
import com.example.ray.carbontracker_flame.R;

import java.util.List;

/**
 * Handles displaying a tip to the user
 */


public class DisplayTips extends AppCompatActivity {
    private CarbonTrackerModel ctm = CarbonTrackerModel.getInstance();
    private int position;

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, DisplayTips.class);
        Bundle extras2 = new Bundle();
        extras2.putInt("Position", 0);
        intent.putExtras(extras2);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tips);

        ctm.updateTips();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        position = extras.getInt("Position");
        List<Tip> tips = ctm.getTipsSaved();
        TextView textview = (TextView) findViewById(R.id.viewtips);

        if (tips.size() == 0) {
            finish();
            textview.setText(R.string.there_are_no_tips_yet);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.no_tips_yet);
            }
            position = -1;
            Button btn1 = (Button) findViewById(R.id.btnPrev);
            btn1.setVisibility(View.GONE);
            Button btn2 = (Button) findViewById(R.id.btnNext);
            btn2.setVisibility(View.GONE);
        } else {
            textview.setText(tips.get(position).getMessage());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Tip " + (position + 1));
            }
        }
        setupOkBtn();
        setupNextBtn();
        setupPrevBtn();
        if (position <= 0) {
            Button btn1 = (Button) findViewById(R.id.btnPrev);
            btn1.setVisibility(View.GONE);
        }
        if (position >= tips.size() - 1) {
            Button btn2 = (Button) findViewById(R.id.btnNext);
            btn2.setVisibility(View.GONE);
        }
    }

    private void setupNextBtn() {
        Button btn = (Button) findViewById(R.id.btnNext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle extras = new Bundle();
                extras.putInt("Position", position + 1);
                intent.putExtras(extras);
                finish();
                startActivity(intent);
            }
        });
    }

    private void setupPrevBtn() {
        Button btn = (Button) findViewById(R.id.btnPrev);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle extras = new Bundle();
                extras.putInt("Position", position - 1);
                intent.putExtras(extras);
                finish();
                startActivity(intent);

            }
        });
    }

    private void setupOkBtn() {
        Button btn = (Button) findViewById(R.id.btnOk);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
