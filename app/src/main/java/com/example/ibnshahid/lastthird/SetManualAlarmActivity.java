package com.example.ibnshahid.lastthird;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by ibnShahid on 20/10/2017.
 */

public class SetManualAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_manual_alarm);

        ArrayList<ManualAlarmModel> data = ManualAlarmTestData.getData();
        AlarmsAdapter adapter = new AlarmsAdapter(this, 0, data);
        ListView listView = (ListView) findViewById(R.id.ll_list);
        listView.setAdapter(adapter);

    }
}
