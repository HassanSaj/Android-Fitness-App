package com.something.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

public class WeightLoss extends AppCompatActivity {
    SlidingRootNav slidingRootNav;
    TextView exercise1,exercise2,exercise3,exercise4,exercise5;
    ImageView image,image2,image3,image4,image5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_loss);
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
        exercise1=findViewById(R.id.exercise);
        exercise2=findViewById(R.id.exercise2);
        exercise3=findViewById(R.id.exercise3);
        exercise4=findViewById(R.id.exercise4);
        exercise5=findViewById(R.id.exercise5);

        image=findViewById(R.id.image);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        image4=findViewById(R.id.image4);
        image5=findViewById(R.id.image5);


        MuscleAddExercise01 obj;
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        String getname=pref.getString("weightexercise","Add");
        exercise1.setText(getname);
        String getname2=pref.getString("weightexercise2","Add");
        exercise2.setText(getname2);
        String getname3=pref.getString("weightexercise3","Add");
        exercise3.setText(getname3);
        String getname4=pref.getString("weightexercise4","Add");
        exercise4.setText(getname4);
        String getname5=pref.getString("weightexercise5","Add");
        exercise5.setText(getname5);


     /*   String geturl=pref.getString("weighturi","");
        image.setImageURI(Uri.parse(geturl));
        String geturl2=pref.getString("weighturi2","");
        image2.setImageURI(Uri.parse(geturl2));
        String geturl3=pref.getString("weighturi3","");
        image3.setImageURI(Uri.parse(geturl3));
        String geturl4=pref.getString("weighturi4","");
        image4.setImageURI(Uri.parse(geturl4));
        String geturl5=pref.getString("weighturi5","");
        image5.setImageURI(Uri.parse(geturl5));

      */
    }
    public void sprint(View view){
        Intent intent=new Intent(this,Sprint.class);
        startActivity(intent);
    }
    public void battleropes(View view){
        Intent intent=new Intent(this,Battleropes.class);
        startActivity(intent);
    }
    public void burpees(View view){
        Intent intent=new Intent(this,Burpees.class);
        startActivity(intent);
    }
    public void skipping(View view){
        Intent intent=new Intent(this,Skipping.class);
        startActivity(intent);
    }
    public void pushups(View view){
        Intent intent=new Intent(this,Pushups.class);
        startActivity(intent);
    }
    public void situps(View view){
        Intent intent=new Intent(this,Pushups.class);
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
    public void exercise1(View view){
        Intent intent =new Intent(this,WeightAddExercise01.class);
        startActivity(intent);
    }
    public void exercise2(View view){
        Intent intent =new Intent(this,WeightAddExercise02.class);
        startActivity(intent);
    }
    public void exercise3(View view){
        Intent intent =new Intent(this,WeightAddExercise03.class);
        startActivity(intent);
    }
    public void exercise4(View view){
        Intent intent =new Intent(this,WeightAddExercise04.class);
        startActivity(intent);
    }
    public void exercise5(View view){
        Intent intent =new Intent(this,WeightAddExercise05.class);
        startActivity(intent);
    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainView.class));
    }
}
