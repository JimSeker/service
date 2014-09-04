package edu.cs4730.servicedemoipc;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


/*
 * Move along, nothing to see here.
 * Go the MainFragment to see the code for service and IPC.
 */

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new MainFragment()).commit();
		}
	}


}
