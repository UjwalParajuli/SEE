package com.ujwal.see;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (ActivityCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;

        }
        else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_TIME_OUT);
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    },SPLASH_TIME_OUT);

                } else {
                    System.exit(1);
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
