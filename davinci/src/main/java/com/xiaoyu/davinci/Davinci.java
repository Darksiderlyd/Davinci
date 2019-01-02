package com.xiaoyu.davinci;

import android.net.Uri;

import java.io.File;

public class Davinci {

    private static ILoader mLoader;

    private Davinci() {
    }

    private static final class DavinciHolder {
        private static Davinci instance = new Davinci();
    }

    public static Davinci get() {
        return DavinciHolder.instance;
    }

    public void setGlobalLoader(ILoader loader) {
        mLoader = loader;
    }


    public LoadOptions load(String path) {
        return new LoadOptions(path);
    }

    public LoadOptions load(Uri uri) {
        return new LoadOptions(uri);
    }

    public LoadOptions load(File file) {
        return new LoadOptions(file);
    }

    public LoadOptions load(int resourceId) {
        return new LoadOptions(resourceId);
    }

    protected void loadImage(LoadOptions options) {
        ILoader loader = options.getLoader();
        if (loader != null) {
            loader.load(options);
        } else {
            mLoader.load(options);
        }
    }

}
