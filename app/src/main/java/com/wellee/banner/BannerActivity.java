package com.wellee.banner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wellee.libbanner.adapter.BannerAdapter;
import com.wellee.libbanner.view.BannerView;

import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity {

    private BannerView mBannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        initView();
        initData();
    }

    private void initView() {
        mBannerView = findViewById(R.id.bv);
    }

    private void initData() {
        final List<String> images = new ArrayList<>();
        images.add("https://img0.baidu.com/it/u=3234506509,677321585&fm=26&fmt=auto&gp=0.jpg");
//        images.add("https://img2.baidu.com/it/u=2689069861,1551374965&fm=26&fmt=auto&gp=0.jpg");
//        images.add("https://img2.baidu.com/it/u=3008495258,294790728&fm=26&fmt=auto&gp=0.jpg");

        final List<String> descriptions = new ArrayList<>();
        descriptions.add("蓝天绿水");
//        descriptions.add("一叶扁舟");
//        descriptions.add("浩瀚宇宙");

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
                        .apply(new RequestOptions().placeholder(R.drawable.default_picture))
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

    public void toSecond(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mBannerView.startAutoRoll();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mBannerView.stopAutoRoll();
//    }

}
