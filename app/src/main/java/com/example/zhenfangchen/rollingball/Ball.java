package com.example.zhenfangchen.rollingball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by zhenfangchen on 1/22/17.
 */

public class Ball {

    private double x, y;
    private double xSpeed, ySpeed;

    private double ballSizeSpeed;
    private double ballSize;

    private Bitmap bitmap;

    private int screenWidth, screenHeight;

    public Ball(Context context) {
        x = 50;
        y = 50;
        xSpeed = 0;
        ySpeed = 0;
        ballSize = 50;

        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ball), (int)ballSize, (int)ballSize, false);

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;

        if (x < 0) {
            x = screenWidth - 1;
        } else if (x > screenWidth) {
            x = 1;
        }

        if (y < 0) {
            y = screenHeight - 1;
        } else if (y > screenHeight) {
            y = 1;
        }

        if (xSpeed > 0) {
            xSpeed -= 0.1;
        } else if (xSpeed < 0) {
            xSpeed += 0.1;
        }

        if (ySpeed > 0) {
            ySpeed -= 0.1;
        } else if (ySpeed < 0) {
            ySpeed += 0.1;
        }

        ballSize += ballSizeSpeed;

        if (ballSize <= 0) {
            ballSize = 1;
        } else if (ballSize > 500) {
            ballSize = 500;
        }

        //Log.d("TEST", ballSize + " " + ballSizeSpeed);

        bitmap = Bitmap.createScaledBitmap(bitmap, (int)ballSize, (int)ballSize, false);
        if (ballSizeSpeed > 0) {
            ballSizeSpeed -= 2;
        } else if (ballSizeSpeed < 0) {
            ballSizeSpeed += 2;
        }

        if (ballSizeSpeed < 0.5 && ballSizeSpeed > -0.5) {
            ballSizeSpeed = 0;
        }

    }

    public void update(double accel) {
        if (accel < 0.0005) {
            return;
        }

        if (accel > 0) {
            ballSizeSpeed -= accel / 10.0;
        } else if (accel < 0) {
            ballSizeSpeed += accel / 10.0;
        }

        Log.d("TEST", (accel / 10.0) + "");
    }

    public void update(double pitch, double azimuth) {
        //pitch = up/down; azimuth = right/left

        double verticalAngle = Math.toDegrees(pitch);
        double horizAngle = Math.toDegrees(azimuth);

        if (horizAngle > 0) {
            xSpeed -= horizAngle / 10;
        } else if (horizAngle < 0) {
            xSpeed += horizAngle / 10;
        }

        if (verticalAngle > 0) {
            ySpeed += verticalAngle / 10;
        } else if (horizAngle < 0) {
            ySpeed -= verticalAngle / 10;;
        }

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getXSpeed() {
        return xSpeed;
    }

    public double getYSpeed() { return ySpeed; }

    public Bitmap getBitmap() {
        return bitmap;
    }


}
