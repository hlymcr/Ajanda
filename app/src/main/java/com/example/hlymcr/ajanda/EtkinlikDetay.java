package com.example.hlymcr.ajanda;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EtkinlikDetay extends AppCompatActivity {
    Button sil;
    TextView adi,tarih,saat,detay;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlik_detay);

        sil=(Button)findViewById(R.id.sil);
        adi = (TextView)findViewById(R.id.adi);
        tarih = (TextView)findViewById(R.id.tarih);
        saat = (TextView)findViewById(R.id.saat);
        detay=(TextView)findViewById(R.id.detay);

        Intent intent=getIntent();
        id = intent.getIntExtra("id", 0);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EtkinlikDetay.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("idi",id);
        editor.commit();

        Database db = new Database(getApplicationContext());
        HashMap<String, String> map = db.EtkinlikDetay(id);

        String Etarih = map.get("tarih").toString();
        String Esaat=map.get("saat").toString();


        adi.setText(map.get("etkinlik_adi"));
        detay.setText(map.get("detay"));
        tarih.setText(Etarih);
        Log.d("Etarih",Etarih);
        Log.d("Esaat",Esaat);
        saat.setText(Esaat);
        sil.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EtkinlikDetay.this);
                alertDialog.setTitle("Uyarı");
                alertDialog.setMessage("Etkinlik Silinsin mi?");
                alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Database db = new Database(getApplicationContext());
                        db.EtkinlikSil(id);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EtkinlikDetay.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("id",id);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Etkinlik Başarıyla Silindi", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

                alertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                    }
                });
                alertDialog.show();


            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
