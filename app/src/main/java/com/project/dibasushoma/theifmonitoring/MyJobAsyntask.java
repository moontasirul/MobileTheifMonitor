package com.project.dibasushoma.theifmonitoring;

import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.project.dibasushoma.theifmonitoring.email_send.SendMail;

/**
 * Created by Moon on 11/24/2017.
 */
public  class MyJobAsyntask extends AsyncTask<String,Void,String> {

    private DataBaseHelper dataBaseHelper;
    private User mUser;
    private LocationUtil mLocationUtil;

    private String lat;
    private String lang;

    @Override
    protected String doInBackground(String... strings) {
        String userName= strings[0];
        Log.i("checkUserName",userName+"");

        dataBaseHelper = new DataBaseHelper(MyApp.getContext());
        mUser = new User();
        mLocationUtil = new LocationUtil(MyApp.getContext());
        mLocationUtil.init();

        if (mUser == null) {
            return "null";
        }

        if (dataBaseHelper.checkUser(userName)) {
            mUser = dataBaseHelper.getData(userName);
        }

        if (mLocationUtil.isLocationEnabled(MyApp.getContext())) {
            mLocationUtil.getLatLongFromGps();

            lat= getLatitude();
            lang = getLongitude();
        }else {
            return null;
        }



        String locationUrl ="http://maps.google.com/maps?q="+lat+","+lang;

        String currentSIMID = UtilityFunctions.getSimID(MyApp.getContext());

        if (mUser.getUserSIMID().equalsIgnoreCase(currentSIMID)) {
            Log.i("check", "getUserSIMID"+mUser.getUserSIMID());
            SendMail sm = new SendMail(MyApp.getContext(), mUser.getEmailId(),
                    "user info", mUser.getUserDeviceId()+", "+currentSIMID+"Device Location: "+ locationUrl);
            //Executing sendmail to send email
            sm.execute();
        }
        return "background job running...";
    }




    public Location getLocation() {
        return mLocationUtil.getLocation();
    }


    public String getLatitude() {
        return mLocationUtil.getLatitude();
    }


    public String getLongitude() {
        return mLocationUtil.getLongitude();
    }

    @Override
    protected void onPreExecute() {





    }



}
