package com.wellee.library_autorollpic;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by liwei on 2017/4/25.
 */

public class AutoRollPic<T> extends FrameLayout implements ViewPager.OnPageChangeListener,
        View.OnTouchListener, GestureDetector.OnGestureListener {

    private Context mContext;
    /**
     * 滚动间隔时间：单位ms，默认2000ms
     */
    private int interval;
    /**
     * 隐藏标题与否：默认不隐藏
     */
    private boolean hideTitle;
    /**
     * 指示器区域背景高度
     */
    private int mIndicatorBgHeight;
    /**
     * 左间距 即标题左间距 (hideTitle为true即无效)
     */
    private int mPaddingLeft;
    /**
     * 右间距 即指示点容器右间距 (hideTitle为true即无效)
     */
    private int mPaddingRight;
    /**
     * 指示器背景颜色 (hideTitle为true即无效)
     */
    private int mIndicatorBgColor;
    /**
     * 标题文字大小 (hideTitle为true即无效)
     */
    private int mTitleTextSize;
    /**
     * 标题文字颜色 (hideTitle为true即无效)
     */
    private int mTitleTextColor;
    /**
     * 指示点大小
     */
    private int mDotSize;
    /**
     * 指示点间距
     */
    private int mDotSpace;
    /**
     * 指示点背景颜色选择器
     */
    private int mDotSelector;
    /**
     * 默认图
     */
    private int mDefaultPicture;
    /**
     * ViewPager
     */
    private ViewPager mViewPager;
    /**
     * 手势探测器
     */
    private GestureDetector mGestureDetector;
    /**
     * ViewPager指示器，默认在底部 由标题和点组成
     */
    private RelativeLayout mIndicator;
    /**
     * 标题
     */
    private TextView mTitle;
    /**
     * 点的容器
     */
    private LinearLayout mDotContainer;
    /**
     * ImageView集合
     */
    private List<ImageView> mImageViews;
    /**
     * 标题集合
     */
    private List<String> mTitles;
    /**
     * 相当于RxJava1.x里的Subscription
     */
    private Disposable subscribe;
    /**
     * ViewPager的当前位置 经转换 见onPageSelected方法
     */
    private int mCurrentPosition = 0;
    /**
     * mCurrentPosition的上一个位置
     */
    private int mPrePosition = 1;

    public AutoRollPic(@NonNull Context context) {
        this(context, null);
    }

    public AutoRollPic(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoRollPic(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoRollPic, defStyleAttr, 0);
        interval = typedArray.getInteger(R.styleable.AutoRollPic_interval, 2000);
        hideTitle = typedArray.getBoolean(R.styleable.AutoRollPic_hideTitle, false);
        mPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.AutoRollPic_indicatorPaddingLeft, dip2px(mContext, 20));
        mPaddingRight = typedArray.getDimensionPixelSize(R.styleable.AutoRollPic_indicatorPaddingRight, dip2px(mContext, 20));
        mIndicatorBgHeight = typedArray.getDimensionPixelSize(R.styleable.AutoRollPic_indicatorAreaHeight, dip2px(mContext, 50));
        mIndicatorBgColor = typedArray.getColor(R.styleable.AutoRollPic_indicatorBackground, 0x60000000);
        mTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.AutoRollPic_textSize, getResources().getDimensionPixelSize(R.dimen.textSize_18));
        mTitleTextColor = typedArray.getColor(R.styleable.AutoRollPic_textColor, 0xffffff);
        mDotSelector = typedArray.getResourceId(R.styleable.AutoRollPic_dotSelector, R.drawable.arp_dot_bg_selector);
        mDotSize = typedArray.getDimensionPixelSize(R.styleable.AutoRollPic_dotSize, dip2px(mContext, 10));
        mDotSpace = typedArray.getDimensionPixelSize(R.styleable.AutoRollPic_dotSpace, dip2px(mContext, 10));
        mDefaultPicture = typedArray.getResourceId(R.styleable.AutoRollPic_defaultPicture, R.drawable.default_picture);
        typedArray.recycle();

        initView();
        // 获得viewpager的触摸事件
        mViewPager.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(mContext, this);
    }

    private void initView() {
        // 加载布局填充自己
        View.inflate(mContext, R.layout.autorollpic, this);
        mViewPager = (ViewPager) findViewById(R.id.arp_viewpager);
        mIndicator = (RelativeLayout) findViewById(R.id.arp_indicator);
        mTitle = (TextView) findViewById(R.id.arp_text);
        mDotContainer = (LinearLayout) findViewById(R.id.arp_dot_container);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mDotContainer.getLayoutParams();
        if (!hideTitle) { // 不隐藏标题
            // 指示点居右 设置指示器区域背景色
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mIndicator.setBackgroundColor(mIndicatorBgColor);
            // 设置指示器区域高 左右间距
            LayoutParams lp1 = (LayoutParams) mIndicator.getLayoutParams();
            lp1.height = mIndicatorBgHeight;
            mIndicator.setLayoutParams(lp1);
            mIndicator.setPadding(mPaddingLeft, 0, mPaddingRight, 0);
            // 设置标题文字大小颜色
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
            mTitle.setTextColor(mTitleTextColor);
        } else { // 隐藏标题 指示点居中
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        }
        mDotContainer.setLayoutParams(lp);
    }

    /**
     * 开始滚动
     */
    public void startAutoRoll() {
        if (interval <= 0) {
            return;
        }
        // 这里防止重复订阅 很有必要
        stopAutoRoll();
        subscribe = Observable.interval(interval, interval, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }, e -> {
                    Log.e("error: ", e.getMessage());
                });
    }

    /**
     * 停止滚动
     */
    public void stopAutoRoll() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
    }

    /**
     * 得到当前图片位置
     *
     * @return 返回的是现实中看到的图片position
     */
    public int getCurrentPosition() {
        return mCurrentPosition == 0 ? 0 : mCurrentPosition - 1;
    }

    /**
     *
     * @param images
     * @param titles
     */
    public void setData(T[] images, String[] titles) {
        if (images == null || images.length == 0) {
            throw new NullPointerException("images can not be empty!");
        }
        setData(Arrays.asList(images), titles == null ? null : Arrays.asList(titles));
    }

    /**
     * 外部传入数据
     * @param images 可传入url或者resId
     * @param titles
     */
    public void setData(List<T> images, List<String> titles) {
        if (images == null || images.size() == 0) {
            throw new NullPointerException("images can not be empty!");
        }
        if (titles == null || titles.size() == 0) {
            hideTitle = true;
        } else {
            this.mTitles = titles;
        }
        setViewPagerAdapterInfo(images);
        initData();
    }

    private void setViewPagerAdapterInfo(List<T> images) {
        mImageViews = new ArrayList<>();
        // 防止指示点多加 很有必要
        mDotContainer.removeAllViews();
        if (images.size() == 1) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            setImgWithGlide(imageView, images.get(0));
            addIndicatorDots();
            mImageViews.add(imageView);
        } else { // 多添加两张图片实现"无限轮播"
            for (int i = 0; i < images.size() + 2; i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                if (i == 0) {
                    setImgWithGlide(imageView, images.get(images.size() - 1));
                } else if (i == images.size() + 1) {
                    setImgWithGlide(imageView, images.get(0));
                } else {
                    setImgWithGlide(imageView, images.get(i - 1));
                    addIndicatorDots();
                }
                mImageViews.add(imageView);
            }
        }
    }

    /**
     * Glide加载图片
     * @param imageView
     * @param urlOrResId
     */
    private void setImgWithGlide(ImageView imageView, T urlOrResId) {
        if (urlOrResId instanceof String || urlOrResId instanceof Integer) {
            Glide.with(mContext)
                    .load(urlOrResId)
                    .asBitmap()
                    .placeholder(mDefaultPicture)
                    .into(imageView);
        } else {
            throw new IllegalArgumentException("T must be a String or an Integer!");
        }
    }

    /**
     * 添加指示点
     */
    private void addIndicatorDots() {
        View dotView = new View(mContext);
        // 设置宽高
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mDotSize, mDotSize);
        lp.setMargins(mDotSpace, 0, 0, 0);
        dotView.setLayoutParams(lp);
        // 指定背景是选择器
        dotView.setBackgroundResource(mDotSelector);
        mDotContainer.addView(dotView);
    }

    /**
     * 初始化adapter和默认显示
     */
    private void initData() {
        // 设置adapter
        mViewPager.removeAllViews();
        mViewPager.setAdapter(new AutoRollAdapter(mImageViews));
        // 默认从传入的第一张图片开始滚动
        if (mImageViews != null && mImageViews.size() > 3) {
            mViewPager.setCurrentItem(1);
            mViewPager.addOnPageChangeListener(this);
            startAutoRoll();
        } else {
            mDotContainer.setVisibility(GONE);
        }
        // 设置指示器默认高亮位置
        mDotContainer.getChildAt(0).setSelected(true);
        if (!hideTitle) {
            mTitle.setText(mTitles.get(0));
        }
        // 解决标题过长
        if (!hideTitle) {
            int dotContainerWidth = (mDotSize + mDotSpace) * mTitles.size();
            mTitle.setWidth(getScreenWidth(mContext) - mPaddingLeft - mPaddingRight - dotContainerWidth);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        if (mCurrentPosition == 0) {
            mCurrentPosition = mImageViews.size() - 2;
        } else if (mCurrentPosition == mImageViews.size() - 1) {
            mCurrentPosition = 1;
        }
        if (mPrePosition != mCurrentPosition) { // 只有位置发生变化才操作
            if (!hideTitle) {
                mTitle.setText(mTitles.get(mCurrentPosition - 1));
            }
            mDotContainer.getChildAt(mCurrentPosition - 1).setSelected(true);
            mDotContainer.getChildAt(mPrePosition - 1).setSelected(false);
            mPrePosition = mCurrentPosition;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 静止的时候偷偷切换位置
        if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (mCurrentPosition == mImageViews.size() - 2 || mCurrentPosition == 1) {
                // 参数2:smoothScroll false平滑的滚动,顺滑的滚动
                mViewPager.setCurrentItem(mCurrentPosition, false);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 分析触摸事件
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下停止滚动
                stopAutoRoll();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // 抬起恢复滚动
                startAutoRoll();
                break;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // 通知自己被点击了
        AutoRollPic.this.performClick();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int sWidth = display.getWidth();
        return sWidth;
    }
}
