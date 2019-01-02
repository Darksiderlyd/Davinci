package com.xiaoyu.imageloader;

import android.app.Application;

import com.xiaoyu.davinci.Davinci;

public class App extends Application {
    public static App gApp;

    @Override
    public void onCreate() {
        super.onCreate();
        gApp = this;
//        Davinci.get().setGlobalLoader(new GlideLoader());
        Davinci.get().setGlobalLoader(new PicassoLoader());
    }
}
