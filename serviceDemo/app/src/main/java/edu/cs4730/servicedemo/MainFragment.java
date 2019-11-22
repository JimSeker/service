package edu.cs4730.servicedemo;

import java.net.MalformedURLException;
import java.net.URL;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple example of how to call/start services.
 */
public class MainFragment extends Fragment {

    TextView logger;
    String TAG = "MainFragment";

    /*
     * The service will call the handler to send back information.
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Object path = msg.obj;
            Toast.makeText(getActivity().getBaseContext(), path.toString(), Toast.LENGTH_LONG).show();
            return true;
        }
    });

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_main, container, false);
        //logger for the permission checking for the file download service.
        logger = myView.findViewById(R.id.logger);

        //IntentService start with 5 random number toasts
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

        //IntentService starts and creates 5 notifications with random numbers.
        myView.findViewById(R.id.btn_istartn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent number5 = new Intent(getActivity().getBaseContext(), myIntentService.class);
                number5.putExtra("times", 5);
                getActivity().startService(number5);
            }
        });
        //Service start with 5 random number toasts
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

        //Service start with 5 random number and sends notifications.
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
                CheckPerm();
            }
        });

        return myView;

    }

    public void downloadFile() {
        URL url = null;
        String file = "";
        try {
            //url = new URL ("http://www.cs.uwyo.edu/~seker/courses/4730/example/pic.jpg");

            //for a bigger file: http://www.nasa.gov/images/content/206402main_jsc2007e113280_hires.jpg
            //for reason I don't understand, I can't download it from nasa on the phone... weird...
           // url = new URL("http://www.nasa.gov/images/content/206402main_jsc2007e113280_hires.jpg");
            url = new URL ("http://www.cs.uwyo.edu/~seker/courses/4730/example/206402main_jsc2007e113280_hires.jpg");

            //yes, I could write the file name, but this means I don't have think it about if I change files.
            file = URLUtil.guessFileName(url.getFile(), null, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {  //storage sucks now..
                createFile("image/png",file);
                return;  //to the activityreturned ...;
            }

        } catch (MalformedURLException e) {

            e.printStackTrace();
        }

        //setup intent and send it.
        Intent getfile = new Intent(getActivity().getBaseContext(), fileDlService.class);
        getfile.putExtra("FILE", file);
        getfile.putExtra("URL", url);
        getActivity().startService(getfile);
    }


    public void CheckPerm() {
        if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            //I'm on not explaining why, just asking for permission.
            Log.v(TAG, "asking for permissions");
            //note, not ActivityCompat. which sends it to the activity, instead of the fragment.  the super the activity didn't help.
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MainActivity.REQUEST_PERM_ACCESS);

        } else {
            logger.append("Contact Write Access: Granted\n");
            downloadFile();
        }

    }

    private void createFile(String mimeType, String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested MIME type.
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, MainActivity.WRITE_REQUEST_CODE);
    }


    //handle the response.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MainActivity.REQUEST_PERM_ACCESS: {  //external file write fragment.
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    logger.append("External File Write Access: Granted\n");
                    downloadFile();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    logger.append("External File Write Access: Not Granted\n");
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        URL url = null;
        String file = "";
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        try {
            //url = new URL ("http://www.cs.uwyo.edu/~seker/courses/4730/example/pic.jpg");

            //for a bigger file: http://www.nasa.gov/images/content/206402main_jsc2007e113280_hires.jpg
            //for reason I don't understand, I can't download it from nasa on the phone... weird...
            // url = new URL("http://www.nasa.gov/images/content/206402main_jsc2007e113280_hires.jpg");
            url = new URL ("http://www.cs.uwyo.edu/~seker/courses/4730/example/206402main_jsc2007e113280_hires.jpg");

            //yes, I could write the file name, but this means I don't have think it about if I change files.
            file = URLUtil.guessFileName(url.getFile(), null, null);
            } catch (MalformedURLException e) {

                e.printStackTrace();
            }
        if (requestCode == MainActivity.WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                //setup intent and send it.
                Intent getfile = new Intent(getActivity().getBaseContext(), fileDlService.class);
                getfile.putExtra("URI", uri);
                getfile.putExtra("URL", url);
                getActivity().startService(getfile);
            }
        }
    }

}
