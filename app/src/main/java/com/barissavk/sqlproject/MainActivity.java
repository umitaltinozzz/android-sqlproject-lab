package com.barissavk.sqlproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    Button Kaydet;
    Button Goster;
    Button Sil;
    Button Guncelle;
    EditText ad;
    EditText soyad;
    EditText yas;
    EditText sehir;
    TextView Bilgiler;
    private veritabani v1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v1 = new veritabani(this);
        Kaydet = (Button) findViewById(R.id.buttonEkle);
        Goster = (Button) findViewById(R.id.buttonGoster);
        Sil = (Button) findViewById(R.id.buttonSil);
        Guncelle = (Button) findViewById(R.id.buttonGuncelle);
        ad = (EditText) findViewById(R.id.editTextAd);
        soyad = (EditText) findViewById(R.id.editTextSoyad);
        yas = (EditText) findViewById(R.id.editTextYas);
        sehir = (EditText) findViewById(R.id.editTextSehir);
        Bilgiler = (TextView) findViewById(R.id.textViewBilgiler);
        Kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    KayitEkle(ad.getText().toString(), soyad.getText().toString(), yas.getText().toString(), sehir.getText().toString());
                }
                finally {
                    v1.close();
                }
            }
        });
        Goster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor crs = KayitGetir();
                KayitGoster(crs);
            }
        });
        Sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Silme işlemi
                KayitSil(ad.getText().toString());

                // Yeniden gösterme işlemi
                Cursor crs = KayitGetir();
                KayitGoster(crs);
            }
        });

        Guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KayitGuncelle(ad.getText().toString(), soyad.getText().toString(), yas.getText().toString(), sehir.getText().toString());
            }
        });
    }
    private String[] sutunlar = {"ad","soyad","yas","sehir"};
    private Cursor KayitGetir()
    {
        SQLiteDatabase db = v1.getWritableDatabase();
        Cursor okunanlar = db.query("OgrenciBilgi",sutunlar,null,null,null,null,null);
        return okunanlar;
    }
    private void KayitGoster(Cursor goster) {
        StringBuilder builder = new StringBuilder();
        int adIndex = goster.getColumnIndex("ad");
        int soyadIndex = goster.getColumnIndex("soyad");
        int yasIndex = goster.getColumnIndex("yas");
        int sehirIndex = goster.getColumnIndex("sehir");

        while (goster.moveToNext()) {
            if (adIndex != -1 && soyadIndex != -1 && yasIndex != -1 && sehirIndex != -1) {
                String add = goster.getString(adIndex);
                String soyadd = goster.getString(soyadIndex);
                String yass = goster.getString(yasIndex);
                String sehirr = goster.getString(sehirIndex);
                builder.append("Ad: ").append(add).append("\n");
                builder.append("Soyad: ").append(soyadd).append("\n");
                builder.append("Yas: ").append(yass).append("\n");
                builder.append("Sehir: ").append(sehirr).append("\n");
                builder.append("----------------").append("\n");
            } else {
                // Sütunlar mevcut değilse, bir hata mesajı göster
                builder.append("Sütunlar mevcut değil").append("\n");
            }
        }
        TextView text = (TextView) findViewById(R.id.textViewBilgiler);
        text.setText(builder.toString());
    }

    private void KayitEkle(String adi, String soyadi, String yasi, String sehri)
    {
        SQLiteDatabase db = v1.getWritableDatabase();
        ContentValues veriler = new ContentValues();
        veriler.put("ad",adi);
        veriler.put("soyad",soyadi);
        veriler.put("yas",yasi);
        veriler.put("sehir",sehri);
        db.insertOrThrow("OgrenciBilgi",null,veriler);
    }
    private void KayitSil(String adi)
    {
        SQLiteDatabase db = v1.getReadableDatabase();
        db.delete("OgrenciBilgi","ad" + "=?",new String[] {adi});
    }
    private void KayitGuncelle(String addd, String soyaddd, String yasss, String sehirrr)
    {
        SQLiteDatabase db = v1.getWritableDatabase();
        ContentValues cvGuncelle = new ContentValues();
        cvGuncelle.put("ad",addd);
        cvGuncelle.put("soyad",soyaddd);
        cvGuncelle.put("yas",yasss);
        cvGuncelle.put("sehir",sehirrr);
        db.update("OgrenciBilgi", cvGuncelle, "ad" + "=?", new String[] {addd});
        db.close();
    }
}