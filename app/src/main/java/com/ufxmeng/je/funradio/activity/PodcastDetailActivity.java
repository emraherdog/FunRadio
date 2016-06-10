package com.ufxmeng.je.funradio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ufxmeng.je.funradio.R;
import com.ufxmeng.je.funradio.adapter.PodcastDetailAdapter;
import com.ufxmeng.je.funradio.bean.PodcastDetail;
import com.ufxmeng.je.funradio.utils.RadioDecoration;
import com.ufxmeng.je.funradio.utils.VolleySingleton;
import com.ufxmeng.je.funradio.utils.XmlUtils;

import java.util.List;

public class PodcastDetailActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    LinearLayout mLinearLayout;

    PodcastDetailAdapter mAdapter;
    Handler mHandler;
    private RequestQueue mRequestQueue;
    private static final String PODCAST_URL = "http://www.rtl.fr/podcast/linvite-de-rtl.xml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_detail);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_podcast_detail);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayout_empty_view);

        mHandler = new Handler(Looper.getMainLooper());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new RadioDecoration());

        final Intent intent = getIntent();
        final String podcast_url = intent.getStringExtra("podcast_url");
        final int res_id = intent.getIntExtra("image_res_id", R.mipmap.podcast_heure_du_crime);

        mRequestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(podcast_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            final List<PodcastDetail> podcastDetails = new XmlUtils().parseXml(response, res_id);
                            if (podcastDetails != null && podcastDetails.size() > 0) {
                                mAdapter = new PodcastDetailAdapter(PodcastDetailActivity.this, podcastDetails);
                                mRecyclerView.setAdapter(mAdapter);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mLinearLayout.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        stringRequest.setTag("cancel");
        mRequestQueue.add(stringRequest);

    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll("cancel");
        super.onDestroy();
    }

}
