package com.wellee.libbanner.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * ViewPagerAdapter 实现无限轮播
 */
public class BannerPagerAdapter extends PagerAdapter {

    private BannerAdapter mAdapter;
    /**
     * 缓存View集合
     */
    private Set<View> mConvertViews;

    public BannerPagerAdapter(BannerAdapter adapter) {
        this.mAdapter = adapter;
        mConvertViews = new HashSet<>();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mAdapter.getItemView(position % mAdapter.getItemCount(), getConvertView());
        container.addView(itemView);
        return itemView;
    }

    private View getConvertView() {
        Log.e("getConvertView", "mConvertViews size = " + mConvertViews.size());

        for (View convertView : mConvertViews) {
            if (convertView.getParent() == null) {
                return convertView;
            }
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        mConvertViews.add((View) object);
    }

}
