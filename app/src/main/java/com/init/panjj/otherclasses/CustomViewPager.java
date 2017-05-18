package com.init.panjj.otherclasses;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import java.lang.reflect.Field;

public class CustomViewPager extends ViewPager {
    private SwipeDirection direction;
    private float initialXValue;
    private ScrollerCustomDuration mScroller;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mScroller = null;
        this.direction = SwipeDirection.all;
        postInitViewPager();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (IsSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (IsSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    private void postInitViewPager() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewpager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);
            this.mScroller = new ScrollerCustomDuration(getContext(), (Interpolator) interpolator.get(null));
            scroller.set(this, this.mScroller);
        } catch (Exception e) {
        }
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if (this.direction == SwipeDirection.all) {
            return true;
        }
        if (this.direction == SwipeDirection.none) {
            return false;
        }
        if (event.getAction() == 0) {
            this.initialXValue = event.getX();
            return true;
        } else if (event.getAction() != 2) {
            return true;
        } else {
            try {
                float diffX = event.getX() - this.initialXValue;
                if (diffX > 0.0f && this.direction == SwipeDirection.right) {
                    return false;
                }
                if (diffX >= 0.0f || this.direction != SwipeDirection.left) {
                    return true;
                }
                return false;
            } catch (Exception exception) {
                exception.printStackTrace();
                return true;
            }
        }
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.direction = direction;
    }

    public void setScrollDurationFactor(double scrollFactor) {
        this.mScroller.setScrollDurationFactor(scrollFactor);
    }
}
