package edu.cs4730.servicedemo;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * A simple example of IntentService that downloads a file from the Internet to 
 * the pictures directory.
 */

public class fileDlService extends IntentService {

    public fileDlService() {
        super("FileDownLoad");
    }
    String TAG = "fileDLService";
    @Override
    protected void onHandleIntent(Intent intent) {
        URL url;
        File file;
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            url = (URL) extras.get("URL");

            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    (String) extras.getString("FILE"));
        } else {
            shownoti(-1, "Error, No file to download", null);
            return;  //nothing to do.
        }
        Log.d(TAG, "File:" + file.getAbsolutePath());
        try {
            long startTime = System.currentTimeMillis();

            Log.d(TAG, "download url:" + url);
            Log.d(TAG, "downloaded file name:" + file.getAbsolutePath());
            //Open a connection to that URL.
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //open output file
            FileOutputStream fos = new FileOutputStream(file);
            try {
                Log.d(TAG, "download beginning");
                //read in from the connection (which is not a string, so getting bytes) and then
                //write those bytes out to the file.  There is likely a zero write at the end, but
                //doesn't harm anything.
                InputStream is = urlConnection.getInputStream();
                byte[] buffer = new byte[4096];
                int n = -1;
                while ((n = is.read(buffer)) != -1) {
                    Log.wtf(TAG, "Wrote " + n + " bytes");
                    fos.write(buffer, 0, n);
                }
                //note there is a catch at the end to print the error message if there is one.
            } finally {
                urlConnection.disconnect();
                fos.close();
            }

            //new tell the system that the file exists , so it will show up in gallery/photos/whatever
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, file.toString());
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            //setup a notification so the user knows it has finished.
            shownoti(0, "download ready in "
                    + ((System.currentTimeMillis() - startTime) / 1000)
                    + " sec", file);

        } catch (IOException e) {
            Log.wtf(TAG, "something died");
            e.printStackTrace();
            shownoti(-2, "Error: " + e, null);
        }
        stopSelf();  //end the service.
    }


    public void shownoti(int val, String message, File file) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification noti;

        if (val >= 0) {
            Intent notificationIntent = new Intent();
            notificationIntent.setAction(Intent.ACTION_VIEW);
            notificationIntent.setDataAndType(
                    Uri.parse("file://" + file.getAbsolutePath()), "image/*"
            );
            PendingIntent contentIntent = PendingIntent.getActivity(this, 100, notificationIntent, 0);

            noti = new NotificationCompat.Builder(getApplicationContext(), MainActivity.id)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setWhen(System.currentTimeMillis())  //When the event occurred, now, since noti are stored by time.
                    .setContentIntent(contentIntent)  //what activity to open.
                    .setContentTitle("FileDownload")   //Title message top row.
                    .setContentText(message)  //message when looking at the notification, second row
                    .setAutoCancel(true)   //allow auto cancel when pressed.
                    .build();  //finally build and return a Notification.

        } else {
            noti = new NotificationCompat.Builder(getApplicationContext(), MainActivity.id)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setWhen(System.currentTimeMillis())  //When the event occurred, now, since noti are stored by time.
                    .setContentTitle("File Download")   //Title message top row.
                    .setContentText(message)  //message when looking at the notification, second row
                    .setAutoCancel(true)   //allow auto cancel when pressed.
                    .build();  //finally build and return a Notification.
        }
        //finally Show the notification
        nm.notify(100, noti);
    }

}
