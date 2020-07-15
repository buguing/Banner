package com.wellee.libbanner.scroller;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 修改ViewPager的滚动速率
 */
public class BannerScroller extends Scroller {

    private int mScrollDuration = 850;

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    public void setScrollDuration(int mScrollDuration) {
        this.mScrollDuration = mScrollDuration;
    }
}
