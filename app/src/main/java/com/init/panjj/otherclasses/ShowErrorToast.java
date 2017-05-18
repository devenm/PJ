package com.init.panjj.otherclasses;

import android.app.Activity;
import android.os.Build.VERSION;
import android.view.View;
import android.widget.Toast;

import com.init.panjj.R;

public class ShowErrorToast {
    public ShowErrorToast(Activity activity, String message) {
        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        if (VERSION.SDK_INT < 21) {
            View vieew = toast.getView();
            vieew.setBackgroundResource(R.drawable.background);
            toast.setView(vieew);
        }
        toast.show();
    }
}
