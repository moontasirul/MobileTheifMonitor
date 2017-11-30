package com.project.dibasushoma.theifmonitoring;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;




public class JobSchedulerService extends JobService {


    private static final String TAG = JobSchedulerService.class.getSimpleName();

    private MyJobAsyntask myJobAsyntask;

    private ICheckData iCheckData;


    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters params) {

        String userName = params.getExtras().getString("userName");

        myJobAsyntask = new MyJobAsyntask(){


            @Override
            protected void onPostExecute(String s) {
                Log.i("checktest", s);

//                final MainActivity mainActivity = new MainActivity();
//
//                mainActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mainActivity.checkDataNew();
//                    }
//                });

                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                jobFinished(params,false);
            }
        };
        myJobAsyntask.execute(userName);

        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        myJobAsyntask.cancel(true);

        return false;
    }
}
