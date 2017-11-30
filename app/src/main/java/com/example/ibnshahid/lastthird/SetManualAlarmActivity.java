package com.example.ibnshahid.lastthird;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

public class SetManualAlarmActivity extends AppCompatActivity {

    protected ManualAlarmModel manualAlarmModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_manual_alarm);

        DatabaseHelper db = new DatabaseHelper(this);

        Button timeButton = (Button) findViewById(R.id.btn_time);
        Button repeatButton = (Button) findViewById(R.id.btn_repeat);
        Button okButton = (Button) findViewById(R.id.btn_ok);
        Button cancelButton = (Button) findViewById(R.id.btn_cancel);

        Boolean SET_DEFAULT_ALARM = getIntent().getBooleanExtra("SET_DEFAULT_ALARM", true);
        okButton.setOnClickListener(v -> {
            // update our database here
            if (SET_DEFAULT_ALARM) { // insert row
                if (!db.setAlarm(manualAlarmModel.hr, manualAlarmModel.min, manualAlarmModel.enabled,
                        manualAlarmModel.mon, manualAlarmModel.tue, manualAlarmModel.wed, manualAlarmModel.thu,
                        manualAlarmModel.fri, manualAlarmModel.sat, manualAlarmModel.sun))
                    Toast.makeText(this, "insert not successful", Toast.LENGTH_SHORT).show();
            } else { // update row
                db.updateAlarm(manualAlarmModel.pk, manualAlarmModel.hr, manualAlarmModel.min, manualAlarmModel.enabled,
                        manualAlarmModel.mon, manualAlarmModel.tue, manualAlarmModel.wed, manualAlarmModel.thu,
                        manualAlarmModel.fri, manualAlarmModel.sat, manualAlarmModel.sun);
            }
        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(this, "cancelButton", Toast.LENGTH_SHORT).show();
        });

        android.app.TimePickerDialog.OnTimeSetListener timeSetListener = (view, hr, min) -> {
            manualAlarmModel.hr = hr;
            manualAlarmModel.min = min;
        };

        int ID = getIntent().getIntExtra("ID", -1);
        if (SET_DEFAULT_ALARM) {
            manualAlarmModel = new ManualAlarmModel();
        } else {
            manualAlarmModel = FetchAlarmData.getAlarm(this, ID);
        }

        Boolean is24Hour = Utilities.getTime == Utilities.getTime24;
        timeButton.setOnClickListener(v -> new android.app.TimePickerDialog(this, timeSetListener,
                manualAlarmModel.hr, manualAlarmModel.min, is24Hour).show());

        repeatButton.setOnClickListener(v -> {
            DaysDialog dialog = new DaysDialog();
            dialog.show(getFragmentManager(), "dialog");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
