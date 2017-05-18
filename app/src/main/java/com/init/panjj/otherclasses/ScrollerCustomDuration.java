package com.init.panjj.otherclasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ScrollerCustomDuration extends Scroller {
    private double mScrollFactor;

    public ScrollerCustomDuration(Context context) {
        super(context);
        this.mScrollFactor = 1.0d;
    }

    public ScrollerCustomDuration(Context context, Interpolator interpolator) {
        super(context, interpolator);
        this.mScrollFactor = 1.0d;
    }

    @SuppressLint({"NewApi"})
    public ScrollerCustomDuration(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
        this.mScrollFactor = 1.0d;
    }

    public void setScrollDurationFactor(double scrollFactor) {
        this.mScrollFactor = scrollFactor;
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int) (((double) duration) * this.mScrollFactor));
    }
}
