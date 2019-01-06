package com.example.hlymcr.ajanda;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AnaSayfa extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private float  last_x, last_y, last_z = 0;

    ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> etkinlik_liste;
    String etkinlik_adlari[];
    String etkinlik_tarih[];
    int etkinlik_idler[];
    public static String[] separated;
    int i;

    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        Calendar calNow = Calendar.getInstance();
        Date call=calNow.getTime();

        Log.d("tarih1", String.valueOf(call));

        separated =String.valueOf(call).split(" ");
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

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }
    public void Takvim(View view){
        startActivity(new Intent(AnaSayfa.this,MainActivity.class));
    }
    public void Etkinlikler(View view){
        startActivity(new Intent(AnaSayfa.this,Etkinlikler.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(AnaSayfa.this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;

            float x = values[0];
            float y = values[1];
            float z = values[2];

            if (z * last_z<0  && x*last_x<0 && y*last_y <0 ) {
                vibrator.vibrate(400);
                Database db = new Database(getApplicationContext());
                etkinlik_liste=db.etkinlikler();
                etkinlik_adlari = new String[etkinlik_liste.size()];
                etkinlik_tarih=new String [etkinlik_liste.size()];
                etkinlik_idler = new int[etkinlik_liste.size()];
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_custom,
                        (ViewGroup) findViewById(R.id.custom_toast_layout));

                TextView text = (TextView) layout.findViewById(R.id.textToShow);
                for(i=0;i<etkinlik_liste.size();i++){
                    etkinlik_adlari[i] = etkinlik_liste.get(i).get("etkinlik_adi");
                    etkinlik_idler[i] = Integer.parseInt(etkinlik_liste.get(i).get("id"));
                    etkinlik_tarih[i]=etkinlik_liste.get(i).get("tarih");

                    Log.d("trh",etkinlik_tarih[0]);
                    String bgnTarih = separated[2]+" "+separated[1]+" "+separated[5];
                    Log.d("bgnTarih",bgnTarih);

                    if(etkinlik_tarih[i].equalsIgnoreCase(bgnTarih)){
                        text.setText("Bugünkü etkinliğiniz:\n "+etkinlik_adlari[i]);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);

                        toast.setView(layout);
                        toast.show();
                    }
                }
            }

            Log.d("MainActivity", String.format("x : %f y : %f z : %f", x, y, z));

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }
}
