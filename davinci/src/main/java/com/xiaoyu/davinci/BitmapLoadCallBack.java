package com.xiaoyu.davinci;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface BitmapLoadCallBack {

    void onPrepareLoad(Drawable placeHolderDrawable);

    void onSuccess(Bitmap bitmap);

    void onError(Exception e);

    class EmptyCallback implements BitmapLoadCallBack {

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

        @Override public void onSuccess(Bitmap bitmap) {
        }

        @Override public void onError(Exception e) {
        }
    }

}
