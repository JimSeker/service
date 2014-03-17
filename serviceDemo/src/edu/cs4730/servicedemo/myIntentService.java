package edu.cs4730.servicedemo;

import java.util.Random;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/*
 * This is a simple IntentSerice, which displays random numbers 
 * via a toast or notification
 */

public class myIntentService extends IntentService {

	/** 
	 * A constructor is required, and must call the super IntentService(String)
	 * constructor with a name for the worker thread.
	 */
	Random r;
	int NotID =1;
	NotificationManager nm;

	public myIntentService() {
		super("number5");  //or whatever name you want to give it.
		r = new Random();
		//showToast("b Intent Service started");
	}
	public myIntentService(String name) {
		super(name);  //or whatever name you want to give it.
		r = new Random();
		//showToast("Intent Service started");
	}

	/*
	 * 
	 * (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		//This is where we do the actually work.  We could download a file 
		//or whatever else is required.  This is already on a separate thread.
		//When we exit this method the service is done.  It can be called again.
		Log.d("intentServer", "handleIntent called");
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		//setup how many messages
		int times =0, i;
		Messenger messenger = null;  
		Bundle extras = intent.getExtras();
		if (extras != null) {
			times = extras.getInt("times",0);
			messenger = (Messenger) extras.get("MESSENGER");
		}
		for (i=0; i<times; i++ ) {
			synchronized (this) {
				try {
					wait(5000);
				} catch (InterruptedException e) {
				}
			}
			String info= i+ "random "+r.nextInt(100);
			Log.d("intentServer", info);
			if (messenger != null) {
				Message msg = Message.obtain();
				msg.obj = info;
				try {
					messenger.send(msg);
				} catch (android.os.RemoteException e1) {
					Log.w(getClass().getName(), "Exception sending message", e1);
				}
			} else {
				//no handler, so use notification
				makenoti(info);
			}
		}
	}
	public void makenoti(String message) {

		Notification noti = new NotificationCompat.Builder(getApplicationContext())
		.setSmallIcon(R.drawable.ic_launcher)
		.setWhen(System.currentTimeMillis())  //When the event occurred, now, since noti are stored by time.
		
		.setContentTitle("Service")   //Title message top row.
		.setContentText(message)  //message when looking at the notification, second row
		.setAutoCancel(true)   //allow auto cancel when pressed.
		.build();  //finally build and return a Notification.

		//Show the notification
		nm.notify(NotID, noti);
		NotID++;
	}

}
