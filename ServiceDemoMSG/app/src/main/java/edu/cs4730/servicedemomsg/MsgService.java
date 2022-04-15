package edu.cs4730.servicedemomsg;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * A simple service that receives message via a handler from the binder.
 * based on http://developer.android.com/guide/components/bound-services.html
 */

public class MsgService extends Service {
    public MsgService() {
    }

    /**
     * Command to the service to display a message
     */
    static final int MSG_SAY_HELLO = 1;

    /**
     * Handler of incoming messages from clients.  normally it might do something here.  this just toasts.
     */
    Handler incomingHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.v("MsgService", "Message received");
            if (msg.what == MSG_SAY_HELLO)
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
            return true;
        }
    });

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(incomingHandler);

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }

}
