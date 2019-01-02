package com.xiaoyu.davinci;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import java.io.File;

public class LoadOptions {

    private int placeholderResId;
    private int errorResId;
    private boolean isCenterCrop;
    private boolean isCenterInside;
    private boolean skipLocalCache; //是否缓存到本地
    private boolean skipNetCache;
    private Bitmap.Config config = Bitmap.Config.RGB_565;
    private int targetWidth;
    private int targetHeight;
    private int radius; //圆角角度
    private float degrees; //旋转角度.注意:picasso针对三星等本地图片，默认旋转回0度，即正常位置。此时不需要自己rotate
    private Drawable placeholder;
    private View targetView;//targetView展示图片
    private BitmapLoadCallBack callBack;
    private String path;
    private File file;
    private int drawableResId;
    private Uri uri;
    private ILoader loader;//实时切换图片加载库


    public LoadOptions(String path) {
        this.path = path;
    }

    public LoadOptions(Uri uri) {
        this.uri = uri;
    }

    public LoadOptions(File file) {
        this.file = file;
    }

    public LoadOptions(int resourceId) {
        this.drawableResId = resourceId;
    }


    public void into(View targetView) {
        this.targetView = targetView;
        Davinci.get().loadImage(this);
    }

    public void bitmap(BitmapLoadCallBack callBack) {
        this.callBack = callBack;
        Davinci.get().loadImage(this);
    }


    public ILoader getLoader() {
        return loader;
    }

    public LoadOptions loader(ILoader loader) {
        this.loader = loader;
        return this;
    }

    public LoadOptions placeholder(int placeholderResId) {
        this.placeholderResId = placeholderResId;
        return this;
    }

    public LoadOptions placeholder(Drawable placeholder) {
        this.placeholder = placeholder;
        return this;
    }


    public LoadOptions error(int errorResId) {
        this.errorResId = errorResId;
        return this;
    }

    public LoadOptions centerCrop() {
        isCenterCrop = true;
        return this;
    }


    public LoadOptions centerInside() {
        isCenterInside = true;
        return this;
    }


    public LoadOptions skipLocalCache(boolean skipLocalCache) {
        this.skipLocalCache = skipLocalCache;
        return this;
    }


    public LoadOptions skipNetCache(boolean skipNetCache) {
        this.skipNetCache = skipNetCache;
        return this;
    }

    public LoadOptions config(Bitmap.Config config) {
        this.config = config;
        return this;
    }


    public LoadOptions resize(int targetWidth, int targetHeight) {
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        return this;
    }


    public LoadOptions corner(int radius) {
        this.radius = radius;
        return this;
    }


    public LoadOptions rotate(float degrees) {
        this.degrees = degrees;
        return this;
    }

    public String getPath() {
        return path;
    }

    public File getFile() {
        return file;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public Uri getUri() {
        return uri;
    }

    public int getTargetWidth() {
        return targetWidth;
    }

    public int getTargetHeight() {
        return targetHeight;
    }

    public int getPlaceholderResId() {
        return placeholderResId;
    }

    public int getErrorResId() {
        return errorResId;
    }

    public boolean isCenterCrop() {
        return isCenterCrop;
    }

    public boolean isCenterInside() {
        return isCenterInside;
    }

    public boolean isSkipLocalCache() {
        return skipLocalCache;
    }

    public boolean isSkipNetCache() {
        return skipNetCache;
    }

    public Bitmap.Config getConfig() {
        return config;
    }

    public int getRadius() {
        return radius;
    }

    public float getDegrees() {
        return degrees;
    }

    public Drawable getPlaceholder() {
        return placeholder;
    }

    public View getTargetView() {
        return targetView;
    }

    public BitmapLoadCallBack getCallBack() {
        return callBack;
    }
}
