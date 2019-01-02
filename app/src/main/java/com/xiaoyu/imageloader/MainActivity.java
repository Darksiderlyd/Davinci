package com.xiaoyu.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xiaoyu.davinci.BitmapLoadCallBack;
import com.xiaoyu.davinci.Davinci;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView viewById = findViewById(R.id.img1);
        final FrameLayout view = findViewById(R.id.v1);
        Davinci.get().load(R.mipmap.ic_launcher).into(viewById);

        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Davinci.get().load(R.mipmap.ic_launcher).bitmap(new BitmapLoadCallBack() {
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }

                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        Drawable drawable = new BitmapDrawable(App.gApp.getResources(),bitmap);
                        view.setBackground(drawable);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });
//        Davinci.get().load("https://i.loli.net/2019/01/02/5c2c300637b9b.png").into(viewById);
    }


}
