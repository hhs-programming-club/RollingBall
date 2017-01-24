package com.example.zhenfangchen.rollingball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by zhenfangchen on 1/22/17.
 */

public class Ball {

    private double x, y;
    private double xSpeed, ySpeed;

    private Bitmap bitmap;

    public Ball(Context context) {
        x = 50;
        y = 50;
        xSpeed = 0;
        ySpeed = 0;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;

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
