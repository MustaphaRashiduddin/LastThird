package com.example.ibnshahid.lastthird;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ibnShahid on 20/10/2017.
 */

public class AlarmsAdapter extends ArrayAdapter<ManualAlarmModel> {

    ArrayList<ManualAlarmModel> data;
    public AlarmsAdapter(Activity activity, @LayoutRes int resource, ArrayList<ManualAlarmModel> data) {
        super(activity, resource, data);
        this.data = data;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_manual_alarm, parent, false);
        }

        TextView tvTime = (TextView) listItemView.findViewById(R.id.time);
        int hr = data.get(position).hr;
        int min = data.get(position).min;
        String time = Utilities.getTimeStr(hr, min);
        tvTime.setText(time);

        return listItemView;
    }
}
