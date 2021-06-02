package com.igustinyomansatriawibawa.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    SharedPreferences shared, getShared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        shared = getSharedPreferences("getId", Context.MODE_PRIVATE);
        int id = shared.getInt("id",-1);
        getShared = getSharedPreferences("getIdReservasi",Context.MODE_PRIVATE);
        int id_reservasi = getShared.getInt("id_reservasi",-1);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(id ==-1 && id_reservasi ==-1 ){
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
//                    Toast.makeText(SplashScreen.this, String.valueOf(id), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashScreen.this, AfterQrActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
}