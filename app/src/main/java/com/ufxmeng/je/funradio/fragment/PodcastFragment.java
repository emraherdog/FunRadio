package com.ufxmeng.je.funradio.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ufxmeng.je.funradio.R;
import com.ufxmeng.je.funradio.activity.PodcastDetailActivity;
import com.ufxmeng.je.funradio.adapter.RadioAdapter;
import com.ufxmeng.je.funradio.bean.RadioInfo;
import com.ufxmeng.je.funradio.utils.RadioDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PodcastFragment extends Fragment {


    private FragmentActivity mActivity;
    RecyclerView mRecyclerView;
    List<RadioInfo> mRadioInfos = null;
    RadioAdapter mAdapter;
    String[] mPodcastUrls;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_podcast, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_podcast);

        mActivity = getActivity();

        mPodcastUrls = getResources().getStringArray(R.array.podcast_urls);

        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new RadioDecoration());

        mAdapter = new RadioAdapter(mActivity, getRadioInfos());

        mAdapter.setOnItemClickListener(new RadioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), PodcastDetailActivity.class);
                intent.putExtra("podcast_url", mPodcastUrls[position]);
                intent.putExtra("image_res_id", mRadioInfos.get(position).getImageResID());
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private List<RadioInfo> getRadioInfos() {

        mRadioInfos = new ArrayList<>();
        int[] podcastImageResId = {
                R.mipmap.podcast_heure_du_crime, R.mipmap.podcast_rtl_matin,
                R.mipmap.podcast_rtl_midi, R.mipmap.podcast_rtl_soir,
                R.mipmap.podcast_grand_jury, R.mipmap.podcast_invite_de_rtl_matin,
                R.mipmap.podcast_le_journal_rtl};

        for (int i = 0; i < podcastImageResId.length; i++) {

            final RadioInfo radioInfo = new RadioInfo();
            radioInfo.setImageResID(podcastImageResId[i]);
            radioInfo.setRadioStationUrl(mPodcastUrls[i]);
            mRadioInfos.add(radioInfo);
        }

        return mRadioInfos;
    }

}
