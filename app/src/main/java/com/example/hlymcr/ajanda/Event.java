package com.example.hlymcr.ajanda;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Event extends Activity {
    private Button startAlarmBtn;
    private TimePickerDialog timePickerDialog;
    final static int REQUEST_CODE = 1;
    public static String[] separated,saat;
    EditText etkinlik,detay;
    public static String Etkinlk,Edetay;
    ArrayList<HashMap<String, String>> etkinlik_liste;
    int etkinlik_idler[];
    int idler;
    int id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Init();

        startAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPickerDialog(false);
                if(Etkinlk==""){
                    Toast.makeText(Event.this,"Etkinliğin Adını Giriniz",Toast.LENGTH_LONG).show();
                }
                Etkinlk =etkinlik.getText().toString();
                Edetay=detay.getText().toString();

            }
        });

        etkinlik =(EditText)findViewById(R.id.etkinlik);
        detay=(EditText)findViewById(R.id.etkinlikD);

        TextView tarihEkran=(TextView)findViewById(R.id.tarih);

        SharedPreferences prefss = PreferenceManager.getDefaultSharedPreferences(this);
        String tarih =prefss.getString("tarih","no id");
        separated =tarih.split(" ");
        Log.d("tarihhh",tarih);
        Log.d("Seperated", String.valueOf(separated[5]));
        if(separated[1].equals("Jan")){
            separated[1]="Ocak";
        }
        else if(separated[1].equals("Feb")){
            separated[1]="Şubat";
        }
        else if(separated[1].equals("Mar")){
            separated[1]="Mart";
        }
        else if(separated[1].equals("Apr")){
            separated[1]="Nisan";
        }
        else if(separated[1].equals("May")){
            separated[1]="Mayıs";
        }
        else if(separated[1].equals("Jun")){
            separated[1]="Haziran";
        }
        else if(separated[1].equals("Jul")){
            separated[1]="Temmuz";
        }
        else if(separated[1].equals("Aug")){
            separated[1]="Ağustos";
        }
        else if(separated[1].equals("Sep")){
            separated[1]="Eylül";
        }
        else if(separated[1].equals("Oct")){
            separated[1]="Ekim";
        }
        else if(separated[1].equals("Nov")){
            separated[1]="Kasım";
        }
        else if(separated[1].equals("Dec")){
            separated[1]="Aralık";
        }
        if(separated[0].equals("Mon")){
            separated[0]="Pazartesi";
        }
        else if(separated[0].equals("Tue")){
            separated[0]="Salı";
        }
        else if(separated[0].equals("Wed")){
            separated[0]="Çarşamba";
        }
        else if(separated[0].equals("Thu")){
            separated[0]="Perşembe";
        }
        else if(separated[0].equals("Fri")){
            separated[0]="Cuma";
        }
        else if(separated[0].equals("Sat")){
            separated[0]="Cumartesi";
        }
        else if(separated[0].equals("Sun")){
            separated[0]="Pazar";
        }
        tarihEkran.setText(separated[2]+" "+separated[1]+" "+separated[0]+" "+separated[5]);



    }
    private void setAlarm(Calendar alarmCalender){

        Database db = new Database(getApplicationContext());
        etkinlik_liste=db.etkinlikler();

        Toast.makeText(getApplicationContext(),"Alarm Ayarlandı!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        etkinlik_idler = new int[etkinlik_liste.size()];
        for(int i=0;i<etkinlik_liste.size();i++){

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            id =prefs.getInt("id",0);//database den alınan id
            Log.d("id", String.valueOf(id));
            etkinlik_idler[i] = Integer.parseInt(etkinlik_liste.get(i).get("id"));//eklenen idler
            Log.d("etkinlik_size", String.valueOf(etkinlik_liste.size()));
            Log.d("etkin_id", String.valueOf(etkinlik_idler[i]));
            idler =etkinlik_idler[i];//çalacak idler
            Log.d("iddd", String.valueOf(idler));



        }

        PendingIntent appIntent = PendingIntent.getBroadcast(this, idler, intent,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalender.getTimeInMillis(), appIntent);
        db.EtkinlikEkle(Etkinlk,separated[2]+" "+separated[1]+" "+separated[5],saat[3],Edetay);
        db.close();
        Toast.makeText(getApplicationContext(), "Etkinliğiniz eklendi.", Toast.LENGTH_SHORT).show();
        etkinlik.setText("");


    }
    private void openPickerDialog(boolean is24hour) {

        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(
                Event.this,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24hour);
        timePickerDialog.setTitle("Alarm Ayarla");

        timePickerDialog.show();
    }
    TimePickerDialog.OnTimeSetListener onTimeSetListener
            = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Date call=calNow.getTime();
            Calendar calSet = (Calendar) calNow.clone();
            Log.d("tarih1", String.valueOf(call));
            calSet.set(Calendar.DATE,Integer.valueOf(separated[2]));
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            Date call2=calSet.getTime();
            Log.d("tarih2", String.valueOf(call2));

            saat=String.valueOf(call2).split(" ");


            if(call2.compareTo(call) <= 0){

                calSet.add(Calendar.DATE, 1);
            }

            setAlarm(calSet);
        }};

    private void Init() {

        startAlarmBtn = (Button)findViewById(R.id.kaydet);

    }
}
