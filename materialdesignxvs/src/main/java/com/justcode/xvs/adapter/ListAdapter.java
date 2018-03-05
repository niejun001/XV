package com.justcode.xvs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.justcode.xvs.R;
import com.justcode.xvs.bean.CategoryList;

import java.util.List;

/**
 * Created by Administrator on 2018/2/8.
 */

public class ListAdapter extends BasePagerAdapter<CategoryList> {

    public ListAdapter(Context context, List<CategoryList> date) {
        super(context, date);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER)
            return new MyViewHolder(mFooterView);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) return;

        ((MyViewHolder)holder).listitem.setText(mDate.get(position).getLabel());

        switch (holder.getLayoutPosition() % 3) {
            case 0:
                ((MyViewHolder) holder).iv_itemhead.setImageResource(R.mipmap.head_img0);
                break;
            case 1:
                ((MyViewHolder) holder).iv_itemhead.setImageResource(R.mipmap.head_img1);
                break;
            case 2:
                ((MyViewHolder) holder).iv_itemhead.setImageResource(R.mipmap.head_img2);
                break;
        }

        //设置点击事件
        if (mListener != null) {
            ((MyViewHolder)holder).listitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    mListener.ItemListClickListener(mDate.get(position).getUrl(),mDate.get(position).getLabel());
                }
            });
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_itemhead;
        TextView listitem;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_itemhead = (ImageView) itemView.findViewById(R.id.iv_itemhead);
            listitem = (TextView) itemView.findViewById(R.id.listitem);
        }
    }
}
