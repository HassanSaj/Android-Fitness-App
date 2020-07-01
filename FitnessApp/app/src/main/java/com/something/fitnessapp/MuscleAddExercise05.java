package com.something.fitnessapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MuscleAddExercise05 extends AppCompatActivity {
    TextView textView;
    ImageView editbutton,image,picture;

    private  static final int PERMISSION_CODE=1001;
    private static final int IMAGE_PICK_CODE=1000;
    AlertDialog alertDialog;
    EditText editText,name;
    Button button;
    String detail,exercise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_add_exercise01);


        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);


        button=new Button(this);
        button.setText("select image");

        name=new EditText(this);
        name.setHint("Exercise Name");

        editText = new EditText(this);
        editText.setHint("Add Detail");

        layout.addView(button);
        layout.addView(name);
        layout.addView(editText);

        textView =findViewById(R.id.text);
        editbutton=findViewById(R.id.edit);
        image=findViewById(R.id.image);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){

                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }else{
                        pickimagefromgallery();
                    }

                }
                else {
                    pickimagefromgallery();
                }
            }
        });

/////////////////////////////retrieving text////////////////////////////////
        SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(this);
        String getdetail=pref.getString("detail","");
        textView.setText(getdetail);

        String geturl=pref.getString("uri5","");
        image.setImageURI(Uri.parse(geturl));

        //////////////////retrieving text///////////////////////////////

        ////////////////text view///////////////


        alertDialog=new AlertDialog.Builder(this).create();
        // editText=new EditText(this);
        //name=new EditText(this);

        alertDialog.setTitle("Add Exercise");
        alertDialog.setView(layout);



        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(editText.getText());

                //////save///////
                exercise=name.getText().toString();
                detail=editText.getText().toString();

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MuscleAddExercise05.this);
                SharedPreferences.Editor editor=pref.edit();

                editor.putString("detail",detail);
                editor.putString("exercise5",exercise);
                editor.apply();


            }
        });

    /*   textView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               editText.setText(textView.getText());
               alertDialog.show();
           }
       });

     */
        ////////text view///////////////////////////////


        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(textView.getText());
                alertDialog.show();
            }
        });


    }

    private void pickimagefromgallery() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickimagefromgallery();
                }else {
                    Toast.makeText(getApplicationContext(),"permission denied",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MuscleAddExercise05.this);
            SharedPreferences.Editor editor=pref.edit();
            Uri uri=data.getData();
            editor.putString("uri5", String.valueOf(uri));
            editor.apply();
            image.setImageURI(data.getData());
        }
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
        startActivity(new Intent(this,MuscleBuilding.class));
    }
}
