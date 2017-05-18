package com.init.panjj.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build.VERSION;

public class CheckNetworkConnection {
    public boolean haveNetworkConnection(Context act) {
        ConnectivityManager connectivityManager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (VERSION.SDK_INT >= 21) {
            for (Network mNetwork : connectivityManager.getAllNetworks()) {
                if (connectivityManager.getNetworkInfo(mNetwork).getState().equals(State.CONNECTED)) {
                    return true;
                }
            }
        } else if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
