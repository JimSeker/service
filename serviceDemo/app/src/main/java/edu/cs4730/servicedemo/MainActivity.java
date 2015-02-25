package edu.cs4730.servicedemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


/*
 * Nothing to see here.  Go to the MainFragment for the code to call/start a service.
 */


public class MainActivity extends ActionBarActivity {


	
	
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
