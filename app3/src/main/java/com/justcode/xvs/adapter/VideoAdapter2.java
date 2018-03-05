package com.justcode.xvs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.justcode.xvs.R;
import com.justcode.xvs.bean.Videolist;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2018/2/7.
 */

public class VideoAdapter2 extends RecyclerView.Adapter<VideoAdapter2.MyViewHolder> {
    private final List<Videolist> videolists;
    private final Context mContext;
    private final LayoutInflater inflater;

    public VideoAdapter2(Context context, List<Videolist> videolists) {
        this.mContext = context;
        this.videolists = videolists;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public VideoAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.videoadapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoAdapter2.MyViewHolder holder, int position) {
        final Videolist videolist = videolists.get(position);
        holder.name.setText(videolist.getName());
        holder.iv.setImageDrawable(mContext.getDrawable(R.mipmap.error));
        holder.iv.setTag(videolist.getImgurl());
        Glide.with(mContext)
                .load(videolist.getImgurl())
                .bitmapTransform(new RoundedCornersTransformation(mContext,10,0,RoundedCornersTransformation.CornerType.ALL))
                .crossFade(500)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        //加载完毕后判断该imageView等待的图片url是不是加载完毕的这张
                        //如果是则为imageView设置图片,否则说明imageView已经被重用到其他item
                        if (videolist.getImgurl().equals(holder.iv.getTag())){
                            holder.iv.setImageDrawable(resource);
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return videolists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv;
        private final TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
        }
    }
}
