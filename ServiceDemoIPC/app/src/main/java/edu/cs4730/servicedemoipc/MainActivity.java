package edu.cs4730.servicedemoipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.cs4730.servicedemoipc.databinding.ActivityMainBinding;

/**
 * This is a very simple example of a service and Ibinder (IPC)
 * when the user clicks the button, it retrieves a random number from the service.
 * <p>
 * http://developer.android.com/guide/components/bound-services.html
 * http://developer.android.com/reference/android/app/Service.html
 */

public class MainActivity extends AppCompatActivity {

	RandNumService mService;
	boolean mBound = false;
	ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		//setup the button, so it will call into the service and get a random number.
		binding.btnGn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mBound) {
					// Call a method from the RandNumService.
					// However, if this call were something that might hang, then this request should
					// occur in a separate thread to avoid slowing down the activity performance.
					//but getting a random number is really quick, so I'm doing it here instead.
					int num = mService.getRandomNumber();
					Toast.makeText(getApplicationContext(), "number: " + num, Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	/**
	 * Defines callbacks for service binding, passed to bindService()
	 */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			RandNumService.LocalBinder binder = (RandNumService.LocalBinder) service;
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

	@Override
	public void onStart() {
		super.onStart();
		// Bind to LocalService
		Intent intent = new Intent(this, RandNumService.class);
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
