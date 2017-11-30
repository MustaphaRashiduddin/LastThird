package com.example.ibnshahid.lastthird;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by ibnShahid on 27/11/2017.
 */

public class DaysDialog extends DialogFragment {

    LayoutInflater inflater;
    View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_days, null);

        ManualAlarmModel manualAlarmModel = ((SetManualAlarmActivity) getActivity()).manualAlarmModel;

        ArrayList<DaysRepeatModel> data = new DaysRepeatData(manualAlarmModel).data();
        DaysAdapter adapter = new DaysAdapter(getActivity(), 0, data);
        ListView listView = (ListView) view.findViewById(R.id.ll_list);
        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view).setPositiveButton("OK", (dialog, which) -> {
            manualAlarmModel.mon = data.get(0).repeat;
            manualAlarmModel.tue = data.get(1).repeat;
            manualAlarmModel.wed = data.get(2).repeat;
            manualAlarmModel.thu = data.get(3).repeat;
            manualAlarmModel.fri = data.get(4).repeat;
            manualAlarmModel.sat = data.get(5).repeat;
            manualAlarmModel.sun = data.get(6).repeat;
        }).setNegativeButton("Cancel", (dialog, which) -> { /* do nothing */ });

        return builder.create();
    }
}
