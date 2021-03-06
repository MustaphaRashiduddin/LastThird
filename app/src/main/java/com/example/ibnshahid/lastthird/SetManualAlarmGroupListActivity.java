package com.example.ibnshahid.lastthird;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by ibnShahid on 20/10/2017.
 */

public class SetManualAlarmGroupListActivity extends Base {

    private AlarmsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_manual_alarm_group_list);

        Button addAlarm = (Button) findViewById(R.id.btn_add_alarm);
        addAlarm.setOnClickListener(v -> {
            Intent intent = new Intent(SetManualAlarmGroupListActivity.this, SetManualAlarmGroupActivity.class);
            intent.putExtra("SET_DEFAULT_ALARM", true);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<ManualAlarmGroupModel> data = FetchAlarmData.allAlarms(this);
        adapter = new AlarmsAdapter(this, 0, data);
        ListView listView = (ListView) findViewById(R.id.ll_list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean invariant = super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.time_mode:
                adapter.notifyDataSetChanged();
                break;
        }
        return invariant;
    }
}
