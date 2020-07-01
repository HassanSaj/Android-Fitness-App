package com.something.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

public class Shoulderandtricep extends AppCompatActivity {
    SlidingRootNav slidingRootNav;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoulders);

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
