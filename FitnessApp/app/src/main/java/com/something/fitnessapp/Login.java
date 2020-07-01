package com.something.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

public class Login extends AppCompatActivity {
    SlidingRootNav slidingRootNav;
    private DatabaseReference ref;
    EditText em,password;
    String email,pass;
    TextView emailset;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        Toolbar toolbar = findViewById(R.id.toolbar);

     /*   setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer)
                .inject();
        //   toolbar.setTitle("Rice");


      */
        ref= FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth=FirebaseAuth.getInstance();

        em=findViewById(R.id.username);
        password=findViewById(R.id.password);

    }

    public void login(View view){
        email=em.getText().toString();
        pass=password.getText().toString();



        if (email.isEmpty()) {
            em.setError(getString(R.string.input_error_email));
            em.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            em.setError(getString(R.string.input_error_email_invalid));
            em.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            password.setError(getString(R.string.input_error_password));
            password.requestFocus();
            return;
        }

        if (pass.length() < 9) {
            password.setError(getString(R.string.input_error_password_length));
            password.requestFocus();
            return;
        }





        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Login Successfull.",
                                    Toast.LENGTH_SHORT).show();




                            startActivity(new Intent(getApplication(),MainView.class));

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(getApplicationContext(), "Login failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

      /*  if (ref.child(name)!=null) {
            ref.child(name).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                    if (name.equals(user.getUsername())) {
                        Toast.makeText(getApplicationContext(), name + "-logedin", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainView.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            Toast.makeText(getApplicationContext(), "Enter name to login", Toast.LENGTH_LONG).show();
        }

       */
    }

    public void registration(View view){
        startActivity(new Intent(getApplicationContext(),Signup.class));
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
