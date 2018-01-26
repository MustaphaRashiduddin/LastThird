package com.example.ibnshahid.lastthird;

import android.app.Activity;
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

public class DaysAdapter extends ArrayAdapter<DaysRepeatModel> {

    ArrayList<DaysRepeatModel> data;
    ManualAlarmGroupModel model;
    public DaysAdapter(Activity activity, @LayoutRes int resource, ArrayList<DaysRepeatModel> data, ManualAlarmGroupModel model) {
        super(activity, resource, data);
        this.data = data;
        this.model = model;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_day, parent, false);
        }

        TextView tvDay = (TextView) listItemView.findViewById(R.id.tv_day);
        CheckBox cbRepeat = (CheckBox) listItemView.findViewById(R.id.cb_repeat);
        String day = data.get(position).day;
        Boolean repeat = data.get(position).repeat;
        tvDay.setText(day);
        cbRepeat.setChecked(repeat);

        cbRepeat.setOnClickListener(v -> {
            data.get(position).repeat = !data.get(position).repeat;
            switch (position) { // I know it's too much coupling, but I ain't using this adapter elsewhere
                case 0:
                    model.mon = data.get(position).repeat;
                    break;
                case 1:
                    model.tue = data.get(position).repeat;
                    break;
                case 2:
                    model.wed = data.get(position).repeat;
                    break;
                case 3:
                    model.thu = data.get(position).repeat;
                    break;
                case 4:
                    model.fri = data.get(position).repeat;
                    break;
                case 5:
                    model.sat = data.get(position).repeat;
                    break;
                case 6:
                    model.sun = data.get(position).repeat;
                    break;
            }
        });

        return listItemView;
    }
}

