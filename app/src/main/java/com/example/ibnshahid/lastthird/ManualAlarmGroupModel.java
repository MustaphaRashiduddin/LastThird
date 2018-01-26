package com.example.ibnshahid.lastthird;

import java.util.Calendar;

/**
 * Created by ibnShahid on 24/10/2017.
 */

public class ManualAlarmGroupModel {
    public int pk;
    public int hr;
    public int min;
    public Boolean enabled;
    public Boolean mon;
    public Boolean tue;
    public Boolean wed;
    public Boolean thu;
    public Boolean fri;
    public Boolean sat;
    public Boolean sun;

    public ManualAlarmGroupModel() {
        this.pk = -1; // invalid id
        Calendar cal = Calendar.getInstance();
        this.hr = cal.get(Calendar.HOUR_OF_DAY);
        this.min = cal.get(Calendar.MINUTE);
        this.mon = false;
        this.tue = false;
        this.wed = false;
        this.thu = false;
        this.fri = false;
        this.sat = false;
        this.sun = false;
    }

    public ManualAlarmGroupModel(ManualAlarmGroupModel model) { // copy constructor
        this.pk = model.pk;
        this.hr = model.hr;
        this.min = model.min;
        this.enabled = model.enabled;
        this.mon = model.mon;
        this.tue = model.tue;
        this.wed = model.wed;
        this.thu = model.thu;
        this.fri = model.fri;
        this.sat = model.sat;
        this.sun = model.sun;
    }

    public ManualAlarmGroupModel(int pk, int hr, int min, int enabled,
                                 int mon, int tue, int wed, int thu, int fri,
                                 int sat, int sun) {
        this.pk = pk;
        this.hr = hr;
        this.min = min;
        this.enabled = (enabled != 0);
        this.mon = (mon != 0);
        this.tue = (tue != 0);
        this.wed = (wed != 0);
        this.thu = (thu != 0);
        this.fri = (fri != 0);
        this.sat = (sat != 0);
        this.sun = (sun != 0);
    }

    public void set(ManualAlarmGroupModel model) {
        this.pk = model.pk;
        this.hr = model.hr;
        this.min = model.min;
        this.mon = model.mon;
        this.tue = model.tue;
        this.wed = model.wed;
        this.thu = model.thu;
        this.fri = model.fri;
        this.sat = model.sat;
        this.sun = model.sun;
    }
}
