package com.ufxmeng.je.funradio.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufxmeng.je.funradio.R;
import com.ufxmeng.je.funradio.bean.PodcastDetail;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by JE on 6/5/2016.
 */
public class PodcastDetailAdapter extends RecyclerView.Adapter<PodcastDetailAdapter.ViewHolder> {

    Context mContext;
    List<PodcastDetail> mPodcastDetails;

    public PodcastDetailAdapter(Context context, List<PodcastDetail> podcastDetails) {
        mContext = context;
        mPodcastDetails = podcastDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final PodcastDetail detail = mPodcastDetails.get(position);

        holder.mImageView.setImageResource(detail.getImageResID());
        holder.mTextViewTitle.setText(detail.getTitle());
        holder.mTextViewPubdate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(detail.getPubDate()));
        holder.mTextViewDuration.setText(detail.getDuration());

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                final String url = detail.getPodcastMp3Url();
                if (url != null) {
                    manager.enqueue(new DownloadManager.Request(Uri.parse(url)));
                }

               /* Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);*/
            }
        });

    }

    @Override
    public int getItemCount() {

        return mPodcastDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mTextViewTitle;
        TextView mTextViewPubdate;
        TextView mTextViewDuration;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.imageview_item_podcast_detail);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.textview_title_podcast_detail);
            mTextViewPubdate = (TextView) itemView.findViewById(R.id.textview_pubdate_podcast_detail);
            mTextViewDuration = (TextView) itemView.findViewById(R.id.textview_duration_podcast_detail);
        }
    }
}
