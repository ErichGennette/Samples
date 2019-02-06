package com.example.erich.m12_asynctask;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ProgressBar eventProgressBar;
    ProgressBar timeProgressBar;
    Button quicktime;
    Button startEvent;
    TextView text;
    Integer maxCount = 100;
    Integer count = maxCount;
    int quicktimeValue = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.output);
        text.setVisibility(View.GONE);

        eventProgressBar = (ProgressBar) findViewById(R.id.eventProgressBar);
        eventProgressBar.setMax(100);
        eventProgressBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));

        timeProgressBar = (ProgressBar) findViewById(R.id.timeProgressBar);
        timeProgressBar.setMax(100);
        resetProgressBars();

        startEvent = (Button) findViewById(R.id.startEvent);
        View.OnClickListener startListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuicktime();
            }
        };
        startEvent.setOnClickListener(startListener);

        quicktime = (Button) findViewById(R.id.quicktime);
        View.OnClickListener quicktimeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventProgressBar.setProgress(eventProgressBar.getProgress() - quicktimeValue);
            }
        };
        quicktime.setOnClickListener(quicktimeListener);
        quicktime.setVisibility(View.GONE);
    }

    class QuicktimeEvent extends AsyncTask<Integer, Double, Double> {

        @Override
        protected Double doInBackground(Integer[] ints) {
            for(; count > ints[0]; count--) {
                if(eventProgressBar.getProgress() > 0) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    publishProgress(Double.parseDouble(count.toString()));
                } else
                    break;
            }
            return Double.parseDouble(count.toString());
        }

        @Override
        protected void onProgressUpdate(Double[] param) {
            timeProgressBar.setProgress(count);
            ColorStateList barColor = ColorStateList.valueOf(Color.GREEN);
            if(count <= 66 && count >= 33)
                barColor = ColorStateList.valueOf(Color.BLUE);
            if(count < 33)
                barColor = ColorStateList.valueOf(Color.RED);
            timeProgressBar.setProgressTintList(barColor);
        }

        @Override
        protected void onPostExecute(Double result) {
            resetProgressBars();
            startEvent.setVisibility(View.VISIBLE);
            quicktime.setVisibility(View.GONE);
            String resultString;
            if(result == 0)
                resultString = "You failed the quicktime event..";
            else
                resultString = "You beat the quicktime event with " + result/20 + " seconds left!";
            text.setText(resultString);
            text.setVisibility(View.VISIBLE);
        }
    }

    private void resetProgressBars() {
        timeProgressBar.setVisibility(View.GONE);
        eventProgressBar.setVisibility(View.GONE);
        timeProgressBar.setProgress(100);
        eventProgressBar.setProgress(100);
        timeProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
    }

    private void startQuicktime() {
        count = maxCount;
        eventProgressBar.setVisibility(View.VISIBLE);
        timeProgressBar.setVisibility(View.VISIBLE);
        quicktime.setVisibility(View.VISIBLE);
        startEvent.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        new QuicktimeEvent().execute(0);
    }
}
