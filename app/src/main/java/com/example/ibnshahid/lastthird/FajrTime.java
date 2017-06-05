package com.example.ibnshahid.lastthird;
import java.util.Calendar;

/**
 * Created by ibnShahid on 05/06/2017.
 */

class FajrTime {
    private static final FajrTime ourInstance = new FajrTime();
    public static Calendar time;
    static FajrTime getInstance() { return ourInstance; }
    private FajrTime() { }

}
