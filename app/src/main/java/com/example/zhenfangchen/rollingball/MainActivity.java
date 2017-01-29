package com.example.zhenfangchen.rollingball;

import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private PlayingSurface surface;
    private SensorManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        surface = new PlayingSurface(this, manager);

        setContentView(surface);

    }

    protected void onPause() {
        super.onPause();
        surface.pause();
    }

    protected void onResume() {
        super.onResume();
        surface.resume();
    }
}
