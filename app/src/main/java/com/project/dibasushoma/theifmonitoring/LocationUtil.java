package com.project.dibasushoma.theifmonitoring;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class LocationUtil implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ILocation {

    public GoogleApiClient mGoogleApiClient;

    public LocationRequest mLocationRequest;
    private FusedLocationProviderApi fusedLocationProviderApi;

    public String latitude;
    public String longitude;
    private Location location;
    private Context activity;


    public static final int REQUEST_CHECK_SETTINGS = 105;

    public static final int REQUEST_CHECK_SETTINGS_ADDRESS = 106;


    public LocationUtil(Context activity) {
        this.activity = activity;
    }


    public void init() {
        mLocationRequest = getLocationRequest().create();
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        buildGoogleApiClient();
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


    private GoogleApiClient.Builder getGoogleClientBuilder() {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API);
        return builder;
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }

    public void disCannectGoogleClient() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void disCannectLocationRequest() {
        if (mLocationRequest != null) {
            mLocationRequest = null;
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient.isConnected())
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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

    public void getLatLongFromGps() {
        Log.i("getLatLongFromGps", "called");
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.i("getLatLongFromGps", "called2");
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
            Log.i("getLatLongFromGps", "called3");
        }
        Location location = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (location != null) {
            Log.i("getLatLongFromGps", "called4");
            setLocation(location);
            setLatitude(location.getLatitude() + "");
            setLongitude(location.getLongitude() + "");
            Log.i("LatLong", "LAT:" + location.getLatitude() + " LONG:" + location.getLongitude());
        }
        Log.i("getLatLongFromGps", "called5");
    }

    public void getLatLongFromAddress(String address) {
        Geocoder geoCoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                setLatitude(addresses.get(0).getLatitude() + "");
                setLongitude(addresses.get(0).getLongitude() + "");
                Log.i("LatLong", "LAT:" + getLatitude() + " LONG:" + getLongitude());
            }
        } catch (Exception ee) {
            ee.printStackTrace();
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

    public void displayLocationSettingsRequest() {
        mLocationRequest = getLocationRequest().create();
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }
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
                            status.startResolutionForResult((Activity) activity, REQUEST_CHECK_SETTINGS);
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
    public void setLocation(@NonNull Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
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
