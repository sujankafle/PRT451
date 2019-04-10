package com.Company.arpreschool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button);

        button1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OnBoardingScreen.class);
            startActivity(intent);
        });

        if( getIntent().getBooleanExtra("Exit", false)){

            finish();
        }

    }



}
