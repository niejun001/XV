package com.justcode.xvs.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justcode.xvs.R;
import com.justcode.xvs.bean.Videolist;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by niejun on 2017/8/7.
 */

public class VideoAdapter extends BaseQuickAdapter<Videolist, BaseViewHolder> {
    private final Context context;

    public VideoAdapter(List list,Context context) {
        super(R.layout.videoadapter,list);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final Videolist item) {
        final ImageView iv = (ImageView) helper.getView(R.id.pic);
        TextView name = (TextView) helper.getView(R.id.name);
        //设置占位符
        iv.setImageDrawable(context.getDrawable(R.mipmap.error));
        name.setText(item.getName());
        //为iv设置Tag,内容是该imageView等待加载的图片url
        iv.setTag(item.getImgurl());

        Glide.with(context)
             .load(item.getImgurl())
             .bitmapTransform(new RoundedCornersTransformation(context,10,0,RoundedCornersTransformation.CornerType.ALL))
             .crossFade(500)
             .into(new SimpleTarget<GlideDrawable>() {
                 @Override
                 public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                     //加载完毕后判断该imageView等待的图片url是不是加载完毕的这张
                     //如果是则为imageView设置图片,否则说明imageView已经被重用到其他item
                     if (item.getImgurl().equals(iv.getTag())){
                         iv.setImageDrawable(resource);
                     }
                 }
             });
        helper.addOnClickListener(R.id.name);
        helper.addOnClickListener(R.id.video_ll);
    }
}
