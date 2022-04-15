package edu.cs4730.freefallsrv;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

/**
 * A "simple" service to capture sensor events and determine if the device is in free fall.
 * It should play a sound, but at the moment, it is just a toast.
 * <p>
 * This will likely be a pretty good drain on a battery as well.
 */
public class MySensorService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    MediaPlayer mediaPlayer;

    public MySensorService() {
    }

    @Override
    public void onCreate() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        logthis("the service is alive!!!");
    }

    //ensure that we disconnect from the sensor once it's done.
    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this, mSensor);
        KillMediaPlayer();
        logthis("the service in this place is dead man!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //SQRT(x*x + y*y + z*z).
        double vector = Math.sqrt(event.values[0] * event.values[0] + event.values[1] * event.values[1] + event.values[2] * event.values[2]);
        //9.8 m/s is basically not moving
        //3.0 m/s or less is basically falling.
        //20 m/s is landing ish, based on what I read.

        //logthis("Vector is " + vector);

        if (vector <= 4.0) { // 3 m/s should be falling, I think...
            //Toast.makeText(getBaseContext(),"I'm falling " + vector, Toast.LENGTH_SHORT).show();
            playsnd();
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //don't care about this.
    }

    public void playsnd() {
        if (mediaPlayer == null) { //first time
            mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.hmscream);
        } else if (mediaPlayer.isPlaying()) { //duh don't start it again.
            //Toast.makeText(getBaseContext(), "I'm playing already", Toast.LENGTH_SHORT).show();
            return;
        } else { //play it at least one, reset and play again.
            mediaPlayer.seekTo(0);
        }
        mediaPlayer.start();
    }


    void KillMediaPlayer() {
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

    public void logthis(String text) {
        Log.v("freefall", text);
    }
}
