package com.xiaoyu.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.xiaoyu.davinci.BitmapLoadCallBack;
import com.xiaoyu.davinci.ILoader;
import com.xiaoyu.davinci.LoadOptions;

import java.io.File;
import java.net.URL;

public class PicassoLoader implements ILoader {

    private static LruCache sLruCache = new LruCache(App.gApp);

    private volatile static Picasso picassoInstance;

    private static Picasso GPicasso() {
        if (picassoInstance == null) {
            synchronized (PicassoLoader.class) {
                if (picassoInstance == null) {
                    picassoInstance = new Picasso.Builder(App.gApp).memoryCache(sLruCache).build();
                }
            }
        }
        return picassoInstance;
    }

    @Override
    public void load(final LoadOptions loadOptions) {
        RequestCreator requestCreator = null;
        if (!TextUtils.isEmpty(loadOptions.getPath())) {
            requestCreator = GPicasso().load(loadOptions.getPath());
        } else if (loadOptions.getFile() != null) {
            requestCreator = GPicasso().load(loadOptions.getFile());
        } else if (loadOptions.getUri() != null) {
            requestCreator = GPicasso().load(loadOptions.getUri());
        } else if (loadOptions.getDrawableResId() != 0) {
            requestCreator = GPicasso().load(loadOptions.getDrawableResId());
        }

        if (requestCreator == null)
            throw new NullPointerException("requestCreator must not be null");

        if (loadOptions.getTargetWidth() > 0 && loadOptions.getTargetHeight() > 0) {
            requestCreator.resize(loadOptions.getTargetWidth(), loadOptions.getTargetWidth());
        }

        if (loadOptions.isCenterInside()) {
            requestCreator.centerInside();
        } else if (loadOptions.isCenterCrop()) {
            requestCreator.centerCrop();
        }

        if (loadOptions.getConfig() != null) {
            requestCreator.config(loadOptions.getConfig());
        }
        if (loadOptions.getErrorResId() != 0) {
            requestCreator.error(loadOptions.getErrorResId());
        }
        if (loadOptions.getPlaceholderResId() != 0) {
            requestCreator.placeholder(loadOptions.getPlaceholderResId());
        }
        if (loadOptions.getRadius() != 0) {
            if (loadOptions.getTargetView() instanceof ImageView) {
                requestCreator.transform(new PicassoTransformation(loadOptions.getRadius(), (ImageView) loadOptions.getTargetView()));
            }
        }
        if (loadOptions.isSkipLocalCache()) {
            requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
        }
        if (loadOptions.isSkipNetCache()) {
            requestCreator.networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE);
        }
        if (loadOptions.getDegrees() != 0) {
            requestCreator.rotate(loadOptions.getDegrees());
        }

