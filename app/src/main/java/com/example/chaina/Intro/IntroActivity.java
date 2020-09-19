package com.example.chaina.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chaina.MainActivity;
import com.example.chaina.R;

public class IntroActivity extends AppCompatActivity  {


    // UI elements.
    private Button mRequestLocationUpdatesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        mRequestLocationUpdatesButton = (Button) findViewById(R.id.request);
        mRequestLocationUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
            }
        });


    }


}