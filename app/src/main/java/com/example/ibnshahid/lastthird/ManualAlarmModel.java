package com.example.ibnshahid.lastthird;

/**
 * Created by ibnShahid on 24/10/2017.
 */

public class ManualAlarmModel {
    public int pk;
//    public String time;
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

    public ManualAlarmModel(int pk, int hr, int min, int enabled,
                            int mon, int tue, int wed, int thu, int fri,
                            int sat, int sun) {
        this.pk = pk;
//        this.time = time;
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
}
