package com.example.hlymcr.ajanda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HULYA on 11.05.2017.
 */

public class Database extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "sqllite_database";//database adı

    private static final String TABLE_NAME = "etkinlik_listesi";
    private static String ETKINLIK_ADİ = "etkinlik_adi";
    private static String ETKINLİK_ID = "id";
    private static String ETKİNLİK_TARİH = "tarih";
    private static String ETKINLIK_SAAT = "saat";
    private static String ETKINLIK_DETAY ="detay";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ETKINLİK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ETKINLIK_ADİ + " TEXT,"
                + ETKİNLİK_TARİH + " TEXT,"
                + ETKINLIK_SAAT + " TEXT,"
                + ETKINLIK_DETAY + " TEXT"
                 + ")";
        db.execSQL(CREATE_TABLE);
    }




    public void EtkinlikSil(int id){ //id si belli olan row u silmek için

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ETKINLİK_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void EtkinlikEkle(String etkinlik_adi, String etkinlik_tarih,String etkinlik_saat,String etkinlik_detay) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ETKINLIK_ADİ, etkinlik_adi);
        values.put(ETKİNLİK_TARİH, etkinlik_tarih);
        values.put(ETKINLIK_SAAT, etkinlik_saat);
        values.put(ETKINLIK_DETAY,etkinlik_detay);
        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }


    public HashMap<String, String> EtkinlikDetay(int id){
        //Databeseden id si belli olan row u çekmek için.
        //Bu methodda sadece tek row değerleri alınır.

        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //mesala map.put("x","300"); mesala burda anahtar x değeri 300.

        HashMap<String,String> etkinlik = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            etkinlik.put(ETKINLIK_ADİ, cursor.getString(1));
            etkinlik.put(ETKİNLİK_TARİH, cursor.getString(2));
            etkinlik.put(ETKINLIK_SAAT, cursor.getString(3));
            etkinlik.put(ETKINLIK_DETAY, cursor.getString(4));
        }
        cursor.close();
        db.close();

        return etkinlik;
    }

    public ArrayList<HashMap<String, String>> etkinlikler(){

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> etkinliklist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                etkinliklist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return kitap liste
        return etkinliklist;
    }

    public void EtkinlikDuzenle(String etkinlik_adi, String etkinlik_tarih,String etkinlik_saat,int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ETKINLIK_ADİ, etkinlik_adi);
        values.put(ETKİNLİK_TARİH, etkinlik_tarih);
        values.put(ETKINLIK_SAAT, etkinlik_saat);

        // updating row
        db.update(TABLE_NAME, values, ETKINLİK_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public int getRowCount() {
        // Bu method bu uygulamada kullanılmıyor ama her zaman lazım olabilir.Tablodaki row sayısını geri döner.
        //Login uygulamasında kullanacağız
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }


    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}
