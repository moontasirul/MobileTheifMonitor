package com.project.dibasushoma.theifmonitoring;


import android.support.annotation.NonNull;

/**
 * Created by user123 on 10/4/2017.
 */

public interface ILocation {

    String getLatitude();

    String getLongitude();

    void setLatitude(@NonNull String latitude);

    void setLongitude(@NonNull String longitude);
}
