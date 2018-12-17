package com.project.universityproject.theifmonitoring;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.project.universityproject.theifmonitoring.email_send.SendMail;

import java.util.concurrent.TimeUnit;

/**
 *
 */

public class MainActivity extends AppCompatActivity  {


    private EditText etVerifyCode;
    private EditText etConfirmVerifyCode;
    private EditText etAnotherPhoneNumber;
    private EditText etEmail;

    private LinearLayout rootView;

    private Button btnsubmit;
    private Button btninsertInfoForActiveApp;
    private Button btnLogout;
    private Switch swtActiveApp;

    private DataBaseHelper dataBaseHelper;
    private User mUser;

    public String userName;
    private String deviceId;
    private Button btnCancelJob;


    private JobScheduler jobScheduler;

    private JobInfo jobInfo;

    private boolean isPermissionGrant = false;
    private boolean flag = false;

    private static final String TAG = MainActivity.class.getSimpleName();

    private int simID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnew);
        RunTimePermissionHandler.requestForAccessDeviceLocationAndPhoneState(this);
        initialView();
        setOnClick();



        show();
    }


    private void initialView() {
        etVerifyCode = findViewById(R.id.et_verify_code);
        etConfirmVerifyCode = findViewById(R.id.et_confrm_verifyCode);
        etAnotherPhoneNumber = findViewById(R.id.et_aditional_phn_nuber);
        etEmail = findViewById(R.id.et_email);
        swtActiveApp = findViewById(R.id.switch1);

        btnCancelJob = findViewById(R.id.btn_cencl_job);

        rootView = findViewById(R.id.linearLayout);

        btninsertInfoForActiveApp = findViewById(R.id.btn_active_app);
        btnsubmit = findViewById(R.id.btn_save);
        btnLogout = findViewById(R.id.btn_logout);

        userName = getIntent().getStringExtra("UserName");

        dataBaseHelper = new DataBaseHelper(this);
        mUser = new User();
    }


    public void show() {
        if (dataBaseHelper.checkUser(userName)) {
            mUser = dataBaseHelper.getData(userName);
        }


        if (mUser.getEmailId() != null) {
            btninsertInfoForActiveApp.setText("User Info Stored");
            swtActiveApp.setVisibility(View.VISIBLE);
           // swtActiveApp.setChecked(false);
        } else {
            swtActiveApp.setVisibility(View.GONE);
            return;
        }

    }


    private void setOnClick() {

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

            }
        });

        btninsertInfoForActiveApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RunTimePermissionHandler.requestReadPhoneState(MainActivity.this);
                deviceId = UtilityFunctions.getDeviceId(getApplicationContext());
                Log.i("check data", userName + " " + deviceId);
                rootView.setVisibility(View.VISIBLE);

            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeActiveAppInfo(userName, deviceId, UtilityFunctions.getSimID(getApplicationContext()),
                        etVerifyCode.getText().toString().trim(),
                        etConfirmVerifyCode.getText().toString().trim(),
                        etAnotherPhoneNumber.getText().toString().trim(),
                        etEmail.getText().toString().trim());

                btninsertInfoForActiveApp.setText("User Info Stored");
            }
        });


        btnCancelJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootView.setVisibility(View.GONE);
            }
        });

        swtActiveApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    scheduleJob();
                } else {
                    JobScheduler jobScheduler = (JobScheduler) getSystemService( Context.JOB_SCHEDULER_SERVICE);
                    jobScheduler.cancelAll();

                }

            }
        });
    }


    /**
     * insert data into database for appActive
     *
     * @param userName
     * @param deviceId
     * @param simID
     * @param verifyCode
     * @param confirmVerifyCode
     * @param phoneNumber
     * @param email
     */
    private void storeActiveAppInfo(String userName, String deviceId, String simID, String verifyCode, String confirmVerifyCode, String phoneNumber, String email) {
        if (verifyCode == null && TextUtils.isEmpty(verifyCode)) {
            etVerifyCode.setError("Verify Code Field empty.");
        }

        if (confirmVerifyCode == null && TextUtils.isEmpty(confirmVerifyCode)) {
            etConfirmVerifyCode.setError("Confirm Verify Code Field empty.");
        }

        if (phoneNumber == null && TextUtils.isEmpty(phoneNumber)) {
            etAnotherPhoneNumber.setError("Phone number Field empty.");
        }

        if (email == null && TextUtils.isEmpty(email)) {
            etEmail.setError("Email Field empty.");
        }


        if (dataBaseHelper.checkUser(userName)) {

            mUser.setUserName(userName);
            mUser.setVerifyingCode(verifyCode);
            mUser.setUserPhone(phoneNumber);
            mUser.setEmailId(email);
            mUser.setUserDeviceId(deviceId);
            mUser.setUserSIMID(simID);
            Log.i("check", String.valueOf(dataBaseHelper.checkUser(userName)));
            dataBaseHelper.addAppInfo(mUser);

            // Toast to show success message that record saved successfully
            Toast.makeText(this, "Insert successful", Toast.LENGTH_LONG).show();
            rootView.setVisibility(View.GONE);

            swtActiveApp.setVisibility(View.VISIBLE);
            swtActiveApp.setChecked(false);
        } else {

            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onActivityResult(requestCode, resultCode, data);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("onRequestPR", "called");
        if (requestCode == Constants.REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted(true);
            } else {
                permissionGranted(false);
            }
        }

    }

    public void permissionGranted(boolean isGrant) {
        this.isPermissionGrant = isGrant;
    }

    public boolean isPermissionGrant() {
        return isPermissionGrant;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isPermissionGrant()) {
            permissionGranted(false);
        }

//        storeActiveAppInfo(userName, deviceId, UtilityFunctions.getSimID(getApplicationContext()),
//                etVerifyCode.getText().toString().trim(),
//                etConfirmVerifyCode.getText().toString().trim(),
//                etAnotherPhoneNumber.getText().toString().trim(),
//                etEmail.getText().toString().trim());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    /**
     * Background job scheduler method
     */

    @SuppressLint("WrongConstant")
    private void scheduleJob() {

        jobScheduler = (JobScheduler) getSystemService(
                Context.JOB_SCHEDULER_SERVICE);


        // The JobService that we want to run
        final ComponentName name = new ComponentName(this, JobSchedulerService.class);


        // Schedule the job
        final int result = jobScheduler.schedule(getJobInfo(123, 1, name));

        // If successfully scheduled, log this thing
        if (result == JobScheduler.RESULT_SUCCESS) {

            Toast.makeText(getApplicationContext(), "start job.....", Toast.LENGTH_LONG).show();

        }
    }


    private JobInfo getJobInfo(final int id, final long hour, final ComponentName name) {
        final long interval = TimeUnit.HOURS.toMillis(hour); // run every hour
        final boolean isPersistent = true; // persist through boot
        final int networkType = JobInfo.NETWORK_TYPE_ANY; // Requires some sort of connectivity

        // final JobInfo jobInfo;

        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("userName",userName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobInfo = new JobInfo.Builder(id, name)
                    .setMinimumLatency(40000)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .setExtras(bundle)
                    .build();
        } else {
            jobInfo = new JobInfo.Builder(id, name)
                    .setPeriodic(10000)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .setExtras(bundle)
                    .build();
        }

        return jobInfo;
    }



    /**
     * This method does not use in any where........ till now
     * @param email
     * @param message
     */
    private void sendEmail(String email, String message) {
        Log.i("sendEmail", "called");
        //Creating SendMail object
        SendMail sm = new SendMail(this, email, "user info", message);

        //Executing sendmail to send email
        sm.execute();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }






}
