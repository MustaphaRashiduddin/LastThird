package com.example.ibnshahid.lastthird;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

public class SetManualAlarmActivity extends AppCompatActivity {

    protected ManualAlarmModel manualAlarmModel;
    protected ManualAlarmModel shadowManualAlarmModel; // to be manipulated in DaysDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_manual_alarm);

        DatabaseHelper db = new DatabaseHelper(this);

        Button timeButton = (Button) findViewById(R.id.btn_time);
        Button repeatButton = (Button) findViewById(R.id.btn_repeat);
        Button okButton = (Button) findViewById(R.id.btn_ok);
        Button deleteButton = (Button) findViewById(R.id.btn_delete);
        Button cancelButton = (Button) findViewById(R.id.btn_cancel);

        Boolean SET_DEFAULT_ALARM = getIntent().getBooleanExtra("SET_DEFAULT_ALARM", true);
        okButton.setOnClickListener(v -> {
            // update our database here
            if (SET_DEFAULT_ALARM) { // insert row
                if (!db.setAlarm(shadowManualAlarmModel.hr, shadowManualAlarmModel.min, shadowManualAlarmModel.enabled,
                        shadowManualAlarmModel.mon, shadowManualAlarmModel.tue, shadowManualAlarmModel.wed, shadowManualAlarmModel.thu,
                        shadowManualAlarmModel.fri, shadowManualAlarmModel.sat, shadowManualAlarmModel.sun))
                    Toast.makeText(this, "insert not successful", Toast.LENGTH_SHORT).show();
            } else { // update row
                db.updateAlarm(shadowManualAlarmModel.pk, shadowManualAlarmModel.hr, shadowManualAlarmModel.min, shadowManualAlarmModel.enabled,
                        shadowManualAlarmModel.mon, shadowManualAlarmModel.tue, shadowManualAlarmModel.wed, shadowManualAlarmModel.thu,
                        shadowManualAlarmModel.fri, shadowManualAlarmModel.sat, shadowManualAlarmModel.sun);
            }
            finish();
        });

        deleteButton.setOnClickListener( (View v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?");
            builder.setMessage("This action will delete your alarm");
            builder.setPositiveButton("OK", (dialog, which) -> {
                db.deleteAlarm(manualAlarmModel.pk);
                finish();
            });
            builder.setNegativeButton("CANCEL", (dialog, which) -> { /*no explicit action*/ });
            builder.show();
        });

        cancelButton.setOnClickListener(v -> finish());

        android.app.TimePickerDialog.OnTimeSetListener timeSetListener = (view, hr, min) -> {
            manualAlarmModel.hr = hr;
            manualAlarmModel.min = min;
        };

        int ID = getIntent().getIntExtra("ID", -1);
        if (SET_DEFAULT_ALARM) {
            manualAlarmModel = new ManualAlarmModel();
            deleteButton.setVisibility(View.GONE); // because we wanna delete *existing* alarms *only*
        } else {
            manualAlarmModel = FetchAlarmData.getAlarm(this, ID);
        }
        shadowManualAlarmModel = new ManualAlarmModel(manualAlarmModel);

        Boolean is24Hour = Utilities.getTime == Utilities.getTime24;
        timeButton.setOnClickListener(v -> new android.app.TimePickerDialog(this, timeSetListener,
                manualAlarmModel.hr, manualAlarmModel.min, is24Hour).show());

        repeatButton.setOnClickListener(v -> {
            DaysDialog dialog = new DaysDialog();
            dialog.show(getFragmentManager(), "dialog");
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        String json = gson.toJson(manualAlarmModel);
        outState.putString("MANUAL_ALARM_MODEL", json);

        json = gson.toJson(shadowManualAlarmModel);
        outState.putString("SHADOW", json);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Gson gson = new Gson();
        String json = savedInstanceState.getString("MANUAL_ALARM_MODEL");
        manualAlarmModel = gson.fromJson(json, ManualAlarmModel.class);

        json = savedInstanceState.getString("SHADOW");
        shadowManualAlarmModel = gson.fromJson(json, ManualAlarmModel.class);
    }
}
