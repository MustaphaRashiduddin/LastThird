package com.example.ibnshahid.lastthird;

import java.util.ArrayList;

/**
 * Created by ibnShahid on 25/10/2017.
 */

public class ManualAlarmTestData {
    public final static ArrayList<ManualAlarmModel> getData() {
        ManualAlarmModel manualAlarmModel1 = new ManualAlarmModel("2:56");
        ManualAlarmModel manualAlarmModel2 = new ManualAlarmModel("15:93");
        ManualAlarmModel manualAlarmModel3 = new ManualAlarmModel("lol");
        ArrayList<ManualAlarmModel> manualAlarmModelArrayList = new ArrayList<>();
        manualAlarmModelArrayList.add(manualAlarmModel1);
        manualAlarmModelArrayList.add(manualAlarmModel2);
        manualAlarmModelArrayList.add(manualAlarmModel3);
        return manualAlarmModelArrayList;
    }
}
