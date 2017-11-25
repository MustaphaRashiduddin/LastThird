package com.example.ibnshahid.lastthird;
import java.util.Calendar;

/**
 * Created by ibnShahid on 07/11/2017.
 */

public class LastThirdTime {
    private static final LastThirdTime ourInstance = new LastThirdTime();
    public static Calendar time;
    static LastThirdTime getInstance() { return ourInstance; }
    static void set(Calendar mTime) { time = mTime; }
    private LastThirdTime() { }
}
