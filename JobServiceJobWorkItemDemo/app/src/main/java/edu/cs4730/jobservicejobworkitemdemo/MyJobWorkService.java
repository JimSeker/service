package edu.cs4730.jobservicejobworkitemdemo;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.app.job.JobWorkItem;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * This is basically a google example (except like most google examples it didn't work) with
 * all the necessary corrections to work correctly.  The org code comes from:
 * https://developer.android.com/reference/android/app/job/JobParameters.html#dequeueWork()
 *
 */

public class MyJobWorkService extends JobService {
    public MyJobWorkService() {
    }
    private NotificationManager mNM;
    private CommandProcessor mCurProcessor;

    /**
     * This is a task to dequeue and process work in the background.
     */
    final class CommandProcessor extends AsyncTask<Void, Void, Void> {
        private final JobParameters mParams;

        CommandProcessor(JobParameters params) {
            mParams = params;
        }

        @Override
        protected Void doInBackground(Void... params) {
            boolean cancelled;
            JobWorkItem work;

            /*
             * Iterate over available work.  Once dequeueWork() returns null, the
             * job's work queue is empty and the job has stopped, so we can let this
             * async task complete.
             */
            while (!(cancelled=isCancelled()) && (work=mParams.dequeueWork()) != null) {
                String txt = work.getIntent().getStringExtra("Key1");
                Log.i("JobWorkService", "Processing work: " + work + ", msg: " + txt);
                showNotification(txt);

                // Process work here...  we'll pretend by sleeping.
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }

                hideNotification();

                // Tell system we have finished processing the work.
                Log.i("JobWorkService", "Done with: " + work);
                mParams.completeWork(work);
            }

            if (cancelled) {
                Log.i("JobWorkService", "CANCELLED!");
            }

            return null;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Log.i("JobWorkService", "Service Created");
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        hideNotification();
        Log.i("JobWorkService", "Service Destroyed");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        // Start task to pull work out of the queue and process it.
        mCurProcessor = new CommandProcessor(params);
        mCurProcessor.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // Allow the job to continue running while we process work.
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Have the processor cancel its current work.
        mCurProcessor.cancel(true);

        // Tell the system to reschedule the job -- the only reason we would be here is
        // because the job needs to stop for some reason before it has completed all of
        // its work, so we would like it to remain to finish that work in the future.
        return true;
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification(String text) {
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification.Builder noteBuilder = new Notification.Builder(this, MainActivity.id1)
                .setSmallIcon(R.drawable.stat_sample)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Job service is running.")  // the label
                .setContentText(text)  // the contents of the entry
                .setChannelId(MainActivity.id1)
                .setContentIntent(contentIntent);  // The intent to send when the entry is clicked

        // We show this for as long as our service is processing a command.
        noteBuilder.setOngoing(true);

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNM.notify(100, noteBuilder.build());
    }

    private void hideNotification() {
        mNM.cancel(100);
    }
}
