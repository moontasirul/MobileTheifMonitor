package com.project.dibasushoma.theifmonitoring;

import android.os.AsyncTask;
import android.util.Log;

import com.project.dibasushoma.theifmonitoring.email_send.SendMail;

/**
 * Created by Moon on 11/24/2017.
 */
public  class MyJobAsyntask extends AsyncTask<String,Void,String> {

    private DataBaseHelper dataBaseHelper;
    private User mUser;

    @Override
    protected String doInBackground(String... strings) {
        String userName= strings[0];
        Log.i("checkUserName",userName+"");

        dataBaseHelper = new DataBaseHelper(MyApp.getContext());
        mUser = new User();

        if (mUser == null) {
            return "null";
        }

        if (dataBaseHelper.checkUser(userName)) {
            mUser = dataBaseHelper.getData(userName);
        }
        String currentSIMID = UtilityFunctions.getSimID(MyApp.getContext());

        if (!mUser.getUserSIMID().equalsIgnoreCase(currentSIMID)) {
            Log.i("check", "getUserSIMID"+mUser.getUserSIMID());
            SendMail sm = new SendMail(MyApp.getContext(), mUser.getEmailId(), "user info", mUser.getUserDeviceId()+currentSIMID);
            //Executing sendmail to send email
            sm.execute();
        }
        return "background job running...";
    }




    @Override
    protected void onPreExecute() {





    }



}
