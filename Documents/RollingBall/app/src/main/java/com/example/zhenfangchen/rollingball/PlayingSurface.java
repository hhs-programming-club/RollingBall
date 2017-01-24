package com.example.zhenfangchen.rollingball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by zhenfangchen on 1/22/17.
 */

public class PlayingSurface extends SurfaceView implements Runnable, SensorEventListener{

    private Ball ball;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private boolean playing = true;

    private Thread gameThread = null;

    private SensorManager sensorManager;
    private Sensor gyro, accel;


    public PlayingSurface(Context context, SensorManager sensorManager) {
        super(context);

        ball = new Ball(context);

        surfaceHolder = getHolder();
        paint = new Paint();

        this.sensorManager = sensorManager;

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

    }

    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        ball.update();
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(ball.getBitmap(), (int) ball.getX(), (int) ball.getY(), paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {

        sensorManager.unregisterListener(this);

        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void resume() {

        sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);

        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;
    private static final double EPSILON = 0.1f;

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                // Axis of the rotation sample, not normalized yet.
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];

                // Calculate the angular speed of the sample
                double omegaMagnitude = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                // Normalize the rotation vector if it's big enough to get the axis
                // (that is, EPSILON should represent your maximum allowable margin of error)
                if (omegaMagnitude > EPSILON) {
                    axisX /= omegaMagnitude;
                    axisY /= omegaMagnitude;
                    axisZ /= omegaMagnitude;
                }

                // Integrate around this axis with the angular speed by the timestep
                // in order to get a delta rotation from this sample over the timestep
                // We will convert this axis-angle representation of the delta rotation
                // into a quaternion before turning it into the rotation matrix.
                double thetaOverTwo = omegaMagnitude * dT / 2.0f;
                double sinThetaOverTwo = Math.sin(thetaOverTwo);
                double cosThetaOverTwo = Math.cos(thetaOverTwo);
                deltaRotationVector[0] = (float) sinThetaOverTwo * axisX;
                deltaRotationVector[1] = (float) sinThetaOverTwo * axisY;
                deltaRotationVector[2] = (float) sinThetaOverTwo * axisZ;
                deltaRotationVector[3] = (float) cosThetaOverTwo;
            }
            timestamp = event.timestamp;
            float[] deltaRotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
            // User code should concatenate the delta rotation we computed with the current rotation
            // in order to get the updated rotation.
            // rotationCurrent = rotationCurrent * deltaRotationMatrix;

            double pitch = Math.asin(-deltaRotationMatrix[7]); //up/down
            double azimuth = Math.acos(deltaRotationMatrix[8] / Math.cos(pitch)); //right/left

            ball.update(pitch, azimuth);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
