package com.justcode.xvs.adapter;


import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justcode.xvs.R;
import com.justcode.xvs.bean.CategoryList;

import java.util.List;

/**
 * Created by niejun on 2017/8/9.
 */

public class ListAdapter extends BaseItemDraggableAdapter<CategoryList,BaseViewHolder> {

    public ListAdapter(List list) {
        super(R.layout.item_list,list);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryList item) {
        TextView listitem = (TextView) helper.getView(R.id.listitem);

        switch (helper.getLayoutPosition() % 3) {
            case 0:
                helper.setImageResource(R.id.iv_itemhead, R.mipmap.head_img0);
                break;
            case 1:
                helper.setImageResource(R.id.iv_itemhead, R.mipmap.head_img1);
                break;
            case 2:
                helper.setImageResource(R.id.iv_itemhead, R.mipmap.head_img2);
                break;
        }

        listitem.setText(item.getLabel());
        helper.addOnClickListener(R.id.listitem);
    }
}
