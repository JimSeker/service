package edu.cs4730.servicedemoipc;

import edu.cs4730.servicedemoipc.RandNumService.LocalBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * This is a very simple example of a service and Ibinder (IPC)
 * when the user clicks the button, it retrieves a random number from the service.
 * 
 * http://developer.android.com/guide/components/bound-services.html
 * http://developer.android.com/reference/android/app/Service.html
 */
public class MainFragment extends Fragment {

    RandNumService mService;
    boolean mBound = false;

	
	public MainFragment() {
		// Required empty public constructor
	}

    @Override
	public void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(getActivity(), RandNumService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
	public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
        	getActivity().unbindService(mConnection);
            mBound = false;
        }
    }

	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View myView = inflater.inflate(R.layout.fragment_main, container, false);

		//setup the button, so it will call into the service and get a random number.
		myView.findViewById(R.id.btn_gn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
			      if (mBound) {
			            // Call a method from the RandNumService.
			            // However, if this call were something that might hang, then this request should
			            // occur in a separate thread to avoid slowing down the activity performance.
			    	     //but getting a random number is really quick, so I'm doing it here instead.
			            int num = mService.getRandomNumber();
			            Toast.makeText(getActivity(), "number: " + num, Toast.LENGTH_SHORT).show();
			        }

			}
		});
		return myView;
	}

	
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mBound = false;
        }
    };

	
}
