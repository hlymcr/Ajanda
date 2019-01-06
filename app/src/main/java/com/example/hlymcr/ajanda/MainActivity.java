package com.example.hlymcr.ajanda;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", new Locale("tr"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);

        actionBar.setTitle(null);



        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        compactCalendar.setUseThreeLetterAbbreviation(true);



        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override

            public void onDayClick(final Date df) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Etkinlik");
                builder.setMessage("Etkinlik eklemek istediğine emin misin?");
                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {



                    }
                });


                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.setClassName("com.example.hlymcr.ajanda","com.example.hlymcr.ajanda.Event");
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("tarih", String.valueOf(df));
                        Log.d("tarih11", String.valueOf(df));
                        editor.commit();
                        startActivity(intent);


                    }
                });


                builder.show();
            }

            @Override

            public void onMonthScroll(Date firstDayOfNewMonth) {

                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));

            }

        });

    }

}
