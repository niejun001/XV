package com.justcode.xvs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.justcode.xvs.bean.VideoCollection;

import java.util.List;

/**
 * Created by Administrator on 2018/2/9.
 */

public class CollectionVideoAdapter extends BaseCollectionAdapter{


    private final List<VideoCollection> mdate;
    private final Context mContext;

    public CollectionVideoAdapter(Context context, List<VideoCollection> list) {
        super(context);
        mdate = list;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(mdate.get(position).getImgurl())
                .into(((Holder) holder).item_img);

        ((Holder) holder).item_name.setText(mdate.get(position).getName());
        ((Holder) holder).item_time.setText("收藏时间：" + mdate.get(position).getTime());

        if (mListener != null) {
            ((Holder) holder).item_collection_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(mdate.get(position).getName(), mdate.get(position).getImgurl(),mdate.get(position).getVideourl());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mdate.size();
    }
}
