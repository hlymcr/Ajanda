package com.example.hlymcr.ajanda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.domain.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Etkinlikler extends AppCompatActivity{

    ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> etkinlik_liste;
    String etkinlik_adlari[];
    int etkinlik_idler[];
    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlikler);



        SharedPreferences prefss = PreferenceManager.getDefaultSharedPreferences(this);

        String etkinlik=prefss.getString("etkinlik","no");
        String tarih =prefss.getString("tarih","noo");
        String saat=prefss.getString("saat","saat");

        Log.d("etkinlikAdi",etkinlik);
        Log.d("tarih",tarih);
        Log.d("saat",saat);


    }

    public void onResume(){

        super.onResume();
        Database db = new Database(getApplicationContext());
        etkinlik_liste=db.etkinlikler();
        if(etkinlik_liste.size()==0){
            Toast.makeText(getApplicationContext(), "Henüz Etkinlik Eklenmemiş.\nAşağıdaki + Butonundan Ekleyiniz", Toast.LENGTH_LONG).show();
        }
        else{
            etkinlik_adlari = new String[etkinlik_liste.size()];
            etkinlik_idler = new int[etkinlik_liste.size()];
            for(i=0;i<etkinlik_liste.size();i++){
                etkinlik_adlari[i] = etkinlik_liste.get(i).get("etkinlik_adi");
                etkinlik_idler[i] = Integer.parseInt(etkinlik_liste.get(i).get("id"));
            }
            lv = (ListView) findViewById(R.id.listview);
            adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.etkinlik_adi, etkinlik_adlari);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    //Listedeki her hangibir yere tıklandıgında tıklanan satırın sırasını alıyoruz.
                    //Bu sıra id arraydeki sırayla aynı oldugundan tıklanan satırda bulunan kitapın id sini alıyor ve kitap detaya gönderiyoruz.
                    Intent intent = new Intent(getApplicationContext(), EtkinlikDetay.class);
                    intent.putExtra("id", (int)etkinlik_idler[arg2]);
                    startActivity(intent);

                }
            });

        }
    }
    public void Ekle(View view){
        Intent intent = new Intent(Etkinlikler.this,MainActivity.class);
        startActivity(intent);
    }
}