package com.ufxmeng.je.funradio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ufxmeng.je.funradio.R;
import com.ufxmeng.je.funradio.bean.RadioInfo;

import java.util.List;

/**
 * Created by JE on 6/3/2016.
 */
public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {

    Context mContext;
    List<RadioInfo> mRadioInfos;

    public RadioAdapter(Context context, List<RadioInfo> radioInfos) {
        mContext = context;
        mRadioInfos = radioInfos;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final RadioInfo radioInfo = mRadioInfos.get(position);
        holder.mImageView.setImageResource(radioInfo.getImageResID());
        holder.mImageView.setMaxHeight(holder.mImageView.getWidth());

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRadioInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.imageview_item);
        }
    }

}
