package com.project.dibasushoma.theifmonitoring;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;


public class LocationUtil implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ILocation {

    public GoogleApiClient mGoogleApiClient;

    public LocationRequest mLocationRequest;

    public String latitude;
    public String longitude;
    private Context context;
    private Location location;
    private FragmentActivity activity;


    public static final int REQUEST_CHECK_SETTINGS = 1;


    public LocationUtil(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
        init();
    }

    public void init() {
        buildGoogleApiClient();

    }

    public void getLatLongFromGPSDevice() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }

        Location location = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (location != null) {
            setLatitude(location.getLatitude() + "");
            setLongitude(location.getLongitude() + "");

            Log.i("LatLong", "LAT:" + getLatitude() + " LONG:" + getLongitude());
        }
    }

    /**
     * buildGoogleApiClient method is used to build GoogleApiClient.
     * <p>
     * * GoogleApiClient.Builder is used to configure client.
     * .addConnectionCallbacks provides callbacks that are called when client connected or disconnected.
     * .addOnConnectionFailedListener covers scenarios of failed attempt of connect client to service.
     * .addApi adds the LocationServices API endpoint from Google Play Services.
     * mGoogleApiClient.connect(): A client must be connected before excecuting any operation.
     */
    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = getGoogleClientBuilder()
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

    }


    @Override
    public void onConnected(Bundle bundle) {
        getLatLongFromGPSDevice();
    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        try {

            //Place current location marker
            latitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";
            Log.d("LatLong", "seelog: " + latitude + "\t" + longitude);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        } catch (Resources.NotFoundException e) {

        } catch (IllegalStateException e) {

        } catch (Exception e) {

        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


    }


    /**
     * Check the device Location Service is enable or disable
     */
    public void showEnableGpsDialog() {
        if (!isLocationEnabled(context)) {
            displayLocationSettingsRequest();
        }
    }

    public boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    protected synchronized GoogleApiClient.Builder getGoogleClientBuilder() {

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API);
        return builder;
    }

    protected synchronized LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }

    public void displayLocationSettingsRequest() {
        Log.i("LocationSettingsRequest", "called");
        mGoogleApiClient = getGoogleClientBuilder().build();
        mGoogleApiClient.connect();
        mLocationRequest = getLocationRequest().create();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
//                            Log.i("tag", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    @Override
    public String getLatitude() {
        return latitude;
    }

    @Override
    public String getLongitude() {
        return longitude;
    }

    @Override
    public void setLatitude(@NonNull String latitude) {
        this.latitude = latitude;
    }

    @Override
    public void setLongitude(@NonNull String longitude) {
        this.longitude = longitude;
    }
}
