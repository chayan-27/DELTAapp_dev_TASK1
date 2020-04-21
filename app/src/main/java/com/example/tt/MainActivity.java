package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.example.tt.R.layout.new_layout;

public class MainActivity extends AppCompatActivity {
    public static int score1 = 0;
    public static int score2 = 0;
    public static int score3 = 0;

    public static long start_time = 10000;
    private TextView mTextViewCountdown;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    public long mtime_left = start_time;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewCountdown = (TextView) findViewById(R.id.text_view_countdown);
        mTextViewCountdown.setVisibility(View.INVISIBLE);
        Button first = (Button) findViewById(R.id.button);
        Button second = (Button) findViewById(R.id.button5);
        Button fourth = (Button) findViewById(R.id.button2);

        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Instruct.class);
                startActivity(intent);
            }
        });
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final RelativeLayout third = (RelativeLayout) findViewById(R.id.bcgd_color);

        final SharedPreferences myScore = this.getSharedPreferences("MyStreakScore", Context.MODE_PRIVATE);
        score3 = myScore.getInt("score", 0);
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Answer.class);
                startActivity(intent);

            }
        });
        final EditText editText = (EditText) findViewById(R.id.editText2);

            first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    third.setBackgroundColor(Color.parseColor("#DC7A39"));
                    editText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            third.setBackgroundColor(Color.parseColor("#DC7A39"));
                        }
                    });
                    final ListView lst = (ListView) findViewById(R.id.lst2);
                    try {
                        final String number = editText.getText().toString();
                        editText.setEnabled(false);
                        ArrayList<String> factors = new ArrayList<>();
                        int c = 0;

                        final int num = Integer.parseInt(number);
                        for (int i = 1; i <= num; i++) {
                            if (num % i == 0) {
                                factors.add(Integer.toString(i));
                                ++c;

                            }
                        }
                        System.out.println(c);
                        int ar[] = new int[c];
                        int j = 0;
                        for (int i = 1; i <= num; i++) {
                            if (num % i == 0) {
                                ar[j] = i;
                                ++j;
                            }
                        }
                        int rnd = (int) ((Math.random() * (((j - 1) - 0) + 1)) + 0);
                        int rnd2 = ar[rnd];
                        int min = 1;
                        int max = num + 2;
                        int r1, r2;
                        do {
                            r1 = (int) ((Math.random() * ((max - min) + 1)) + min);
                            r2 = (int) ((Math.random() * ((max - min) + 1)) + min);

                        } while (((num % r1) == 0) || ((num % r2) == 0) || (r1 == r2));


                        ArrayList<Integer> opt = new ArrayList<>();
                        opt.add(rnd2);
                        opt.add(r1);
                        opt.add(r2);
                        Collections.shuffle(opt);
                        final int ans = rnd2;


                        final ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, R.layout.new_layout, opt);
                        lst.setAdapter(adapter);
                        mTextViewCountdown.setVisibility(View.VISIBLE);
                        startTimer();

                        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String selected = ((TextView) view.findViewById(R.id.optu)).getText().toString();
                                int numb = Integer.parseInt(selected);
                                int numb2 = Integer.parseInt(number);
                                pauseTimer();
                                resetTimer();
                                mTextViewCountdown.setVisibility(View.INVISIBLE);
                                editText.setEnabled(true);

                                String check = "";
                                if ((numb2 % numb) == 0) {
                                    third.setBackgroundColor(Color.GREEN);
                                    check = "Correct Answer";

                                    score1 = score1 + 1;
                                    score2 = score2 + 1;
                                    if (score2 > score3) {
                                        score3 = score2;

                                        SharedPreferences.Editor editor = myScore.edit();
                                        editor.putInt("score", score3);
                                        editor.commit();

                                    }

                                } else {
                                    third.setBackgroundColor(Color.RED);
                                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                                    check = "Wrong Answer,Correct answer is " + ans;
                                    if (score2 > score3) {
                                        score3 = score2;

                                        SharedPreferences.Editor editor = myScore.edit();
                                        editor.putInt("score", score3);
                                        editor.commit();

                                    }
                                    score2 = 0;

                                }


                                Toast.makeText(MainActivity.this, check, Toast.LENGTH_LONG).show();
                                adapter.clear();
                                editText.setText("");


                            }
                        });


                    }catch(Exception e)
                    {
                        Toast.makeText(MainActivity.this,"Enter a valid number",Toast.LENGTH_LONG).show();
                        editText.setEnabled(true);
                    }
                }
            });

    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mtime_left, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mtime_left = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(MainActivity.this, Answer.class);
                startActivity(intent);
                resetTimer();



            }
        }.start();
        mTimerRunning = true;
    }

    private void updateCountDownText() {
        int seconds = (int) (mtime_left / 1000);
        String ut = Integer.toString(seconds);
        mTextViewCountdown.setText(ut);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    private void resetTimer() {
        mtime_left = start_time;
        updateCountDownText();
    }
}
