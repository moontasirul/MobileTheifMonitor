package com.project.dibasushoma.theifmonitoring;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * Created by user123 on 9/22/2017.
 */

public class RunTimePermissionHandler {


    /**
     * set all premission request code
     */
    public static final int REQUEST_CODE = 1010;

    /**
     * set call permission request code
     */
    public static final int REQUEST_CODE_CALL = 1;

    /**
     * set camera permission request code
     */
    public static final int REQUEST_CODE_CAMERA = 2;

    /**
     * invoke screen overlay request code
     */
    public static final int REQUEST_CODE_FOR_SCREEN_OVERLAY = 101;

    /**
     * invoke package key for set package manager
     */
    public static final String PACKAGE_KEY = "package:";

    /**
     * declare only call permission in manifest
     */
    public static final String CALL = Manifest.permission.CALL_PHONE;

    /**
     * declare only CAMERA permission in manifest
     */
    public static final String CAMERA = Manifest.permission.CAMERA;

    /**
     * declare a string Location array which is deployed two types of location permission in manifest
     */
    public static final String[] LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    /**
     * declare only READ_STORAGE permission in manifest
     */
    public static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    /**
     * declare a string array which is deployed two different permission in manifest.
     * Which is camera access and write external storage.
     */
    public static final String[] CAMERA_AND_STORAGE = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    /**
     * declare only send sms permission in manifest
     */
    public static final String[] SEND_SMS = {Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS};

    /**
     * This method will check the permission on manifest for that activity.
     *
     * @param activity
     * @param requestCode
     * @param requestKey
     * @return
     *
     */
    public static boolean checkPermission(Activity activity, int requestCode, String requestKey) {
        if (ActivityCompat.checkSelfPermission(activity, requestKey) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{requestKey}, requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method will check the permission of a string array on manifest for that activity. when two or more types of permission
     * needs at a time then we will use this method.
     * @param activity
     * @param requestCode
     * @param requestKey
     * @return
     */
    public static boolean checkPermission(Activity activity, int requestCode, String[] requestKey) {
        if (ActivityCompat.checkSelfPermission(activity, String.valueOf(requestKey)) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, requestKey, requestCode);
            return false;
        } else {
            return true;
        }
    }


    /**
     * Check permission Overlay
     * open a popup and accept runtime permission and send result
     * @param activity
     * @return
     */
    public static boolean checkDrawOverlayPermission(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!Settings.canDrawOverlays(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse(PACKAGE_KEY + activity.getPackageName()));
            activity.startActivityForResult(intent, REQUEST_CODE_FOR_SCREEN_OVERLAY);
            return false;
        } else {
            return true;
        }
    }


    /**
     * Imvoke to get permissions to read phone state
     *
     * @return
     */
    public static boolean requestReadPhoneState(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(activity, READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            activity.requestPermissions(new String[]{READ_PHONE_STATE}, Constants.REQUEST_READ_PHONE_STATE);
        }
        return false;
    }


    /**
     * Location permission array
     */
    private static String[] locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};




    /**
     * Invoke to check read phone state permission.
     * If permission is grant
     *
     * @param activity
     * @return
     */
    public static boolean requestForAccessDeviceLocation(FragmentActivity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            activity.requestPermissions(locationPermissions, Constants.REQUEST_LOCATION_ACCESS);
        }
        return false;
    }



}
