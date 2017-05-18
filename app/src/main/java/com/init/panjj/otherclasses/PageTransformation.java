package com.init.panjj.otherclasses;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import com.wang.avi.indicators.LineScalePartyIndicator;

public class PageTransformation implements PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1.0f) {
            view.setAlpha(0.0f);
        } else if (position <= 0.0f) {
            view.setAlpha(LineScalePartyIndicator.SCALE);
            view.setTranslationX(0.0f);
            view.setScaleX(LineScalePartyIndicator.SCALE);
            view.setScaleY(LineScalePartyIndicator.SCALE);
        } else if (position <= LineScalePartyIndicator.SCALE) {
            view.setAlpha(LineScalePartyIndicator.SCALE - position);
            view.setTranslationX(((float) pageWidth) * (-position));
            float scaleFactor = MIN_SCALE + (0.25f * (LineScalePartyIndicator.SCALE - Math.abs(position)));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else {
            view.setAlpha(0.0f);
        }
    }
}
