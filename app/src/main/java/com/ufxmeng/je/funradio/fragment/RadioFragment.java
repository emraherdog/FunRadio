package com.ufxmeng.je.funradio.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.ufxmeng.je.funradio.R;
import com.ufxmeng.je.funradio.adapter.RadioAdapter;
import com.ufxmeng.je.funradio.bean.RadioInfo;
import com.ufxmeng.je.funradio.services.RadioService;
import com.ufxmeng.je.funradio.utils.PrefUtils;
import com.ufxmeng.je.funradio.utils.RadioDecoration;
import com.ufxmeng.je.funradio.utils.RadioState;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadioFragment extends Fragment {


    RecyclerView mRecyclerView;
    RadioAdapter mAdapter;
    List<RadioInfo> mRadioInfos;

    TextView mTextViewPlayTime;
    ImageView mImageViewProfile;
    ImageView mImageViewVolume;

    private int currentState = RadioState.STATE_STOP;
    private int currentPosition = 0;
    private FragmentActivity mActivity;
    private String[] mRadioUrls;
    private PrefUtils mPrefUtils;

    AudioManager mAudioManager;
    boolean isSilent = false;

    RelativeLayout mRelativeLayout;
    private ProgressBar mProgressBar = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_radio, container, false);

        mActivity = getActivity();

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        mPrefUtils = PrefUtils.getInstance(mActivity);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_main);

        initView(view);

        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new RadioDecoration());

        mAdapter = new RadioAdapter(getActivity(), getData());


        mAdapter.setOnItemClickListener(new RadioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (!mPrefUtils.getIsNetworkConnected()) {
                    Toast.makeText(mActivity, "No Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentPosition == position && currentState == RadioState.STATE_PLAY) {

                    currentState = RadioState.STATE_STOP;
                    Intent intent = new Intent(mActivity, RadioService.class);
                    intent.setAction(RadioState.ACTION_STOP);
                    mActivity.startService(intent);


                } else {

                    currentState = RadioState.STATE_PLAY;
                    //hide last progress bar
                    View childView = mRecyclerView.getChildAt(currentPosition);
                    mProgressBar = (ProgressBar) childView.findViewById(R.id.progressbar_item);
                    mProgressBar.setVisibility(View.GONE);
                    mTextViewPlayTime.setText("00:00");

                    currentPosition = position;

                    final RadioInfo radioInfo = mRadioInfos.get(position);
                    mImageViewProfile.setImageResource(radioInfo.getImageResID());

                    Intent intent = new Intent(mActivity, RadioService.class);
                    intent.setAction(RadioState.ACTION_PLAY);
                    intent.putExtra("url", mRadioUrls[position]);
                    mActivity.startService(intent);

                    //display progress bar
                    childView = mRecyclerView.getChildAt(position);
                    mProgressBar = (ProgressBar) childView.findViewById(R.id.progressbar_item);
                    mProgressBar.setVisibility(View.VISIBLE);

                }


            }
        });


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                final int childCount = mRecyclerView.getChildCount();
                final int totalItemCount = layoutManager.getItemCount();
                final int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItemPosition + childCount >= totalItemCount
                        && dy > 5 && mRelativeLayout.getTranslationY() < 1) {
//                    Logger.d("%d", firstVisibleItemPosition);
                 /*   ObjectAnimator animator = ObjectAnimator.ofFloat(mRelativeLayout, "translationY", 1.0f);
                    animator.setDuration(1200);
                    animator.start();*/
                    mRelativeLayout.animate()
                            .y(mRelativeLayout.getHeight() + mRelativeLayout.getY())
                            .setDuration(500)
                            .start();
                }

                if (dy < -5 && mRelativeLayout.getTranslationY() >= mRelativeLayout.getHeight()) {

/*
                    ObjectAnimator animator = ObjectAnimator.ofFloat(mRelativeLayout, "translationY", -1.0f);
                    animator.setDuration(1200);
                    animator.start();
*/
                    mRelativeLayout.animate()
                            .y(mRelativeLayout.getY() - mRelativeLayout.getHeight())
                            .setDuration(500)
                            .start();

                }

            }
        });

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    private void initView(View view) {

        mTextViewPlayTime = (TextView) view.findViewById(R.id.textview_paly_time);
        mImageViewProfile = (ImageView) view.findViewById(R.id.imageview_profile);
        mImageViewVolume = (ImageView) view.findViewById(R.id.imageview_volume);

        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.relativelayout_bottom);

        mImageViewVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///// TODO: 6/4/2016 add listener

                if (isSilent) {
                    mImageViewVolume.setImageResource(R.mipmap.ic_volume);
                    final int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_PLAY_SOUND);
                    isSilent = false;
                } else {
                    mImageViewVolume.setImageResource(R.mipmap.ic_mute);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_PLAY_SOUND);
                    isSilent = true;

                }
            }
        });


        //add animation to mImageViewProfile

    }

    private List<RadioInfo> getData() {

        mRadioUrls = getResources().getStringArray(R.array.radio_urls);
        final int[] mRadioIcons = {R.mipmap.rmc_info_talk_sport, R.mipmap.rtl, R.mipmap.europe1,
                R.mipmap.france_inter, R.mipmap.france_info, R.mipmap.radiomeuh,
                R.mipmap.fip, R.mipmap.fun_radio_fr, R.mipmap.cherie_fm,
                R.mipmap.bfm, R.mipmap.virgin_radio_officiel, R.mipmap.rfm_1039_fm,
                R.mipmap.nrj_france, R.mipmap.skyrock, R.mipmap.chantefrance,
                R.mipmap.ouifm, R.mipmap.france_bleu_nord, R.mipmap.rireetchansons,
                R.mipmap.bbc_world_service, R.mipmap.espn_radio, R.mipmap.npr_news};


        mRadioInfos = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            RadioInfo radioInfo = new RadioInfo();
            radioInfo.setRadioStationUrl(mRadioUrls[i]);
            radioInfo.setImageResID(mRadioIcons[i % mRadioIcons.length]);
            mRadioInfos.add(radioInfo);
        }

        return mRadioInfos;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == null) {
                return;
            }
            switch (intent.getAction()) {
                case RadioState.ACTION_BROADCAST_PREPARED:
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                    Logger.d("ACTION_BROADCAST_PREPARED");
                    break;
                case RadioState.ACTION_BROADCAST_PAUSE:
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                    Logger.d("ACTION_BROADCAST_PAUSE");
                    break;
                case RadioState.ACTION_BROADCAST_ERROR:
                    stopTextPlayTime();
                    Logger.d("ACTION_BROADCAST_ERROR");
                    break;
                case RadioState.ACTION_BROADCAST_PLAYTIME:
                    updatePlayTime(intent);
                    break;
                case RadioState.ACTION_BROADCAST_CACHE:
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    private void updatePlayTime(Intent intent) {
        if (intent != null) {

            if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
                mProgressBar.setVisibility(View.GONE);
            }
            final int playTime = intent.getIntExtra("play_time", 0);
            mTextViewPlayTime.setText(millToString(playTime));
        }

    }

    private String millToString(int time) {

        return (time / 1000 / 60 >= 10 ? time / 1000 / 60 : "0" + time / 1000 / 60)
                + ":" +
                (time / 1000 % 60 >= 10 ? time / 1000 % 60 : "0" + time / 1000 % 60);
    }

    private void stopTextPlayTime() {
        mTextViewPlayTime.setText("00:00");
    }


    @Override
    public void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RadioState.ACTION_BROADCAST_PREPARED);
        intentFilter.addAction(RadioState.ACTION_BROADCAST_ERROR);
        intentFilter.addAction(RadioState.ACTION_BROADCAST_PAUSE);
        intentFilter.addAction(RadioState.ACTION_BROADCAST_PLAYTIME);
        intentFilter.addAction(RadioState.ACTION_BROADCAST_CACHE);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onPause() {

        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.stopService(new Intent(mActivity, RadioService.class));
    }
}
