package com.something.fitnessapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.something.fitnessapp.R;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

public class Signup extends AppCompatActivity {
    SlidingRootNav slidingRootNav;
    EditText email,username,password;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    TextView writemail;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private final int GPS_PERMISSION = 42;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        email=findViewById(R.id.mail);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);

      databaseReference = FirebaseDatabase.getInstance().getReference("Users");
      firebaseAuth=FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer)
                .inject();

 */

        if (ContextCompat.checkSelfPermission(Signup.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {



            // Permission is not granted
        }

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
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    GPS_PERMISSION);
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    public void login(View view){
        Intent intent=new Intent(Signup.this,Login.class);
        startActivity(intent);
    }
    public void signup(View view){
      //  Intent intent=new Intent(Signup.this,MainView.class);
        //startActivity(intent);
        final String emailaddress=email.getText().toString().trim();
        final String usr=username.getText().toString().trim();
        final String pass=password.getText().toString().trim();



        if (usr.isEmpty()) {
            username.setError(getString(R.string.input_error_name));
            username.requestFocus();
            return;
        }

        if (emailaddress.isEmpty()) {
            email.setError(getString(R.string.input_error_email));
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailaddress).matches()) {
            email.setError(getString(R.string.input_error_email_invalid));
            email.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            password.setError(getString(R.string.input_error_password));
            password.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            password.setError(getString(R.string.input_error_password_length));
            password.requestFocus();
            return;
        }






        firebaseAuth.createUserWithEmailAndPassword(emailaddress, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    emailaddress,
                                    usr,
                                    pass
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(Signup.this, "registration successful", Toast.LENGTH_LONG).show();



                                        startActivity(new Intent(getApplicationContext(), MainView.class));
                                    } else {
                                        Toast.makeText(Signup.this, "registration unsuccessful", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(1);
    }
}
