package edu.cs4730.servicedemoipc;

import java.security.SecureRandom;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * A simple service.   It setups the binder (called LocalBinder), so the activity/fragment
 * can call into the service to get a random number.
 * there are no toasts in the service, so POST_NOTIFICATIONS is not needed.
 */

public class RandNumService extends Service {
	
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final SecureRandom mGenerator = new SecureRandom();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        RandNumService getService() {
            // Return this instance of LocalService so clients can call public methods
            return RandNumService.this;
        }
    }

	public RandNumService() {
		//blank constructor.
	}

	//return a connection to the service, when the activity/fragment binds to the service.
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

    /** method for clients */
    public int getRandomNumber() {
      return mGenerator.nextInt(100);
    }

}
