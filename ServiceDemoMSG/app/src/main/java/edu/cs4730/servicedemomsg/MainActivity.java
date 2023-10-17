package edu.cs4730.servicedemomsg;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.cs4730.servicedemomsg.databinding.ActivityMainBinding;

/**
 * This is a very simple example of a service and Ibinder (IPC)
 * when the user clicks the button, it triggers the service to say hi via a toast.
 * <p>
 * http://developer.android.com/guide/components/bound-services.html
 * http://developer.android.com/reference/android/app/Service.html
 */
public class MainActivity extends AppCompatActivity {

    Messenger mService;
    boolean mBound = false;
    final String TAG = "MainActivity";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setup the button, so it will call into the service via cause the service to react and say hi.
        binding.btnGn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBound) {
                    // Create and send a message to the service, using a supported 'what' value
                    Message msg = Message.obtain(null, MsgService.MSG_SAY_HELLO, 0, 0);
                    try {
                        mService.send(msg);
                        Log.v(TAG, "Message sent.");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.v(TAG, "Bound is false");
                }
            }
        });
    }

    /**
     * Class for interacting with the main interface of the service.
     */

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.

            mService = new Messenger(service);
            mBound = true;
            Log.v(TAG, "connected is true");
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mBound = false;
            Log.v(TAG, "Disconnected!");
        }

    };

    @Override
    public void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, MsgService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

}
