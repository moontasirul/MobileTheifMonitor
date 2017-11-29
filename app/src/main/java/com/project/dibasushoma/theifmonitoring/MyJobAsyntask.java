package com.project.dibasushoma.theifmonitoring;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.project.dibasushoma.theifmonitoring.email_send.SendMail;

import static android.content.Intent.getIntent;

/**
 * Created by Moon on 11/24/2017.
 */
public  class MyJobAsyntask extends AsyncTask<Void,Void,String> {

    private DataBaseHelper dataBaseHelper;
    private User mUser;

    @Override
    protected void onPreExecute() {

        final MainActivity mainActivity = new MainActivity();


        dataBaseHelper = new DataBaseHelper(MyApp.getContext());
        mUser = new User();
//        final String userName =
//        Log.i("check", "getUser"+userName);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mUser == null) {
                    return;
                }

                if (dataBaseHelper.checkUser("moon")) {
                    mUser = dataBaseHelper.getData("moon");
                }
                String currentSIMID = UtilityFunctions.getSimID(MyApp.getContext());

                if (mUser.getUserSIMID().equalsIgnoreCase(currentSIMID)) {
                    Log.i("check", "getUserSIMID"+mUser.getUserSIMID());
                    SendMail sm = new SendMail(MyApp.getContext(), mUser.getEmailId(), "user info", mUser.getUserDeviceId()+currentSIMID);
                    //Executing sendmail to send email
                    sm.execute();
                }

            }
        });
    }

    @Override
    protected String doInBackground(Void... voids) {




        return "Background task running....";
    }

}
