package com.xiaoyu.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.xiaoyu.davinci.BitmapLoadCallBack;
import com.xiaoyu.davinci.ILoader;
import com.xiaoyu.davinci.LoadOptions;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class GlideLoader implements ILoader {


    @Override
    public void load(LoadOptions loadOptions) {
        GlideRequests with = GlideApp.with(App.gApp);
        GlideRequest glideRequest = null;
        if (!TextUtils.isEmpty(loadOptions.getPath())) {
            glideRequest = with.load(loadOptions.getPath());
        } else if (loadOptions.getFile() != null) {
            glideRequest = with.load(loadOptions.getFile());
        } else if (loadOptions.getUri() != null) {
            glideRequest = with.load(loadOptions.getUri());
        } else if (loadOptions.getDrawableResId() != 0) {
            glideRequest = with.load(loadOptions.getDrawableResId());
        }

        if (glideRequest == null)
            throw new NullPointerException("glideRequest must not be null");


        if (loadOptions.getTargetWidth() > 0 && loadOptions.getTargetHeight() > 0) {
            glideRequest = glideRequest.override(loadOptions.getTargetWidth(), loadOptions.getTargetWidth());
        }

        if (loadOptions.isCenterInside()) {
            glideRequest = glideRequest.centerInside();
        } else if (loadOptions.isCenterCrop()) {
            glideRequest = glideRequest.centerCrop();
        }

        if (loadOptions.getConfig() != null) {

        }
        if (loadOptions.getErrorResId() != 0) {
            glideRequest = glideRequest.error(loadOptions.getErrorResId());
        }
        if (loadOptions.getPlaceholderResId() != 0) {
            glideRequest = glideRequest.placeholder(loadOptions.getPlaceholderResId());
        }
        if (loadOptions.getRadius() != 0) {
            if (loadOptions.getTargetView() instanceof ImageView) {
                glideRequest = glideRequest.transform(new RoundedCornersTransformation(loadOptions.getRadius(), 0));
            }
        }


        glideRequest = glideRequest.skipMemoryCache(loadOptions.isSkipLocalCache() || loadOptions.isSkipNetCache());

        if (loadOptions.getDegrees() != 0) {

        }

        if (loadOptions.getTargetView() instanceof ImageView) {
            glideRequest.into(((ImageView) loadOptions.getTargetView()));
        } else if (loadOptions.getCallBack() != null) {
            glideRequest.into(new XqsTarget(loadOptions.getCallBack()));
        }
    }

    class XqsTarget implements Target<Bitmap> {

        private BitmapLoadCallBack callBack;

        public XqsTarget(BitmapLoadCallBack callBack) {
            this.callBack = callBack;
        }

        public XqsTarget() {}

        @Override
        public void onLoadStarted(@Nullable Drawable placeholder) {
            callBack.onPrepareLoad(placeholder);
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            callBack.onError(null);
        }

        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            callBack.onSuccess(resource);
        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {

        }

        @Override
        public void getSize(@NonNull SizeReadyCallback cb) {

        }

        @Override
        public void removeCallback(@NonNull SizeReadyCallback cb) {

        }

        @Override
        public void setRequest(@Nullable Request request) {

        }

        @Nullable
        @Override
        public Request getRequest() {
            return null;
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onDestroy() {

        }
    }


    @Override
    public void clearDiskCache() {
        GlideApp.get(App.gApp).clearDiskCache();
    }

    @Override
    public void clearMemoryCache() {
        GlideApp.get(App.gApp).clearMemory();
    }
}
