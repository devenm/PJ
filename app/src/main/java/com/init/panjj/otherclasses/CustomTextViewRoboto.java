package com.init.panjj.otherclasses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewRoboto extends TextView {
    public CustomTextViewRoboto(Context context) {
        super(context);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto_regular.ttf"));
    }

    public CustomTextViewRoboto(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto_regular.ttf"));
    }

    public CustomTextViewRoboto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto_regular.ttf"));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
