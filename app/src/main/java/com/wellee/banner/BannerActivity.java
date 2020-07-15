package com.wellee.banner;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wellee.libbanner.adapter.BannerAdapter;
import com.wellee.libbanner.view.BannerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class BannerActivity extends AppCompatActivity {

    private Disposable mDisposable;
    private BannerView mBannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        RxPermissions rxPermissions = new RxPermissions(this);
        mDisposable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        initView();
                        initData();
                    }
                });
    }

    private void initView() {
        mBannerView = findViewById(R.id.bv);
    }

    private void initData() {
        final List<String> images = new ArrayList<>();
        images.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3736505945,2891641029&fm=26&gp=0.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594716584559&di=1a345db11cda02cc38f4f7e36f83fdb3&imgtype=0&src=http%3A%2F%2Fimg1.juimg.com%2F171022%2F330794-1G0221UZ753.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594716620842&di=cd5578c283f0e380bc9824a31f448c2b&imgtype=0&src=http%3A%2F%2Fimg.taopic.com%2Fuploads%2Fallimg%2F140624%2F240455-14062406361053.jpg");

        final List<String> descriptions = new ArrayList<>();
        descriptions.add("蓝天绿水");
        descriptions.add("一叶扁舟");
        descriptions.add("浩瀚宇宙");

        mBannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getItemView(int position, View convertView) {
                ImageView imageView;
                if (convertView == null) {
                    imageView = new ImageView(BannerActivity.this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                } else {
                    imageView = (ImageView) convertView;
                    Log.e("getItemView", "复用了" + imageView);
                }
                Glide.with(BannerActivity.this)
                        .load(images.get(position))
                        .apply(new RequestOptions().placeholder(com.wellee.libbanner.R.drawable.default_picture))
                        .into(imageView);
                return imageView;
            }

            @Override
            public int getItemCount() {
                return images.size();
            }

            @Override
            public CharSequence getItemDesc(int position) {
                return descriptions.get(position);
            }
        });

        mBannerView.setOnClickListener(v -> {
            int currentPosition = mBannerView.getCurrentPosition();
            Toast.makeText(this, "currentPosition = " + currentPosition, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBannerView.startAutoRoll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBannerView.stopAutoRoll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
