package com.gsy.ml.ui.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/4/5.
 */

public class MyApplicaticon extends Application {


    public static Context mApplicaticon;

    public static Context instance()
    {
        return mApplicaticon;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicaticon=this;
    }
}
