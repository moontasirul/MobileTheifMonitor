package com.project.dibasushoma.theifmonitoring;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by user123 on 9/18/2017.
 */

public class UtilityFunctions {

    private static ProgressDialog progressDialog;
    /**
     * status bar transparent
     * @param activity
     */
    public static void setStatusBarTransparent(FragmentActivity activity){
        if(activity == null){
            return;
        }
        Window window = activity.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void noInternetDialogBox(final Context context) {
        AlertDialog.Builder builder = getAlertDialogBuilder(context, context.getString(R.string.internet_connection_title), context.getString(R.string.no_inernet_dailog));
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent gpsIntent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(gpsIntent);
            }
        });
        builder.create();
        builder.show();
    }
    public static AlertDialog.Builder getAlertDialogBuilder(Context context, String title, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        return builder;
    }


    public static void showProgressBar(final FragmentActivity aActivity) {

        try {
            if (progressDialog != null || aActivity == null) {
                return;
            }

            progressDialog = new ProgressDialog(aActivity);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Connecting");
            progressDialog.setCancelable(false);
            progressDialog.show();


        } catch (Exception ex) {

        }

    }


    public static void stopProgressBar(final FragmentActivity aActivity) {
        try {

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception ex) {

        }
    }



    public static String getDeviceId(Context context) {
        if (context == null) {
            return "";
        }

        boolean isTelephonyService = false;
        PackageManager packageManager = context.getPackageManager();
        // Check the Package Manager isn't exists
        if (packageManager != null) {
            isTelephonyService = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        }
        // Check it has Telephony Service
        if (isTelephonyService) {
            // Get the IMEI number
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return "IMEI is :" + telephonyManager.getImei();
            } else {
                return "IMEI is :" + telephonyManager.getDeviceId();
            }
        } else {
            // Get the MAC Address
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return "MAC Address: " + wifiManager.getConnectionInfo().getMacAddress();
        }

    }


    public static String getSimID(Context context) {
        if (context == null) {
            return "";
        }

        boolean isTelephonyService = false;
        PackageManager packageManager = context.getPackageManager();
        // Check the Package Manager isn't exists
        if (packageManager != null) {
            isTelephonyService = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        }
        // Check it has Telephony Service
        if (isTelephonyService) {
            // Get the IMEI number
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return "SIM ID is :" + telephonyManager.getSimSerialNumber();
            } else {
                return "SIM ID is :" + telephonyManager.getSubscriberId();
            }
        } else {
            // Get the MAC Address
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return "MAC Address: " + wifiManager.getConnectionInfo().getMacAddress();
        }

    }




//    public static void changedFragment(FragmentActivity activity, Fragment fragment, boolean isAddToBackStack) {
//        if (activity == null || fragment == null) {
//            return;
//        }
//        FragmentTransaction transaction = activity.getSupportFragmentManager()
//                .beginTransaction();
//        if (isAddToBackStack) {
//            transaction.addToBackStack(null);
//            Log.i("changeFragment", "Granted");
//        }
//        transaction.replace(R.id.container, fragment, Constants.CURRENT_FRAGMENT);
//        transaction.commitAllowingStateLoss();
//        activity.getSupportFragmentManager().executePendingTransactions();
//    }



    /**
     * invoke to hide keyboard
     *
     * @param activity
     */
    public static void hideVirtualKeyboard(FragmentActivity activity) {
        if (activity == null) {
            return;
        }

        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(null == activity
                .getCurrentFocus() ? null : activity.getCurrentFocus()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static void showSoftKeyboard(FragmentActivity activity, View view) {
        if (activity == null || view == null) {
            return;
        }

        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    /**
     * check the user name is alpha uumaric or not
     * @param aString
     * @return
     */
    public static boolean isAlphaNumeric(String aString) {
        String reges = "[A-Za-z0-9]+";
        return aString.matches(reges);
    }



}
