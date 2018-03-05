package com.justcode.xvs.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justcode.xvs.R;
import com.justcode.xvs.bean.Videolist;

import java.util.List;

/**
 * Created by ChinaLHR on 2016/12/15.
 * Email:13435500980@163.com
 */

public class VideoAdapter extends BasePagerAdapter<Videolist> {


    public VideoAdapter(Context context, List<Videolist> date) {
        super(context, date);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER)
            return new MyViewHolder(mFooterView);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_basepager, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) return;

        ((MyViewHolder)holder).item_base_tv_title.setText(mDate.get(position).getName());

        Glide.with(mContext)
                .load(mDate.get(position).getImgurl())
                .into(((MyViewHolder)holder).item_base_iv);

        //设置点击事件
        if (mListener != null) {
            ((MyViewHolder)holder).item_movie_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    mListener.ItemClickListener(mDate.get(position).getName(),mDate.get(position).getImgurl(),mDate.get(position).getVideourl());
                }
            });

            //长按事件
//            ((MyViewHolder)holder).item_movie_cardview.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    int position = holder.getLayoutPosition();
//                    mListener.ItemLongClickListener(holder.itemView, position);
//                    return true;
//                }
//            });
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView item_base_iv;
        TextView item_base_tv_title;
        CardView item_movie_cardview;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_base_iv = (ImageView) itemView.findViewById(R.id.item_base_iv);
            item_base_tv_title = (TextView) itemView.findViewById(R.id.item_base_tv_title);
            item_movie_cardview = (CardView) itemView.findViewById(R.id.item_movie_cardview);
        }
    }

}
