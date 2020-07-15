package com.wellee.libbanner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.wellee.libbanner.R;
import com.wellee.libbanner.adapter.BannerAdapter;
import com.wellee.libbanner.adapter.BannerPagerAdapter;
import com.wellee.libbanner.scroller.BannerScroller;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * BannerViewPager 轮播的ViewPager
 */
public class BannerViewPager extends ViewPager {

    /**
     * 间隔时间 默认2000 单位ms
     */
    private int mInterval;
    /**
     * 滚动速率 默认不反射修改
     */
    private int mScrollDuration;
    private Disposable mDisposable;
    private BannerAdapter mAdapter;


    public BannerViewPager(@NonNull Context context) {
        this(context, null);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        setScrollDuration();
    }

    public void setAttrs(AttributeSet attrs) {
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BannerViewPager);
        mInterval = array.getInt(R.styleable.BannerViewPager_interval, 2000);
        mScrollDuration = array.getInt(R.styleable.BannerViewPager_scrollDuration, 0);
        array.recycle();
    }

    private void setScrollDuration() {
        if (mScrollDuration != 0) {
            try {
                inflect2SetDuration();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 反射修改ViewPager滚动速率
     *
     * @throws Exception 异常
     */
    private void inflect2SetDuration() throws Exception {
        Field mScrollerField = ViewPager.class.getDeclaredField("mScroller");
        mScrollerField.setAccessible(true);
        BannerScroller scroller = new BannerScroller(getContext(), new AccelerateInterpolator());
        if (mScrollDuration >= mInterval) {
            mScrollDuration = (int) (0.85 * mScrollDuration);
        }
        scroller.setScrollDuration(mScrollDuration);
        mScrollerField.set(this, scroller);
    }

    public void setAdapter(@NonNull BannerAdapter adapter) {
        if (mAdapter != null) {
            throw new RuntimeException("must not be setAdapter twice");
        }
        mAdapter = adapter;
        setAdapter(new BannerPagerAdapter(mAdapter));

    }

    /**
     * 开始滚动
     */
    public void startAutoRoll() {
        if (mInterval <= 0) {
            return;
        }
        // 这里防止重复订阅 很有必要
        stopAutoRoll();
        mDisposable = Observable.interval(mInterval, mInterval, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        aLong -> setCurrentItem(getCurrentItem() + 1),
                        e -> Log.e("BannerViewPager", Objects.requireNonNull(e.getMessage()))
                );
    }

    /**
     * 停止滚动
     */
    public void stopAutoRoll() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
