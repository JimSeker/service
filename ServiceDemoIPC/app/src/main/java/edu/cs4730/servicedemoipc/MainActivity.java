package edu.cs4730.servicedemoipc;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Move along, nothing to see here.
 * Go the MainFragment to see the code for service and IPC.
 */

public class MainActivity extends AppCompatActivity {

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
