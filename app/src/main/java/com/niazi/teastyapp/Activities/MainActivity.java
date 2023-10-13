package com.niazi.teastyapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.niazi.teastyapp.R;
import com.niazi.teastyapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private static final String PREFS_NAME = "MyPrefs";


    FirebaseAuth firebaseAuth;
    private static final String FIRST_LAUNCH_KEY = "firstLaunch";
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseAuth =FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, Seller_Data_Activity.class);
            startActivity(intent);
            finish();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // If not signed in, navigate back to the sign-in activity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        binding.buyer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                    // Save sign-in status in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    // Navigate to the main activity or dashboard

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);

                    finish();


            }
        });
        binding.seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Sign_In_Activity.class);
                startActivity(intent);

            }
        });



    }

}
