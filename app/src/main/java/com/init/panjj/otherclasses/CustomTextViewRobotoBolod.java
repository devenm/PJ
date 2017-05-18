package com.init.panjj.otherclasses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewRobotoBolod extends TextView {
    public CustomTextViewRobotoBolod(Context context) {
        super(context);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto_bold.ttf"));
    }

    public CustomTextViewRobotoBolod(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto_bold.ttf"));
    }

    public CustomTextViewRobotoBolod(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto_bold.ttf"));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
