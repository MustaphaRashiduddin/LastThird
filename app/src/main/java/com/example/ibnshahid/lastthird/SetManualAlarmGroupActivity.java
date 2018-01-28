package com.example.ibnshahid.lastthird;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class SetManualAlarmGroupActivity extends AppCompatActivity {

    protected ManualAlarmGroupModel manualAlarmGroupModel;
    protected ManualAlarmGroupModel shadowManualAlarmGroupModel; // to be manipulated in DaysDialog
    TextView tvRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_manual_alarm_group);

        DatabaseHelper db = new DatabaseHelper(this);

        // initializing buttons
        Button timeButton = (Button) findViewById(R.id.btn_time);
        Button repeatButton = (Button) findViewById(R.id.btn_repeat);
        Button okButton = (Button) findViewById(R.id.btn_ok);
        Button deleteButton = (Button) findViewById(R.id.btn_delete);
        Button cancelButton = (Button) findViewById(R.id.btn_cancel);

        // initializing textviews
        tvRepeat = (TextView) findViewById(R.id.tv_repeat);

        Boolean SET_DEFAULT_ALARM = getIntent().getBooleanExtra("SET_DEFAULT_ALARM", true);
        okButton.setOnClickListener(v -> {
            // update our database here
            if (SET_DEFAULT_ALARM) { // insert row
                if (!db.setAlarmGroup(shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min,
                        shadowManualAlarmGroupModel.mon, shadowManualAlarmGroupModel.tue, shadowManualAlarmGroupModel.wed,
                        shadowManualAlarmGroupModel.thu, shadowManualAlarmGroupModel.fri, shadowManualAlarmGroupModel.sat,
                        shadowManualAlarmGroupModel.sun, shadowManualAlarmGroupModel)) {
                    Toast.makeText(this, "insert not successful", Toast.LENGTH_SHORT).show();
                } else {
                    updateAlarms(db);
                }
            } else { // update row
                db.updateAlarmGroup(shadowManualAlarmGroupModel.pk, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min,
                        shadowManualAlarmGroupModel.enabled, shadowManualAlarmGroupModel.mon, shadowManualAlarmGroupModel.tue,
                        shadowManualAlarmGroupModel.wed, shadowManualAlarmGroupModel.thu, shadowManualAlarmGroupModel.fri,
                        shadowManualAlarmGroupModel.sat, shadowManualAlarmGroupModel.sun);
                updateAlarms(db);
            }
            finish();
        });

        deleteButton.setOnClickListener( (View v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?");
            builder.setMessage("This action will delete your alarm");
            builder.setPositiveButton("OK", (dialog, which) -> {
                db.deleteAlarmGroup(manualAlarmGroupModel.pk);
                finish();
            });
            builder.setNegativeButton("CANCEL", (dialog, which) -> { /*no explicit action*/ });
            builder.show();
        });

        cancelButton.setOnClickListener(v -> finish());

        android.app.TimePickerDialog.OnTimeSetListener timeSetListener = (view, hr, min) -> {
            shadowManualAlarmGroupModel.hr = hr;
            shadowManualAlarmGroupModel.min = min;
        };

        int ID = getIntent().getIntExtra("ID", -1);
        if (SET_DEFAULT_ALARM) {
            manualAlarmGroupModel = new ManualAlarmGroupModel();
            deleteButton.setVisibility(View.GONE); // because we wanna delete *existing* alarms *only*
        } else {
            manualAlarmGroupModel = FetchAlarmData.getAlarm(this, ID);
        }
        shadowManualAlarmGroupModel = new ManualAlarmGroupModel(manualAlarmGroupModel);

        Boolean is24Hour = Utilities.getTime == Utilities.getTime24;
        timeButton.setOnClickListener(v -> new android.app.TimePickerDialog(this, timeSetListener,
                shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min, is24Hour).show());

        repeatButton.setOnClickListener(v -> {
            DaysDialog dialog = new DaysDialog();
            dialog.show(getFragmentManager(), "dialog");
        });
    }

    void updateAlarms(DatabaseHelper db) {
        if (shadowManualAlarmGroupModel.mon)
            db.setAlarm(0, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min, shadowManualAlarmGroupModel.pk);
        else
            db.deleteAlarm(0, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min);
        if (shadowManualAlarmGroupModel.tue)
            db.setAlarm(1, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min, shadowManualAlarmGroupModel.pk);
        else
            db.deleteAlarm(1, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min);
        if (shadowManualAlarmGroupModel.wed)
            db.setAlarm(2, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min, shadowManualAlarmGroupModel.pk);
        else
            db.deleteAlarm(2, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min);
        if (shadowManualAlarmGroupModel.thu)
            db.setAlarm(3, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min, shadowManualAlarmGroupModel.pk);
        else
            db.deleteAlarm(3, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min);
        if (shadowManualAlarmGroupModel.fri)
            db.setAlarm(4, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min, shadowManualAlarmGroupModel.pk);
        else
            db.deleteAlarm(4, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min);
        if (shadowManualAlarmGroupModel.sat)
            db.setAlarm(5, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min, shadowManualAlarmGroupModel.pk);
        else
            db.deleteAlarm(5, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min);
        if (shadowManualAlarmGroupModel.sun)
            db.setAlarm(6, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min, shadowManualAlarmGroupModel.pk);
        else
            db.deleteAlarm(6, shadowManualAlarmGroupModel.hr, shadowManualAlarmGroupModel.min);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvRepeat.setText(FetchAlarmData.getDays(shadowManualAlarmGroupModel));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        String json = gson.toJson(manualAlarmGroupModel);
        outState.putString("MANUAL_ALARM_MODEL", json);

        json = gson.toJson(shadowManualAlarmGroupModel);
        outState.putString("SHADOW", json);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Gson gson = new Gson();
        String json = savedInstanceState.getString("MANUAL_ALARM_MODEL");
        manualAlarmGroupModel = gson.fromJson(json, ManualAlarmGroupModel.class);

        json = savedInstanceState.getString("SHADOW");
        shadowManualAlarmGroupModel = gson.fromJson(json, ManualAlarmGroupModel.class);
    }
}
