package com.project.universityproject.theifmonitoring;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;


import androidx.work.Data;
import androidx.work.Worker;



public class MyWorker extends Worker {

    private MyJobAsynTask myJobAsynTask;

//    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//    }


    @NonNull
    @Override
    public Worker.Result doWork() {


        Data data = getInputData();
        String userName = data.getString(MainActivity.KEY_USER_NAME);

        myJobAsynTask = new MyJobAsynTask() {
            @Override
            protected void onPostExecute(String s) {
                Log.i("checktest", s);

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        };

        myJobAsynTask.execute(userName);



            Log.i("checktest", "called");
           // return Result.success();

        return Result.SUCCESS;
        }

    }