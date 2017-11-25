package com.example.ibnshahid.lastthird;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by ibnShahid on 23/11/2017.
 */

public class Base extends AppCompatActivity {

    private MenuItem miTimeMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        miTimeMode = menu.findItem(R.id.time_mode);
        SharedPreferences sp = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        miTimeMode.setTitle(sp.getString("timeMode", getResources().getString(R.string.time_mode_24)));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.time_mode:
                if (miTimeMode.getTitle().toString().equals(getString(R.string.time_mode_24))) {
                    Utilities.getTime = Utilities.getTime24;
                    miTimeMode.setTitle(R.string.time_mode_12);
                } else {
                    Utilities.getTime = Utilities.getTime12;
                    miTimeMode.setTitle(R.string.time_mode_24);
                }
                SharedPreferences sp = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("timeMode", miTimeMode.getTitle().toString());
                editor.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
