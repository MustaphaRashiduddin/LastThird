package com.example.ibnshahid.lastthird;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ibnShahid on 20/10/2017.
 */

public class AlarmsAdapter extends ArrayAdapter<ManualAlarmGroupModel> {

    Context context;
    ArrayList<ManualAlarmGroupModel> data;
    public AlarmsAdapter(Activity activity, @LayoutRes int resource, ArrayList<ManualAlarmGroupModel> data) {
        super(activity, resource, data);
        this.data = data;
        context = activity;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_manual_alarm_group, parent, false);
        }

        TextView tvTime = (TextView) listItemView.findViewById(R.id.time);
        int hr = data.get(position).hr;
        int min = data.get(position).min;
        String time = Utilities.getTimeStr(hr, min);
        tvTime.setText(time);

        listItemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SetManualAlarmGroupActivity.class);
            intent.putExtra("ID", data.get(position).pk);
            intent.putExtra("SET_DEFAULT_ALARM", false);
            context.startActivity(intent);
        });

        CheckBox cbEnabled = (CheckBox) listItemView.findViewById(R.id.cb_enabled);
        cbEnabled.setChecked(data.get(position).enabled);

        cbEnabled.setOnClickListener(v -> {
            data.get(position).enabled = !data.get(position).enabled;
            DatabaseHelper db = new DatabaseHelper(context);
            db.updateEnabled(data.get(position).pk, data.get(position).enabled);
        });

        TextView tvDays = (TextView) listItemView.findViewById(R.id.tv_days);
        tvDays.setText(FetchAlarmData.getDays(getContext(), data.get(position).pk));

        return listItemView;
    }
}
