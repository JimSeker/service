package edu.cs4730.workmanagerdemo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;


/*
 * need a simple worker, parameter worker.  Then chain them together for the last example.  maybe a parallel 
 */

public class MainActivity extends AppCompatActivity {

    TextView tv_oneshot, tv_param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_oneshot = findViewById(R.id.tv_oneshot);
        tv_param = findViewById(R.id.tv_param);

        findViewById(R.id.btn_oneshot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneshot();
            }
        });

        findViewById(R.id.btn_param).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                param();
            }
        });
    }


    public void oneshot() {
        OneTimeWorkRequest runWorkA = new OneTimeWorkRequest.Builder(WorkerA.class)
            .build();
        WorkManager.getInstance().enqueue(runWorkA);
        LiveData<WorkStatus> status = WorkManager.getInstance().getStatusById(runWorkA.getId());
        status.observe(this, new Observer<WorkStatus>() {
            @Override
            public void onChanged(@Nullable WorkStatus workStatus) {
                switch (workStatus.getState()) {
                    case BLOCKED:
                        tv_oneshot.setText("Status is Blcoked");
                        break;
                    case CANCELLED:
                        tv_oneshot.setText("Status is canceled");
                        break;
                    case ENQUEUED:
                        tv_oneshot.setText("Status is enqueued");
                        break;
                    case FAILED:
                        tv_oneshot.setText("Status is failed");
                        break;
                    case RUNNING:
                        tv_oneshot.setText("Status is running");
                        break;
                    case SUCCEEDED:
                        tv_oneshot.setText("Status is succeeded");
                        break;
                    default:
                        tv_oneshot.setText("Status is unknown");
                }

            }
        });
    }

    public void param() {
        // Create the Data object:
        final Data myData = new Data.Builder()
            // We need to pass three integers: X, Y, and Z
            .putInt(WorkerParameters.KEY_X_ARG, 42)
            // ... and build the actual Data object:
            .build();

        // ...then create and enqueue a OneTimeWorkRequest that uses those arguments
        OneTimeWorkRequest mathWork = new OneTimeWorkRequest.Builder(WorkerParameters.class)
            .setInputData(myData)
            .build();
        WorkManager.getInstance().enqueue(mathWork);

        //now set the observer to get the result.
        WorkManager.getInstance().getStatusById(mathWork.getId())
            .observe(this, new Observer<WorkStatus>() {
                @Override
                public void onChanged(@Nullable WorkStatus status) {
                    if (status != null && status.getState().isFinished()) {
                        int myResult = status.getOutputData().getInt(WorkerParameters.KEY_RESULT,
                            -1);
                        tv_param.setText("Result is " + myResult);
                    }
                }
            });
    }
}
