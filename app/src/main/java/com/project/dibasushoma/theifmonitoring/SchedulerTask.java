package com.project.dibasushoma.theifmonitoring;

import android.util.Log;

/**
 * Created by Moon on 11/24/2017.
 */

public class SchedulerTask {

    private ICheckData mainActivity;

    public SchedulerTask(){

        mainActivity = new MainActivity();
        if(mainActivity !=null){
            mainActivity.checkData();
        }else {
            Log.i("check","find null");
            return;
        }

    }

}
