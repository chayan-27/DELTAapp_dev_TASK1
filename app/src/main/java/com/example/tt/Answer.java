package com.example.tt;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Answer extends AppCompatActivity {
    int scor3 = MainActivity.score1;
    int score4 = MainActivity.score3;
    TextView streak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView scorecounter = (TextView) findViewById(R.id.textview5);
        streak = (TextView) findViewById(R.id.textview6);
        scorecounter.setText("Score : " + scor3);
        MainActivity.score1=0;


        streak.setText("Longest Winning Streak : " + score4);


    }
}
