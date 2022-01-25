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
     * 触摸暂停
     */
    private boolean mCanTouchToPause;
    private int mBottomHeight;
    private int mBottomPadding;
    private int mBottomBgColor;
    private int mIndicatorLocation;
    private int mDotDrawableSelector;
    private int mDotSize;
    private int mDotMargin;
    private int mTitleLocation;
    private int mTitleTextSize;
    private int mTitleTextColor;

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
        mHideBottom = array.getBoolean(R.styleable.BannerView_hideBottom, false);
        mHideIndicator = array.getBoolean(R.styleable.BannerView_hideIndicator, false);
        mCanTouchToPause = array.getBoolean(R.styleable.BannerView_canTouchToPause, true);
        mBottomHeight = array.getDimensionPixelSize(R.styleable.BannerView_bottomHeight, 0);
        mBottomPadding = array.getDimensionPixelSize(R.styleable.BannerView_bottomPadding, Utils.dp2px(getContext(), 10));
        mBottomBgColor = array.getColor(R.styleable.BannerView_bottomBackgroundColor, ContextCompat.getColor(getContext(), R.color.black));
        mIndicatorLocation = array.getInt(R.styleable.BannerView_indicatorLocation, 1);
        mDotDrawableSelector = array.getResourceId(R.styleable.BannerView_dotDrawableSelector, R.drawable.banner_dot_bg_selector);
        mDotSize = array.getDimensionPixelSize(R.styleable.BannerView_dotSize, Utils.dp2px(getContext(), 10));
        mDotMargin = array.getDimensionPixelSize(R.styleable.BannerView_dotMargin, Utils.dp2px(getContext(), 5));
        mTitleLocation = array.getInt(R.styleable.BannerView_titleLocation, -1);
        mTitleTextSize = array.getDimensionPixelSize(R.styleable.BannerView_titleTextSize, Utils.sp2px(getContext(), 18));
        mTitleTextColor = array.getColor(R.styleable.BannerView_titleTextColor, ContextCompat.getColor(getContext(), R.color.white));
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
        if (this.mAdapter != null) {
            return;
        }
        this.mAdapter = adapter;
        mBannerVp.setAdapter(adapter);
        // 初始化点
        initDotIndicator();
        // 初始化标题文字
        initDesc();
        // 事件监听
        handleListener();
    }

    private void handleListener() {
        mBannerVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                onSelectedItem(position);
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
        mBannerVp.startAutoRoll();
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
        if (!mHideIndicator) {
            mBannerDotContainer.removeAllViews();
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize, mDotSize);
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
        int realPosition = position % mAdapter.getItemCount();
        Log.e("onSelectedItem", "position = " + position + ", realPosition = " + realPosition);
        if (!mHideIndicator) {
            mBannerDotContainer.getChildAt(mPrePosition).setSelected(false);
            mBannerDotContainer.getChildAt(realPosition).setSelected(true);
        }
        mBannerTvDesc.setText(mAdapter.getItemDesc(realPosition));
        this.mPrePosition = realPosition;
    }

}
