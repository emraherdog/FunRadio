package com.ufxmeng.je.funradio;

import android.app.Application;

import com.ufxmeng.je.funradio.utils.NetworkUtils;

/**
 * Created by JE on 6/4/2016.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final NetworkUtils networkUtils = NetworkUtils.getInstance(getApplicationContext());
        final boolean inConnected = networkUtils.inConnected();
    }
}
