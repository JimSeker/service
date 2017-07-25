package edu.cs4730.servicedemo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;


/*
 * Nothing to see here.  Go to the MainFragment for the code to call/start a service.
 *
 * The activity handles the response for the file premission request.
 */


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_PERM_ACCESS = 1;
    MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mainFragment).commit();
        }
    }

    //handle the response.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERM_ACCESS: {  //external file write fragment.
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mainFragment.logger.append("External File Write Access: Granted\n");
                    mainFragment.downloadFile();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    mainFragment.logger.append("External File Write Access: Not Granted\n");
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
