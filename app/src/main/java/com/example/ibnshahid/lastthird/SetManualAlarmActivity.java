package com.example.ibnshahid.lastthird;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

public class SetManualAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_set_manual_alarm);

        DatabaseHelper db = new DatabaseHelper(this);

        Button timeButton = (Button) findViewById(R.id.btn_time);
        Button repeatButton = (Button) findViewById(R.id.btn_repeat);

        int hour = getIntent().getIntExtra("hour", 5);
        int minute = getIntent().getIntExtra("minute", 0);

        android.app.TimePickerDialog.OnTimeSetListener timeSetListener = (view, hr, min) -> {
            Toast.makeText(this, "time: " + hr + " " + min, Toast.LENGTH_SHORT).show();
            if (!db.setAlarm(hr, min))
                Toast.makeText(this, "insert not successful", Toast.LENGTH_SHORT).show();
        };

        timeButton.setOnClickListener(v -> new android.app.TimePickerDialog(this, timeSetListener, hour,
                minute,false).show());

        repeatButton.setOnClickListener(v -> {
            Cursor res = db.getAlarms();
            if (res.getCount() == 0) {
                Toast.makeText(this, "no results", Toast.LENGTH_SHORT).show();
            } else {
                String lol = null;
                while (res.moveToNext()) { // I'm guessing this line moves one past -1 and then starts executing
                    lol = res.getString(1);
                    Toast.makeText(this, "" + lol, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
