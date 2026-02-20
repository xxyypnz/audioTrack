package com.share.audiotrackdemo;

import android.app.Application;
import android.util.Log;

import com.share.audiotrackdemo.syfuction.wave.waveImpl.NarrowBandFilterCoef;

public class AppAppliction  extends Application {
    public static Application mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;

        Log.e("xfan", "before NarrowBandFilterCoef.initialize");
        NarrowBandFilterCoef.initialize(this);
        Log.e("xfan", "after NarrowBandFilterCoef.initialize");

    }
}
