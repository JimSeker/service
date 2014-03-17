package edu.cs4730.servicedemo;

import java.net.MalformedURLException;
import java.net.URL;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	/*
	 * The service will call the handler to send back information.
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message message) {
			Object path = message.obj;
			Toast.makeText(MainActivity.this, path.toString(), Toast.LENGTH_LONG).show();
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//IntentSerive start with 5 random number toasts
		findViewById(R.id.btn_istarth).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent number5 = new Intent(getBaseContext(), myIntentService.class);
				number5.putExtra("times", 5);
				Messenger messenger = new Messenger(handler);
				number5.putExtra("MESSENGER", messenger);
				startService(number5);
			}
		});

		//IntentSerive stop.  If already stopped, what happens?
		findViewById(R.id.btn_istartn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent number5 = new Intent(getBaseContext(), myIntentService.class);
				number5.putExtra("times", 5);
				startService(number5);
			}
		});
		//IntentSerive start with 5 random number toasts
		findViewById(R.id.btn_sstarth).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent number5 = new Intent(getBaseContext(), myService1.class);
				number5.putExtra("times", 5);
				Messenger messenger = new Messenger(handler);
				number5.putExtra("MESSENGER", messenger);
				startService(number5);
			}
		});

		//
		findViewById(R.id.btn_sstartn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent number5 = new Intent(getBaseContext(), myService1.class);
				number5.putExtra("times", 5);
				startService(number5);
			}
		});
		//
		findViewById(R.id.btn_file).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				URL url = null;
				String file = "";
				try {
					url = new URL ("http://www.cs.uwyo.edu/~seker/courses/4730/example/pic.jpg");
					//for a bigger file: http://www.nasa.gov/images/content/206402main_jsc2007e113280_hires.jpg
					
					file =  "pic.jpg";
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//setup intent and send it.
				Intent getfile = new Intent(getBaseContext(), fileDlService.class);
				getfile.putExtra("FILE", file);
				getfile.putExtra("URL", url);
				startService(getfile);
			}
		});
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
	
}
