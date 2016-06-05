package com.ufxmeng.je.funradio.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.orhanobut.logger.Logger;
import com.ufxmeng.je.funradio.utils.NetworkUtils;
import com.ufxmeng.je.funradio.utils.PrefUtils;

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        final NetworkUtils networkUtils = NetworkUtils.getInstance(context);
        final String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            final boolean inConnected = networkUtils.inConnected();
            Logger.d("inConnected " + inConnected);
        }
    }
}
