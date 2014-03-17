package edu.cs4730.servicedemo;

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.Process;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class myService1 extends Service {
	  private Looper mServiceLooper;
	  private ServiceHandler mServiceHandler;

	  //my variables
		Random r;
		int NotID =1;
		NotificationManager nm;
	  // Handler that receives messages from the thread
	  private final class ServiceHandler extends Handler {
	      public ServiceHandler(Looper looper) {
	          super(looper);
	      }
	      @Override
	      public void handleMessage(Message msg) {
	          // Normally we would do some work here, like download a file.
	          // For our sample, we just sleep for 5 seconds.
	  		//setup how many messages
	        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	  		int times =0, i;
	  		Messenger messenger = null;  
	    	Bundle extras = msg.getData();
	    	  
	    	  
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
					Message mymsg = Message.obtain();
					mymsg.obj = info;
					try {
						messenger.send(mymsg);
					} catch (android.os.RemoteException e1) {
						Log.w(getClass().getName(), "Exception sending message", e1);
					}
				} else {
					//no handler, so use notification
					makenoti(info);
				}
			}
	          // Stop the service using the startId, so that we don't stop
	          // the service in the middle of handling another job
	          stopSelf(msg.arg1);
	      }
	  }

	  @Override
	  public void onCreate() {
			r = new Random();
	    // Start up the thread running the service.  Note that we create a
	    // separate thread because the service normally runs in the process's
	    // main thread, which we don't want to block.  We also make it
	    // background priority so CPU-intensive work will not disrupt our UI.
	    HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
	    thread.start();
	    
	    // Get the HandlerThread's Looper and use it for our Handler 
	    mServiceLooper = thread.getLooper();
	    mServiceHandler = new ServiceHandler(mServiceLooper);
	  }

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

	      // For each start request, send a message to start a job and deliver the
	      // start ID so we know which request we're stopping when we finish the job
	      Message msg = mServiceHandler.obtainMessage();
	      msg.arg1 = startId;//needed for stop.
	      msg.setData(intent.getExtras());
	      mServiceHandler.sendMessage(msg);
	      
	      // If we get killed, after returning from here, restart
	      return START_STICKY;
	  }

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}
	  @Override
	  public void onDestroy() {
	    Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show(); 
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
