package com.something.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

public class Lunchoptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunchoptions);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        SlidingRootNav slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer)
                .inject();
        //   toolbar.setTitle("Rice");
    }
    public void formuscle(View view){
        Intent intent=new Intent(this,Lunchformuscle.class);
        startActivity(intent);
    }
    public void forweight(View view){
        Intent intent=new Intent(this,Lunchforweightloss.class);
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

}
