package com.something.fitnessapp;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Retrieve recorded data */
        Bundle extras = getIntent().getExtras();
        Graph.timePerKm = extras.getParcelableArrayList(getString(R.string.time_per_km));

        /* Compute max time per kilometer */
        if (Graph.timePerKm != null && !Graph.timePerKm.isEmpty())
            Graph.maxTime = (long) Collections.max(Graph.timePerKm);

        /* Build view */
        setContentView(R.layout.activity_summary);

        /* Build Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.stats);
        setSupportActionBar(toolbar);

        /* Get Strings */
        String totalDistanceString = getString(R.string.total_distance);
        String averageSpeedString = getString(R.string.average_speed);
        String elapsedTimeString = getString(R.string.elapsed_time);
        String timePerKmString = getString(R.string.time_per_km);

        /* Update TextViews */
        changeText((TextView) findViewById(R.id.total_distance), totalDistanceString, extras.getString(totalDistanceString));
        changeText((TextView) findViewById(R.id.average_speed), averageSpeedString, extras.getString(averageSpeedString));
        changeText((TextView) findViewById(R.id.elapsed_time), elapsedTimeString, longToTime(extras.getLong(elapsedTimeString)));
        changeText((TextView) findViewById(R.id.time_per_km), timePerKmString, "(max: " + longToTime(Graph.maxTime) + ")");

        /* Go back to main activity when back button is clicked */
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });
    }

    private String longToTime(long time) {

        /* Setup time String format & convert */
        Locale locale = getResources().getConfiguration().locale;
        SimpleDateFormat date = new SimpleDateFormat("mm:ss:SSS", locale);
        String str = date.format(new Date(time));

        /* Replace dots by units & append ms unit */
        str = str.replaceFirst(":", "m");
        str = str.replaceFirst(":", "s");
        return (str + "ms");
    }

    /* Append desired Object to specific TextView */
    private void changeText(TextView tv, String str, Object append) { tv.setText(str + append); }
}
