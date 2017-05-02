package com.init.panj.radioplayer;

/**
 * Created by deepak on 10/13/2015.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class CallRecord extends BroadcastReceiver {
    TelephonyManager telManager;

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
               try {
                       Controls.pauseControl(context);
               }
               catch (Exception e){}

            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
              try {
                      Controls.pauseControl(context);

              }
              catch (Exception e){}
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                try {
Controls.playControl(context);
                }
                catch (Exception e){}
            }

        }
    }
}
