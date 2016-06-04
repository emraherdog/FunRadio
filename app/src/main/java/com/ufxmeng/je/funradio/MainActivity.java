package com.ufxmeng.je.funradio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufxmeng.je.funradio.adapter.RadioAdapter;
import com.ufxmeng.je.funradio.bean.RadioInfo;
import com.ufxmeng.je.funradio.utils.RadioDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;
    RadioAdapter mAdapter;
    List<RadioInfo> mRadioInfos;

    TextView mTextViewPlayTime;
    ImageView mImageViewProfile;
    ImageView mImageViewVolume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);

        initView();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new RadioDecoration());

        mAdapter = new RadioAdapter(this, getData());

        mAdapter.setOnItemClickListener(new RadioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                final RadioInfo radioInfo = mRadioInfos.get(position);
                mImageViewProfile.setImageResource(radioInfo.getImageResID());

            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {

        mTextViewPlayTime = (TextView) findViewById(R.id.textview_paly_time);
        mImageViewProfile = (ImageView) findViewById(R.id.imageview_profile);
        mImageViewVolume = (ImageView) findViewById(R.id.imageview_volume);
    }


    private List<RadioInfo> getData() {
        mRadioInfos = new ArrayList<>();

        for (int i = 0; i < 21; i++) {

            if (R.mipmap.bbc_radio_4 + i != R.mipmap.ic_launcher) {
                RadioInfo radioInfo = new RadioInfo();
                radioInfo.setImageResID(R.mipmap.bbc_radio_4 + i);
                radioInfo.setRadioStationUrl("http://audio.scdn.arkena.com/11006/franceinfo-midfi128.mp3");
                mRadioInfos.add(radioInfo);
            }

        }
        return mRadioInfos;
    }


}
