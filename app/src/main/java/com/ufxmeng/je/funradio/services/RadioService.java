package com.ufxmeng.je.funradio.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.ufxmeng.je.funradio.utils.RadioState;

import java.io.IOException;

public class RadioService extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {


    private int currentState = RadioState.STATE_STOP;
    MediaPlayer mMediaPlayer = null;
    private WifiManager.WifiLock mWifiLock;
    String mLastPlayUrl = "";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && intent.getAction() != null) {

            switch (intent.getAction()) {
                case RadioState.ACTION_PLAY:
                    radioPlay(intent);
                    break;
                case RadioState.ACTION_STOP:
                    radioStop(intent);
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


    private void radioStop(Intent intent) {
        if (currentState == RadioState.STATE_PLAY) {
            currentState = RadioState.STATE_STOP;
            mMediaPlayer.pause();

            intent = new Intent();
            intent.setAction(RadioState.ACTION_BROADCAST_PAUSE);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            Logger.d("ACTION_STOP");
        }
    }

    private void radioPlay(Intent intent) {

        currentState = RadioState.STATE_PLAY;
        final String url = intent.getStringExtra("url");

        try {

            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mWifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
            mWifiLock.acquire();

            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync();
            mLastPlayUrl = url;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.d("ACTION_PLAY %s", url);
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw null;
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        if (mWifiLock != null) {
            mWifiLock.release();
        }
        Logger.d("onDestroy");
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.start();
        mp.setVolume(1.0f, 1.0f);
        mHandler.sendEmptyMessage(100);

        Intent intent = new Intent(RadioState.ACTION_BROADCAST_PREPARED);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        Toast.makeText(RadioService.this, "Start playing!", Toast.LENGTH_SHORT).show();
        Logger.d("onPrepared");
    }


    private void sendBroadcastPlayTime(String action) {
        Intent intent = new Intent(action);
        intent.putExtra("play_time", mMediaPlayer.getCurrentPosition());
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mMediaPlayer.isPlaying()) {
                sendBroadcastPlayTime(RadioState.ACTION_BROADCAST_PLAYTIME);
                sendEmptyMessageDelayed(240, 500);
            } else if (currentState == RadioState.STATE_PLAY &&
                    !mMediaPlayer.isPlaying()) {
                sendBroadcastPlayTime(RadioState.ACTION_BROADCAST_CACHE);
            }
        }
    };

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        Intent intent = new Intent(RadioState.ACTION_BROADCAST_ERROR);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        Toast.makeText(getApplicationContext(), "Failed to Load Stream.", Toast.LENGTH_SHORT).show();
        Logger.d("what %d, extra %d", what, extra);
        stopSelf();
        return true;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                    mMediaPlayer.setVolume(1.0f, 1.0f);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                    stopSelf();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                break;
        }

    }
}