        if (loadOptions.getTargetView() instanceof ImageView) {
            requestCreator.into(((ImageView) loadOptions.getTargetView()));
        } else if (loadOptions.getCallBack() != null) {
            requestCreator.into(new PicassoTarget(loadOptions.getCallBack()));
        }


    }

    @Override
    public void clearDiskCache() {
        //根据自己的APP实现
    }

    @Override
    public void clearMemoryCache() {
        sLruCache.clear();
    }

    class PicassoTarget implements Target {
        private BitmapLoadCallBack callBack;

        public PicassoTarget(BitmapLoadCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            callBack.onSuccess(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            callBack.onError(e);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            callBack.onPrepareLoad(placeHolderDrawable);
        }
    }

    class PicassoTransformation implements Transformation {

        private int mRadius;

        private int vWidth;   //显示图片的ImageView的宽度
        private int vHeight;  //显示图片的ImageView的高度

        private ImageView.ScaleType mScaleType;

        private ImageView imageView;

        public PicassoTransformation(int mRadius, ImageView imageView) {
            this.imageView = imageView;
            this.mRadius = mRadius;
            this.vWidth = imageView.getLayoutParams().width;
            this.vHeight = imageView.getLayoutParams().height;
            ImageView.ScaleType scaleType = imageView.getScaleType();
            if (scaleType == null) {
                mScaleType = ImageView.ScaleType.CENTER_CROP;
            } else {
                mScaleType = scaleType;
            }
        }

        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap bitmap = configureBounds(source);
            return bitmap;
        }

        //这里拿到的Bitmap是根据View后并且加上圆角之后的
        private Bitmap configureBounds(Bitmap bitmap) {
            final int bitmapWidth = bitmap.getWidth();
            final int bitmapHeight = bitmap.getHeight();
            float x = 0;
            float y = 0;
            float scaleX = 1;
            float scaleY = 1;
            Matrix matrix = new Matrix();
            Bitmap newbmp;

            if (ImageView.ScaleType.MATRIX == mScaleType) {
                matrix = imageView.getImageMatrix();
            } else if (ImageView.ScaleType.CENTER == mScaleType) {
                //大则裁剪
                if (bitmapWidth > vWidth) {
                    x = Math.round((bitmapWidth - vWidth) * 0.5f);
                }
                if (bitmapHeight > vHeight) {
                    y = Math.round((bitmapHeight - vHeight) * 0.5f);
                }

            } else if (ImageView.ScaleType.CENTER_CROP == mScaleType) {
                if (bitmapWidth * vHeight > vWidth * bitmapHeight) {
                    scaleX = vHeight / (float) bitmapHeight;
                    scaleY = scaleX;
                } else {
                    scaleX = vWidth / (float) bitmapWidth;
                    scaleY = scaleX;
                }
                x = Math.round((bitmapWidth - vWidth / scaleX) / 2);
                y = Math.round((bitmapHeight - vHeight / scaleY) / 2);
                matrix.postScale(scaleX, scaleY);

            } else if (ImageView.ScaleType.CENTER_INSIDE == mScaleType) {
                if (bitmapWidth <= vWidth && bitmapHeight <= vHeight) {
                    scaleX = 1.0f;
                    scaleY = scaleX;
                } else {
                    scaleX = Math.min(vWidth / (float) bitmapWidth, vHeight / (float) bitmapHeight);
                    scaleY = scaleX;
                }
                matrix.postScale(scaleX, scaleY);

            } else {

                RectF mTempSrc = new RectF(0, 0, bitmapWidth, bitmapHeight);
                RectF mTempDst = new RectF(0, 0, vWidth, vHeight);
                matrix.setRectToRect(mTempSrc, mTempDst, scaleTypeToScaleToFit(mScaleType));

            }

            try {
                newbmp = Bitmap.createBitmap(bitmap, (int) x, (int) y, (int) (bitmapWidth - 2 * x), (int) (bitmapHeight - 2 * y), matrix, true);
            } catch (Exception e) {
                e.printStackTrace();
                newbmp = bitmap;
            }

            //
            Bitmap canvasBitmap = Bitmap.createBitmap(newbmp.getWidth(), newbmp.getHeight(), Bitmap.Config.ARGB_8888);
            if (canvasBitmap == null) {
                canvasBitmap = Bitmap.createBitmap(newbmp.getWidth(), newbmp.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(canvasBitmap);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(newbmp, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

            paint.setAntiAlias(true);
            canvas.drawRoundRect(new RectF(0, 0, newbmp.getWidth(), newbmp.getHeight()), mRadius, mRadius, paint);
            return canvasBitmap;
        }

        private Matrix.ScaleToFit scaleTypeToScaleToFit(ImageView.ScaleType st) {
            switch (st) {
                case FIT_XY:
                    return Matrix.ScaleToFit.FILL;
                case FIT_END:
                    return Matrix.ScaleToFit.END;
                case FIT_START:
                    return Matrix.ScaleToFit.START;
                case FIT_CENTER:
                    return Matrix.ScaleToFit.CENTER;
                default:
                    return Matrix.ScaleToFit.CENTER;
            }
        }

        @Override
        public String key() {
            return "corner()";
        }
    }


}
