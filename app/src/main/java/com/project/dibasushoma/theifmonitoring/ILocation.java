package com.project.dibasushoma.theifmonitoring;


import android.location.Location;
import android.support.annotation.NonNull;

/**
 * To handel GPS location
 * Created by moontasirul on 10/4/2017.
 *
 * @author Atom AP Ltd.
 */

public interface ILocation {

    void setLocation(@NonNull Location location);

    Location getLocation();

    String getLatitude();

    String getLongitude();

    void setLatitude(@NonNull String latitude);

    void setLongitude(@NonNull String longitude);
}
