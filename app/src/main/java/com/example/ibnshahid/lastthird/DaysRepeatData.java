package com.example.ibnshahid.lastthird;

import java.util.ArrayList;

/**
 * Created by ibnShahid on 28/11/2017.
 */

public class DaysRepeatData {
    ArrayList<DaysRepeatModel> daysRepeatModelArrayList = new ArrayList<>();
    public static ArrayList<DaysRepeatModel> defaultData() {
        ArrayList<DaysRepeatModel> daysRepeat = new ArrayList<>();
        DaysRepeatModel day1 = new DaysRepeatModel("Monday", false);
        DaysRepeatModel day2 = new DaysRepeatModel("Tuesday", false);
        DaysRepeatModel day3 = new DaysRepeatModel("Wednesday", false);
        DaysRepeatModel day4 = new DaysRepeatModel("Thursday", false);
        DaysRepeatModel day5 = new DaysRepeatModel("Friday", false);
        DaysRepeatModel day6 = new DaysRepeatModel("Saturday", false);
        DaysRepeatModel day7 = new DaysRepeatModel("Sunday", false);
        daysRepeat.add(day1);
        daysRepeat.add(day2);
        daysRepeat.add(day3);
        daysRepeat.add(day4);
        daysRepeat.add(day5);
        daysRepeat.add(day6);
        daysRepeat.add(day7);
        return daysRepeat;
    }

    DaysRepeatData(ManualAlarmGroupModel model) {
        DaysRepeatModel day1 = new DaysRepeatModel("Monday", model.mon);
        DaysRepeatModel day2 = new DaysRepeatModel("Tuesday", model.tue);
        DaysRepeatModel day3 = new DaysRepeatModel("Wednesday", model.wed);
        DaysRepeatModel day4 = new DaysRepeatModel("Thursday", model.thu);
        DaysRepeatModel day5 = new DaysRepeatModel("Friday", model.fri);
        DaysRepeatModel day6 = new DaysRepeatModel("Saturday", model.sat);
        DaysRepeatModel day7 = new DaysRepeatModel("Sunday", model.sun);
        daysRepeatModelArrayList.add(day1);
        daysRepeatModelArrayList.add(day2);
        daysRepeatModelArrayList.add(day3);
        daysRepeatModelArrayList.add(day4);
        daysRepeatModelArrayList.add(day5);
        daysRepeatModelArrayList.add(day6);
        daysRepeatModelArrayList.add(day7);
    }

    ArrayList<DaysRepeatModel> data() { return daysRepeatModelArrayList; }
}
