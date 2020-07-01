package com.something.fitnessapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

public class MainView extends AppCompatActivity {
    SlidingRootNav slidingRootNav;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer)
                .inject();
        //



        if(!isNetworkAvailable()){
            //Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing the App")
                    .setMessage("No Internet Connection,check your settings")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .show();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void muscle(View view){
        Intent intent=new Intent(MainView.this,MuscleBuilding.class);
        startActivity(intent);
    }
    public void weightloss(View view){
        Intent intent=new Intent(MainView.this,WeightLoss.class);
        startActivity(intent);
    }
    public void nutrition(View view){
        Intent intent=new Intent(MainView.this,Nutrition.class);
        startActivity(intent);
    }
    public void map(View view){
        Intent intent=new Intent(MainView.this,MapsActivity.class);
        startActivity(intent);
    }

    public void logout(View view){
        Signup signup=new Signup();
        signup.writemail.setText("");

        Signup user = new Signup();
        user.email.setText("");
        user.password.setText("");
        user.username.setText("");

        Intent intent=new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
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
    public void onBackPressed() {
        super.onBackPressed();


    }
}
