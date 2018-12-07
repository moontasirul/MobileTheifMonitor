package com.project.universityproject.theifmonitoring;

import android.app.Application;
import android.content.Context;

/**
 * Created by Moon on 11/30/2017.
 */

public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
