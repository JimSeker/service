package edu.cs4730.workmanagerdemo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import edu.cs4730.workmanagerdemo.databinding.ActivityMainBinding;

/**
 * need a simple worker, parameter worker.  Then chain them together for the last example.  maybe a parallel
 * Make sure you are looking at the logcat as well.  You can see what the workers are doing.
 * <p>
 * see https://developer.android.com/topic/libraries/architecture/adding-components
 * note, no toasts in workers, so POST_NOTIFICATION permission is not needed.
 */
@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set the click listeners for the three buttons.
        binding.btnOneshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneshot();
            }
        });

        binding.btnParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                param();
            }
        });

        binding.btnChain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chain();
            }
        });
    }


    /**
     * This will create a oneshot workerA task and schedule it to run once.
     * commented out code shows how to make it recur every 24 hours.
     */
    public void oneshot() {
        //for a schedule once
        OneTimeWorkRequest runWorkA = new OneTimeWorkRequest.Builder(WorkerA.class).build();
        /*
        //for recur schedule, say every 24 hours, comment out above, since duplicate variables.
        PeriodicWorkRequest.Builder workerkBuilder =
            new PeriodicWorkRequest.Builder(WorkerA.class, 24,  TimeUnit.HOURS);
        // ...if you want, you can apply constraints to the builder here...
        Constraints myConstraints = new Constraints.Builder()
            //.setRequiresDeviceIdle(true)
            .setRequiresCharging(true)
            // Many other constraints are available, see the
            // Constraints.Builder reference
            .build();

       // Create the actual work object:
        PeriodicWorkRequest runWorkA = workerkBuilder.setConstraints(myConstraints)
        .build();
       */

        //now enqueue the task so it can be run.
        WorkManager.getInstance(getApplicationContext()).enqueue(runWorkA);

        //not necessary, but this will tell us the status of the task.
        LiveData<WorkInfo> status = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(runWorkA.getId());
        status.observe(this, new Observer<WorkInfo>() {

            @Override
            public void onChanged(@Nullable WorkInfo workStatus) {
                switch (workStatus.getState()) {
                    case BLOCKED:
                        binding.tvOneshot.setText("Status is Blocked");
                        break;
                    case CANCELLED:
                        binding.tvOneshot.setText("Status is canceled");
                        break;
                    case ENQUEUED:
                        binding.tvOneshot.setText("Status is enqueued");
                        break;
                    case FAILED:
                        binding.tvOneshot.setText("Status is failed");
                        break;
                    case RUNNING:
                        binding.tvOneshot.setText("Status is running");
                        break;
                    case SUCCEEDED:
                        binding.tvOneshot.setText("Status is succeeded");
                        break;
                    default:
                        binding.tvOneshot.setText("Status is unknown");
                }

            }
        });
    }

    //This shows how to set input and receive a result for a worker task.
    public void param() {
        // Create the Data object:
        final Data myData = new Data.Builder()
                // We need to pass three integers: X, Y, and Z
                .putInt(WorkerParameters.KEY_X_ARG, 42)
                // ... and build the actual Data object:
                .build();

        // ...then create and enqueue a OneTimeWorkRequest that uses those arguments
        OneTimeWorkRequest mathWork = new OneTimeWorkRequest.Builder(WorkerParameters.class).setInputData(myData).build();
        WorkManager.getInstance(getApplicationContext()).enqueue(mathWork);

        //now set the observer to get the result.
        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(mathWork.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo status) {
                if (status != null && status.getState().isFinished()) {
                    int myResult = status.getOutputData().getInt(WorkerParameters.KEY_RESULT, -1);
                    binding.tvParam.setText("Result is " + myResult);
                }
            }
        });
    }

    //This method changes together WorkerA and B in parallel and then C.
    public void chain() {
        //first setup the requests
        OneTimeWorkRequest runWorkA = new OneTimeWorkRequest.Builder(WorkerA.class).build();
        OneTimeWorkRequest runWorkB = new OneTimeWorkRequest.Builder(WorkerB.class).build();
        OneTimeWorkRequest runWorkC = new OneTimeWorkRequest.Builder(WorkerC.class).build();
        //now setup them up to run, A and B together.  Once they are complete then launch C.
        WorkManager.getInstance(getApplicationContext())
                // First, run all the A tasks (in parallel):
                .beginWith(Arrays.asList(runWorkA, runWorkB))
                // ...when all A tasks are finished, run the single B task:
                .then(runWorkC).enqueue();


        // not necessary, but so the display updates get the LiveData for each and set to update the textviews.

        LiveData<WorkInfo> status = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(runWorkA.getId());
        status.observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workStatus) {
                switch (workStatus.getState()) {
                    case BLOCKED:
                        binding.tvChaina.setText("A Status is Blocked");
                        break;
                    case CANCELLED:
                        binding.tvChaina.setText("A Status is canceled");
                        break;
                    case ENQUEUED:
                        binding.tvChaina.setText("A Status is enqueued");
                        break;
                    case FAILED:
                        binding.tvChaina.setText("A Status is failed");
                        break;
                    case RUNNING:
                        binding.tvChaina.setText("A Status is running");
                        break;
                    case SUCCEEDED:
                        binding.tvChaina.setText("A Status is succeeded");
                        break;
                    default:
                        binding.tvChaina.setText("A Status is unknown");
                }
            }
        });

        LiveData<WorkInfo> statusb = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(runWorkB.getId());
        statusb.observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workStatus) {
                switch (workStatus.getState()) {
                    case BLOCKED:
                        binding.tvChainb.setText("B Status is Blocked");
                        break;
                    case CANCELLED:
                        binding.tvChainb.setText("B Status is canceled");
                        break;
                    case ENQUEUED:
                        binding.tvChainb.setText("B Status is enqueued");
                        break;
                    case FAILED:
                        binding.tvChainb.setText("B Status is failed");
                        break;
                    case RUNNING:
                        binding.tvChainb.setText("B Status is running");
                        break;
                    case SUCCEEDED:
                        binding.tvChainb.setText("B Status is succeeded");
                        break;
                    default:
                        binding.tvChainb.setText("B Status is unknown");
                }
            }
        });

        LiveData<WorkInfo> statusc = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(runWorkC.getId());
        statusc.observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workStatus) {
                switch (workStatus.getState()) {
                    case BLOCKED:
                        binding.tvChainc.setText("C Status is Blocked");
                        break;
                    case CANCELLED:
                        binding.tvChainc.setText("C Status is canceled");
                        break;
                    case ENQUEUED:
                        binding.tvChainc.setText("C Status is enqueued");
                        break;
                    case FAILED:
                        binding.tvChainc.setText("C Status is failed");
                        break;
                    case RUNNING:
                        binding.tvChainc.setText("C Status is running");
                        break;
                    case SUCCEEDED:
                        binding.tvChainc.setText("C Status is succeeded");
                        break;
                    default:
                        binding.tvChainc.setText("C Status is unknown");
                }
            }
        });
    }
}
