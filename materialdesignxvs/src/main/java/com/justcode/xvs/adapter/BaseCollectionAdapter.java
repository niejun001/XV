package com.justcode.xvs.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.justcode.xvs.R;

public abstract class BaseCollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    /**
     * 点击回调接口
     */
    public OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public interface OnItemClickListener {
        void onItemClick(String name, String imguel,String videourl);
    }

    public BaseCollectionAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_collection, parent, false));
        return holder;
    }

    public class Holder extends RecyclerView.ViewHolder {
        public CardView item_collection_card;
        public ImageView item_img;
        public TextView item_name;
        public TextView item_time;

        public Holder(View itemView) {
            super(itemView);
            item_collection_card = (CardView) itemView.findViewById(R.id.item_collection_card);
            item_img = (ImageView) itemView.findViewById(R.id.item_img);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            item_time = (TextView) itemView.findViewById(R.id.item_time);
        }
    }


}
