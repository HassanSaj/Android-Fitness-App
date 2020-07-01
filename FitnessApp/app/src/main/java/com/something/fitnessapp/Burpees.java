package com.something.fitnessapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.concurrent.TimeUnit;

public class Burpees extends AppCompatActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener  {


    private long timeCountInMilliSeconds = 1 * 60000;
    Button setting;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadSettings();
        setTimer();
    }

    private enum TimerStatus {
        STARTED,
        STOPPED,
    }

    int breakCount = 0;
    boolean breakOrWork = false;
    private Burpees.TimerStatus timerStatus = Burpees.TimerStatus.STOPPED;
    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private ImageView imageViewTomato, imageViewWork, imageViewBreak;
    // private ImageView imageViewPomodora1, imageViewPomodora2, imageViewPomodora3, imageViewPomodora4;
    private CountDownTimer countDownTimer;
    private boolean  vibration;
    private SharedPreferences settings;
    private Vibrator vibrator;

    //hamburger menu
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    ImageView imageViewPic;
    TextView textViewName,textViewEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burpees);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        SlidingRootNav slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer)
                .inject();
        //
        // method call to initialize the views
        initViews();

        //Set vibrate feature
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Toggle menu
      /*  actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar();


       */
        //Taking Navigation menu settings


        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Taking to feature from the setting menu
        loadSettings();

        //ActionBar set
       /* ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setTitle("Pomodoro");
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));


        */





        // method call to initialize the listeners
        initListeners();
        //method call to initialize the settings menu item
        setTimer();
    }



    /**
     * method to initialize the settings menu item
     */
    private void setTimer() {
        Long work = Long.valueOf(Integer.parseInt(settings.getString("work_duration", "1")) * 60000);
        textViewTime.setText(hmsTimeFormatter(work));
    }

    /**
     * Method to take settings from the setting menu
     */
    private void loadSettings() {
        vibration = settings.getBoolean("vibration", false);
        settings.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * method to initialize the views
     */
    private void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        progressBarCircle = findViewById(R.id.progressBarCircle);
        textViewTime = findViewById(R.id.textViewTime);
        imageViewReset = findViewById(R.id.imageViewReset);
        imageViewStartStop = findViewById(R.id.imageViewStartStop);
        imageViewTomato = findViewById(R.id.imageViewTomato);
        imageViewBreak = findViewById(R.id.imageViewBreak);
        imageViewWork = findViewById(R.id.imageViewWork);
    }

    /**
     * method to initialize the click listeners
     */
    private void initListeners() {
        imageViewReset.setOnClickListener(this);
        imageViewStartStop.setOnClickListener(this);
        imageViewTomato.setOnClickListener(this);
    }

    /**
     * implemented method to listen clicks
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewReset:
                reset();
                break;
            case R.id.imageViewStartStop:
                workstartStop();
                break;
            case R.id.imageViewTomato:
                visibleButton();
                workstartStop();
                break;
        }
    }

    /**
     * method to visible start stop icon
     */
    public void visibleButton() {
        imageViewStartStop.setVisibility(View.VISIBLE);
        imageViewTomato.setVisibility(View.GONE);
    }

    /**
     * method to reset count down timer
     */
    private void reset() {
        breakCount = 0;
        stopCountDownTimer();
        //startCountDownTimer();
        textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
        // call to initialize the progress bar values
        setProgressBarValues();
        //hiding break and work icon
        imageViewBreak.setVisibility(View.GONE);
        imageViewWork.setVisibility(View.GONE);
        // changing stop icon to start icon
        imageViewStartStop.setImageResource(R.drawable.onimage);
        // changing the timer status to stopped
        timerStatus = Burpees.TimerStatus.STOPPED;
    }


    /**
     * method to start and stop count down timer
     */
    private void workstartStop() {

        breakOrWork = true;
        if (timerStatus == Burpees.TimerStatus.STOPPED) {

            // call to initialize the timer values
            WorkSetTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // showing the work icon
            imageViewWork.setVisibility(View.VISIBLE);
            // showing the reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
            imageViewStartStop.setImageResource(R.drawable.stopimage);
            // changing the timer status to started
            timerStatus = Burpees.TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();


        } else {
            // changing stop icon to start icon
            imageViewStartStop.setImageResource(R.drawable.onimage);
            // changing the timer status to stopped
            timerStatus = Burpees.TimerStatus.STOPPED;
            stopCountDownTimer();

        }

    }

    /**
     * method to initialize the values for count down timer work
     */
    private void WorkSetTimerValues() {
        int time;
        time = Integer.parseInt(settings.getString("work_duration", "1"));
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 1000;
    }

    /**
     * method to initialize the values for count down timer
     */
    private void BreakSetTimerValues() {
        int time;

        // fetching value from edit text and type cast to integer
        time = Integer.parseInt(settings.getString("break_duration", "1"));

        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 1000;
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @SuppressLint("MissingPermission")
            @Override
            public void onFinish() {
                //The count check end of the task
                breakCount++;
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                setProgressBarValues();
                // hiding the work icon
                imageViewWork.setVisibility(View.GONE);
                // hiding the break icon
                imageViewBreak.setVisibility(View.GONE);
                // changing stop icon to start icon
                imageViewStartStop.setImageResource(R.drawable.onimage);
                // changing the timer status to stopped
                timerStatus = Burpees.TimerStatus.STOPPED;
                //Vibration
                vibration = settings.getBoolean("vibration", true);

                if (vibration) vibrator.vibrate(1000);

                //checking work and break times
                checkBreakOrWork();
            }
        }.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            scheduleNotification(getNotification("Time is over!!"), timeCountInMilliSeconds);
        }
    }

    /**
     * method to set the alarm
     *
     * @param notification
     * @param delay
     */
    private void scheduleNotification(Notification notification, long delay) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = System.currentTimeMillis() + delay;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);

    }


    /**
     * method to creating notification
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification getNotification(String content) {
        Intent notificationIntent = new Intent(this, Timer_MainActivity.class);
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification builder = new Notification.Builder(this, "default")
                .setContentTitle("Fitness App")
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setVibrate(new long[]{500, 500, 500, 500, 500})
                .setSmallIcon(R.drawable.startimage).getNotification();
        return builder;
    }


    /**
     * method check break and work duration
     */
    public void checkBreakOrWork() {
        if (breakCount != 8) {
            if (breakOrWork) {
                breakAlert();
                // System.out.println("************************ break");
            } else if (!breakOrWork) {
                workAlert();
                //  System.out.println("************************ work");
            } else {
                Toast.makeText(getApplicationContext(), "Finish", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Pomodoro is over", Toast.LENGTH_LONG).show();
            reset();

        }

    }

    /**
     * method to show alert take a break
     */
    public void breakAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Good job! Would you like  to take a break");

        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                workstartStop();
            }
        });

        alertDialogBuilder.setNegativeButton("Take a break", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                breakStartStop();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * method to show alert take a break
     */
    public void workAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("The break is over! Now working time");
        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                workstartStop();
            }
        });

        alertDialogBuilder.setNegativeButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * method to start and stop count down timer break
     */
    private void breakStartStop() {
        breakOrWork = false;
        if (timerStatus == Burpees.TimerStatus.STOPPED) {

            // call to initialize the timer values
            BreakSetTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // showing the break icon
            imageViewBreak.setVisibility(View.VISIBLE);
            // showing the reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
            imageViewStartStop.setImageResource(R.drawable.stopimage);
            // making edit text not editable
            // changing the timer status to started
            timerStatus = Burpees.TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();

        } else {
            breakCount = 0;
            // changing stop icon to start icon
            imageViewStartStop.setImageResource(R.drawable.startimage);
            // changing the timer status to stopped
            timerStatus = Burpees.TimerStatus.STOPPED;
            stopCountDownTimer();

        }

    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //   getMenuInflater().inflate(R.menu.menu_main, menu);
        //  loadInformation();
        return true;
    }

    /**
     * method to get profile pic. and name
     */


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    public void set(View view){
        Intent settingsIntent = new Intent(getApplicationContext(), Timer_Settings.class);
        startActivity(settingsIntent);
    }

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
