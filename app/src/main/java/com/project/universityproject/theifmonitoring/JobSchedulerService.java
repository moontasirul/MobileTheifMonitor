package com.project.universityproject.theifmonitoring;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;


/**
 * This is background job service class.
 * It have been used to handle background jobService
 * and execute that scheduler tasks.
 */

public class JobSchedulerService extends JobService {


    private static final String TAG = JobSchedulerService.class.getSimpleName();

    private MyJobAsynTask myJobAsynTask;




    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters params) {
        String userName = params.getExtras().getString("userName");


        myJobAsynTask = new MyJobAsynTask(){


            @Override
            protected void onPostExecute(String s) {
                Log.i("checktest", s);

                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                jobFinished(params,true);

            }
        };

        myJobAsynTask.execute(userName);
        jobFinished(params,true);
        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        myJobAsynTask.cancel(true);

        return false;
    }
}
