package com.something.fitnessapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

public class TrackerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Build view */
        setContentView(R.layout.track_activity_main);

        /* Build Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Get layout elements */
        getLayoutElements();

        /* Setup GPS */
        if (checkPermission())
            lm = getLocationManager();

        /* Hide stop button by default */
        stop.hide();

        /* Start recording when start button is clicked only if GPS is authorised */
        start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (granted || checkPermission())
                    startRecording(view);
            }
        });

        /* Stop recording & show summary activity with retrieved data */
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(stopRecording());
            }
        });
    }


    private void getLayoutElements() {

        /* Get buttons */
        start = (FloatingActionButton) findViewById(R.id.start);
        stop = (FloatingActionButton) findViewById(R.id.stop);

        /* Get chronometer */
        chronometer = (Chronometer) findViewById(R.id.chronometer);

        /* Get Strings */
        currentSpeedString = getString(R.string.current_speed);
        totalDistanceString = getString(R.string.total_distance);
        averageSpeedString = getString(R.string.average_speed);
        elapsedTimeString = getString(R.string.elapsed_time);

        /* Get TextViews */
        currentSpeedTV = (TextView) findViewById(R.id.current_speed);
        totalDistanceTV = (TextView) findViewById(R.id.total_distance_rt);
        averageSpeedTV = (TextView) findViewById(R.id.average_speed_rt);
    }


    private boolean checkPermission() {

        /* Get GPS permission status */
        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        /* Ask for permission if GPS not authorised */
        if (check != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERMISSION);
        else granted = true;

        return (granted);
    }


    @Override /* Get request results. If request was cancelled, the result arrays are empty. */
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        /* Get view for Snackbar */
        View view = findViewById(R.id.content_main);

        /* Setup GPS if permission request was accepted & show success Snackbar */
        if (requestCode == GPS_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            granted = true;
            lm = getLocationManager();
            Snackbar.make(view, R.string.granted, Snackbar.LENGTH_LONG).show();
        } else Snackbar.make(view, R.string.denied, Snackbar.LENGTH_LONG).show();
    }


    /* Get Location Manager */
    private LocationManager getLocationManager() {
        return ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
    }


    /* Switch buttons, show Snackbar & start recording */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startRecording(View view) {

        switchButtons();

        /* Show Snackbar */
        Snackbar.make(view, R.string.start, Snackbar.LENGTH_LONG).show();

        /* Start chronometer to compute elapsed time */
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        /* Start gathering GPS positions */
        startGPS();
    }


    /* Switch buttons, stop recording & bundle data */
    private Intent stopRecording() {

        switchButtons();

        /* Stop gathering data */
        chronometer.stop();
        stopGPS();

        /* Create Intent object to launch summary activity with bundled data */
        Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);

        /* Bundle data */
        intent.putExtra(totalDistanceString, decimal.format(totalDistance) + kms);
        intent.putExtra(averageSpeedString, decimal.format(averageSpeed) + kmh);
        intent.putExtra(elapsedTimeString, getElapsedTime());
        intent.putExtra(getString(R.string.time_per_km), timePerKm);

        /* Send data to summary activity */
        return (intent);
    }


    private void switchButtons() {
        if (start.isShown()) {
            start.hide();
            stop.show();
        } else {
            start.show();
            stop.hide();
        }
    }

    private long getElapsedTime() {
        return (SystemClock.elapsedRealtime() - chronometer.getBase());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startGPS() {

        /* Request update every 400ms (Doherty Thresold) with 1 meter minimum distance difference */
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, listener);

        /* Instantiate lists */
        locations = new LinkedList<>();
        timePerKm = new ArrayList<>();

        /* Reset variables */
        totalDistance = 0;
        averageSpeed = 0;
        elapsedTime = 0;
        totalSpeed = 0;
        totalTime = 0;

        /* Reset TextViews */
        currentSpeedTV.setText(currentSpeedString);
        totalDistanceTV.setText(totalDistanceString);
        averageSpeedTV.setText(averageSpeedString);
    }

    /* Stop gathering positions */
    private void stopGPS() { lm.removeUpdates(listener); }

    /* GPS listener setup */
    private LocationListener listener = new LocationListener() {

        @Override /* Location actually changed */
        public void onLocationChanged(Location location) {

            /* Get new speed & convert m/s to km/h */
            double currentSpeed = location.getSpeed() * 3.6;

            /* Compute & update current speed */
            currentSpeedTV.setText(currentSpeedString + decimal.format(currentSpeed) + kmh);

            /* Compute & update average speed */
            totalSpeed += currentSpeed;
            averageSpeed = totalSpeed / (locations.size() + 1);
            averageSpeedTV.setText(averageSpeedString + decimal.format(averageSpeed) + kmh);

            /* Not moving */
            if (currentSpeed == 0)
                return;

            if (!locations.isEmpty()) {

                /* Compute & update total distance */
                int before = (int) totalDistance;
                totalDistance += (location.distanceTo(locations.peekLast()) / 1000);
                totalDistanceTV.setText(totalDistanceString + decimal.format(totalDistance) + kms);

                /* New kilometer */
                int done = (int) totalDistance - before;
                if (done > 0) {

                    /* Compute time elapsed since last kilometer if present & update total time */
                    long time = (timePerKm.isEmpty() ? getElapsedTime() : getElapsedTime() - totalTime);
                    totalTime += time;

                    /* Multiple kilometers at once */
                    /*if (done > 1) time /= done;
                    for (int i = 0; i < done; i++)*/
                        timePerKm.add(time);
                }
            }

            /* Save new position */
            locations.add(location);
        }

        @Override /* GPS sensor has been enabled */
        public void onProviderEnabled(String provider) {}

        @Override /* GPS sensor has been disabled */
        public void onProviderDisabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    /* Formatting variables */
    private String decimalPattern = "#.###";
    private String kmh = "km/h";
    private String kms = "km";

    /* GPS related variables */
    private boolean granted = false;
    private final int GPS_PERMISSION = 42;
    private LocationManager lm;
    private LinkedList<Location> locations;
    private double totalDistance, averageSpeed, totalSpeed;
    private ArrayList<Long> timePerKm;
    private long elapsedTime, totalTime;
    private DecimalFormat decimal = new DecimalFormat(decimalPattern);

    /* Layout elements */
    private FloatingActionButton start, stop;
    private Chronometer chronometer;
    private TextView currentSpeedTV, totalDistanceTV, averageSpeedTV;
    private String currentSpeedString, totalDistanceString, averageSpeedString, elapsedTimeString;
    public void homepage(View view){

        startActivity(new Intent(this,MainView.class));
    }
    public void musclepage(View view){

        startActivity(new Intent(this,MuscleBuilding.class));
    }
    public void weightpage(View view){

        startActivity(new Intent(this,WeightLoss.class));
    }
    public void nutritionpage(View view){

        startActivity(new Intent(this,Nutrition.class));
    }
    public void logoutpage(View view){

        startActivity(new Intent(this,Signup.class));
    }


}
