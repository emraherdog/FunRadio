package com.ufxmeng.je.funradio.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Created by JE on 6/4/2016.
 */
public class NetworkUtils {

    Context mContext;
    private static NetworkUtils sNetworkUtils;

    public static NetworkUtils getInstance(Context context) {
        if (sNetworkUtils == null) {
            sNetworkUtils = new NetworkUtils(context);
        }
        return sNetworkUtils;
    }

    private NetworkUtils(Context context) {
        mContext = context;
    }

    public boolean inConnected() {

        final PrefUtils prefUtils = PrefUtils.getInstance(mContext);
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            prefUtils.setIsNetworkConnected(networkInfo.isConnected());
            return networkInfo.isConnected();
        } else {
            prefUtils.setIsNetworkConnected(false);
        }
        return false;
    }
}
