package com.wellee.libbanner.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.viewpager.widget.ViewPager;

import com.wellee.libbanner.R;
import com.wellee.libbanner.adapter.BannerAdapter;
import com.wellee.libbanner.utils.Utils;

/**
 * BannerView集成BannerViewPager、指示器、描述文字
 */
public class BannerView extends RelativeLayout implements LifecycleObserver {

    private static final String FRAGMENT_TAG = "lifecycle_fragment";
    private BannerViewPager mBannerVp;
    private TextView mBannerTvDesc;
    private LinearLayout mBannerDotContainer;

    private BannerAdapter mAdapter;

    /**
     * 上个指示点的位置
     */
    private int mPrePosition = 0;

    private GestureDetector mGestureDetector;

    private boolean mHideBottom;
    private boolean mHideIndicator;
    /**
     * 是否可以自动轮播 默认true
     */
    private boolean mCanAutoScroll;
    /**
     * 一条数据是否滚动
     */
    private boolean mOneDataScroll = true;
    /**
     * 触摸暂停
     */
    private boolean mCanTouchToPause;
    private int mBottomHeight;
    private int mBottomPadding;
    private int mBottomBgColor;
    private int mIndicatorLocation;
    private int mDotDrawableSelector;
    private int mDotWidth, mDotHeight;
    private int mDotMargin;
    private int mTitleLocation;
    private int mTitleTextSize;
    private int mTitleTextColor;
    private boolean canScroll;
    private int mCurrentPosition;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bindLifecycleFragment(context);
        inflate(context, R.layout.layout_banner, this);
        initAttrs(attrs);
        initView(attrs);
    }

    private void bindLifecycleFragment(Context context) {
        if (context instanceof Activity) {
            FragmentManager fm = ((Activity) context).getFragmentManager();
            LifecycleFragment current = new LifecycleFragment();
            current.getLifecycle().addObserver(this);
            fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BannerView);
        mHideBottom = array.getBoolean(R.styleable.BannerView_bv_hideBottom, false);
        mHideIndicator = array.getBoolean(R.styleable.BannerView_bv_hideIndicator, false);
        mCanAutoScroll = array.getBoolean(R.styleable.BannerView_bv_canAutoScroll, true);
        mOneDataScroll = array.getBoolean(R.styleable.BannerView_bv_oneDataScroll, true);
        mCanTouchToPause = array.getBoolean(R.styleable.BannerView_bv_canTouchToPause, true);
        mBottomHeight = array.getDimensionPixelSize(R.styleable.BannerView_bv_bottomHeight, 0);
        mBottomPadding = array.getDimensionPixelSize(R.styleable.BannerView_bv_bottomPadding, Utils.dp2px(getContext(), 10));
        mBottomBgColor = array.getColor(R.styleable.BannerView_bv_bottomBackgroundColor, ContextCompat.getColor(getContext(), R.color.black));
        mIndicatorLocation = array.getInt(R.styleable.BannerView_bv_indicatorLocation, 1);
        mDotDrawableSelector = array.getResourceId(R.styleable.BannerView_bv_dotDrawableSelector, R.drawable.banner_dot_bg_selector);
        mDotWidth = array.getDimensionPixelSize(R.styleable.BannerView_bv_dotWidth, Utils.dp2px(getContext(), 10));
        mDotHeight = array.getDimensionPixelSize(R.styleable.BannerView_bv_dotHeight, Utils.dp2px(getContext(), 10));
        mDotMargin = array.getDimensionPixelSize(R.styleable.BannerView_bv_dotMargin, Utils.dp2px(getContext(), 5));
        mTitleLocation = array.getInt(R.styleable.BannerView_bv_titleLocation, -1);
        mTitleTextSize = array.getDimensionPixelSize(R.styleable.BannerView_bv_titleTextSize, Utils.sp2px(getContext(), 18));
        mTitleTextColor = array.getColor(R.styleable.BannerView_bv_titleTextColor, ContextCompat.getColor(getContext(), R.color.white));
        array.recycle();
    }

    private void initView(AttributeSet attrs) {
        mBannerVp = findViewById(R.id.banner_vp);
        mBannerVp.setAttrs(attrs);

        mBannerTvDesc = findViewById(R.id.banner_tv_desc);
        mBannerDotContainer = findViewById(R.id.banner_dot_container);

        // 底部显示或隐藏
        RelativeLayout rlBottom = findViewById(R.id.banner_rl_bottom);
        rlBottom.setVisibility(mHideBottom ? View.GONE : View.VISIBLE);
        // 设置底部背景
        if (!mHideBottom) {
            LayoutParams params = (LayoutParams) rlBottom.getLayoutParams();
            params.height = mBottomHeight == 0 ? Utils.dp2px(getContext(), 50) : mBottomHeight;
            rlBottom.setPadding(mBottomPadding, mBottomPadding, mBottomPadding, mBottomPadding);
            rlBottom.setLayoutParams(params);
            rlBottom.setBackgroundColor(mBottomBgColor);
        }
        // 点的位置
        mBannerDotContainer.setLayoutParams(getParams(mBannerDotContainer, mIndicatorLocation));
        // 标题位置
        mBannerTvDesc.setLayoutParams(getParams(mBannerTvDesc, mTitleLocation));
        mBannerTvDesc.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        mBannerTvDesc.setTextColor(mTitleTextColor);
    }

    private LayoutParams getParams(View view, int location) {
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        switch (location) {
            case -1:
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                break;
            case 0:
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                break;
            case 1:
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                break;
        }
        return params;
    }

    public void setAdapter(@NonNull BannerAdapter adapter) {
        this.mAdapter = adapter;
        mBannerVp.setAdapter(adapter);
        mBannerVp.setCurrentItem(getItemCount());
        // 初始化点
        initDotIndicator();
        // 初始化标题文字
        initDesc();
        // 事件监听
        handleListener();
        // 设置banner可否滚动
        if (!mOneDataScroll && getItemCount() < 2) {
            canScroll = false;
            stopAutoRoll();
        } else {
            canScroll = true;
        }
        mBannerVp.setCanScroll(canScroll);
        startAutoRoll();
    }

    /**
     * viewpager 自动滚动时间间隔
     *
     * @param interval 单位ms
     */
    public void setInterval(int interval) {
        if (mBannerVp != null) {
            mBannerVp.setInterval(interval);
        }
    }

    /**
     * viewpager滚动速率
     *
     * @param scrollDuration 单位ms
     */
    public void setScrollDuration(int scrollDuration) {
        if (mBannerVp != null) {
            mBannerVp.setScrollDuration(scrollDuration);
        }
    }

    private void handleListener() {
        mBannerVp.clearOnPageChangeListeners();
        mBannerVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                onSelectedItem(mCurrentPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 手动向前滑动 滑到1或0时 向后切换10个周期
                    if (getItemCount() > 1) {
                        if (mCurrentPosition == 1) {
                            mBannerVp.setCurrentItem(10 * getItemCount() + 1, false);
                        } else if (mCurrentPosition == 0) {
                            mBannerVp.setCurrentItem(10 * getItemCount(), false);
                        }
                    }
                }
            }
        });

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                // 通知自己被点击了
                BannerView.this.performClick();
                return false;
            }
        });

        mBannerVp.setOnTouchListener((v, event) -> {
            // 分析触摸事件
            mGestureDetector.onTouchEvent(event);
            if (!mCanTouchToPause) {
                return false;
            }
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
        });
    }

    /**
     * 停止滚动 Activity的onResume() 中开始
     * Fragment isVisibleToUser == true 开始
     */
    public void startAutoRoll() {
        if (mCanAutoScroll && canScroll) {
            mBannerVp.startAutoRoll();
        }
    }

    private int getItemCount() {
        if (mAdapter != null) {
            return mAdapter.getItemCount();
        }
        return 0;
    }

    /**
     * 停止滚动 Activity的onPause() 中停止
     * Fragment isVisibleToUser == false 停止
     */
    public void stopAutoRoll() {
        mBannerVp.stopAutoRoll();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        startAutoRoll();
        Log.d("BannerView onResume", "startAutoRoll");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        stopAutoRoll();
        Log.d("BannerView onPause", "stopAutoRoll");
    }

    /**
     * 获取当前点击的位置
     *
     * @return position
     */
    public int getCurrentPosition() {
        return mPrePosition;
    }

    /**
     * 动态添加指示器
     */
    private void initDotIndicator() {
        mPrePosition = 0;
        mBannerDotContainer.removeAllViews();
        boolean isAddIndicator = !mHideIndicator;
        if (!mOneDataScroll) {
            isAddIndicator = isAddIndicator && getItemCount() > 1;
        }
        if (isAddIndicator) {
            for (int i = 0; i < getItemCount(); i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotWidth, mDotHeight);
                params.leftMargin = params.rightMargin = mDotMargin;
                View dotView = new View(getContext());
                dotView.setLayoutParams(params);
                dotView.setSelected(i == 0);
                dotView.setBackgroundResource(mDotDrawableSelector);
                mBannerDotContainer.addView(dotView);
            }
        }
    }

    /**
     * 标题/描述文字
     */
    private void initDesc() {
        CharSequence desc = mAdapter.getItemDesc(0);
        mBannerTvDesc.setText(desc);
    }

    /**
     * 动态改变点的选中状态和文字
     *
     * @param position position
     */
    private void onSelectedItem(int position) {
        int realPosition = position % getItemCount();
        Log.e("onSelectedItem", "position = " + position + ", realPosition = " + realPosition);
        if (!mHideIndicator) {
            if (mBannerDotContainer.getChildAt(mPrePosition) != null) {
                mBannerDotContainer.getChildAt(mPrePosition).setSelected(false);
            }
            if (mBannerDotContainer.getChildAt(realPosition) != null) {
                mBannerDotContainer.getChildAt(realPosition).setSelected(true);
            }
        }
        mBannerTvDesc.setText(mAdapter.getItemDesc(realPosition));
        this.mPrePosition = realPosition;
    }

}
