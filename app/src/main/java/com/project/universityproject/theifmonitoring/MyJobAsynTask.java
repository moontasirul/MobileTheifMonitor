package com.project.universityproject.theifmonitoring;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.project.universityproject.theifmonitoring.email_send.EmailConfig;
import com.project.universityproject.theifmonitoring.email_send.GMailSender;
import com.project.universityproject.theifmonitoring.email_send.SendMail;


public  class MyJobAsynTask extends AsyncTask<String,Void,String> {

    private DataBaseHelper dataBaseHelper;
    private User mUser;
    private LocationUtil mLocationUtil;

    private String lat;
    private String lang;



    @Override
    protected String doInBackground(String... strings) {
        String userName= strings[0];

        Log.i("check_banckgroundJob","called");
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



        final String locationUrl ="http://maps.google.com/maps?q="+lat+","+lang;


        final String currentSIMID = UtilityFunctions.getSimID(MyApp.getContext());


        if (mUser.getUserSIMID().equalsIgnoreCase(currentSIMID)) {
            Log.i("check", "getUserSIMID"+mUser.getUserSIMID());


            // older version .........................................................
//            SendMail sm = new SendMail(MyApp.getContext(), mUser.getEmailId(),
//                    "Mobile Thief Monitoring System ", mUser.getUserDeviceId()+", "+currentSIMID+"Device Location: "+ locationUrl);
//            //Executing sendmail to send email
//            sm.execute();



            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        GMailSender sender = new GMailSender(EmailConfig.EMAIL,
                                EmailConfig.PASSWORD);

                        Log.i("SendMail", sender.toString());
                        sender.sendMail("Mobile Thief Monitoring System ", mUser.getUserDeviceId()+", "+currentSIMID+"Device Location: "+ locationUrl,
                                EmailConfig.EMAIL, mUser.getEmailId());


                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                    }
                }

            }).start();
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
