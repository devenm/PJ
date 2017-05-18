package com.init.panjj.otherclasses;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewHelvetica extends TextView {
    public CustomTextViewHelvetica(Context context) {
        super(context);
        setFont(context);
    }

    private void setFont(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "HelveticaNeueLTPro-Lt.otf"));
    }

    public CustomTextViewHelvetica(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public CustomTextViewHelvetica(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }
}
