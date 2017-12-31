package com.example.ibnshahid.lastthird;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
    ManualAlarmModel manualAlarmModel;
    DaysRepeatModelListWrapper wrapper = new DaysRepeatModelListWrapper();

    private class DaysRepeatModelListWrapper {
        public ArrayList<DaysRepeatModel> data;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_days, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view).setPositiveButton("OK", (dialog, which) -> {
            ManualAlarmModel trueModel = ((SetManualAlarmActivity) getActivity()).manualAlarmModel;
            trueModel.set(manualAlarmModel);
            ((SetManualAlarmActivity) getActivity()).
                    tvRepeat.setText(FetchAlarmData.getDays(manualAlarmModel));
        }).setNegativeButton("Cancel", (dialog, which) -> onCancel(dialog));

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        manualAlarmModel = ((SetManualAlarmActivity) getActivity()).shadowManualAlarmModel;
        wrapper.data = new DaysRepeatData(manualAlarmModel).data();
        DaysAdapter adapter = new DaysAdapter(getActivity(), 0, wrapper.data, manualAlarmModel);
        ListView listView = (ListView) view.findViewById(R.id.ll_list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        manualAlarmModel.set(((SetManualAlarmActivity) getActivity()).manualAlarmModel);
    }

}
