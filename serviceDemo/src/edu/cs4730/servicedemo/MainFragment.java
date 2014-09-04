package edu.cs4730.servicedemo;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * A simple example of how to call/start services.
 * 
 */
public class MainFragment extends Fragment {

	
	/*
	 * The service will call the handler to send back information.
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message message) {
			Object path = message.obj;
			Toast.makeText(getActivity().getBaseContext(), path.toString(), Toast.LENGTH_LONG).show();
		};
	};
	
	public MainFragment() {
		// Required empty public constructor
	}

	public MainFragment(Handler h) {
		handler = h;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
		View myView = inflater.inflate(R.layout.fragment_main, container, false);
		
		
		//IntentSerive start with 5 random number toasts
		myView.findViewById(R.id.btn_istarth).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent number5 = new Intent(getActivity().getBaseContext(), myIntentService.class);
				number5.putExtra("times", 5);
				Messenger messenger = new Messenger(handler);
				number5.putExtra("MESSENGER", messenger);
				getActivity().startService(number5);
			}
		});

		//IntentSerive stop.  If already stopped, what happens?
		myView.findViewById(R.id.btn_istartn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent number5 = new Intent(getActivity().getBaseContext(), myIntentService.class);
				number5.putExtra("times", 5);
				getActivity().startService(number5);
			}
		});
		//IntentSerive start with 5 random number toasts
		myView.findViewById(R.id.btn_sstarth).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent number5 = new Intent(getActivity().getBaseContext(), myService1.class);
				number5.putExtra("times", 5);
				Messenger messenger = new Messenger(handler);
				number5.putExtra("MESSENGER", messenger);
				getActivity().startService(number5);
			}
		});

		//
		myView.findViewById(R.id.btn_sstartn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent number5 = new Intent(getActivity().getBaseContext(), myService1.class);
				number5.putExtra("times", 5);
				getActivity().startService(number5);
			}
		});
		//
		myView.findViewById(R.id.btn_file).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				URL url = null;
				String file = "";
				try {
					url = new URL ("http://www.cs.uwyo.edu/~seker/courses/4730/example/pic.jpg");
					//for a bigger file: http://www.nasa.gov/images/content/206402main_jsc2007e113280_hires.jpg
					//url = new URL ("http://www.nasa.gov/images/content/206402main_jsc2007e113280_hires.jpg");
					
					file =  "pic.jpg";
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//setup intent and send it.
				Intent getfile = new Intent(getActivity().getBaseContext(), fileDlService.class);
				getfile.putExtra("FILE", file);
				getfile.putExtra("URL", url);
				getActivity().startService(getfile);
			}
		});
		
		return myView;
		
	}

}
