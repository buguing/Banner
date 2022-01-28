package com.wellee.libbanner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.wellee.libbanner.R;
import com.wellee.libbanner.adapter.BannerAdapter;
import com.wellee.libbanner.adapter.BannerPagerAdapter;
import com.wellee.libbanner.scroller.BannerScroller;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

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
    private BannerAdapter mAdapter;
    private Timer mTimer;
    private Handler mHandler;

    private boolean isCanScroll;


    public BannerViewPager(@NonNull Context context) {
        this(context, null);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        setScrollDuration();
        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void setAttrs(AttributeSet attrs) {
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BannerViewPager);
        mInterval = array.getInt(R.styleable.BannerViewPager_bv_interval, 2000);
        mScrollDuration = array.getInt(R.styleable.BannerViewPager_bv_scrollDuration, 0);
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

        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(() -> setCurrentItem(getCurrentItem() + 1));
            }
        }, mInterval, mInterval);
    }

    /**
     * 停止滚动
     */
    public void stopAutoRoll() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }
}
