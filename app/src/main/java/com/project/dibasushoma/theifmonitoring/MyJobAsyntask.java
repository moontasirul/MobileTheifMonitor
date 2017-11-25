package com.project.dibasushoma.theifmonitoring;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.project.dibasushoma.theifmonitoring.email_send.SendMail;

/**
 * Created by Moon on 11/24/2017.
 */

public class MyJobAsyntask extends AsyncTask<Void,Void,String> {


    @Override
    protected String doInBackground(Void... voids) {


        return "Background task running....";
    }

}
