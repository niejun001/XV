package com.justcode.xvs.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2018/2/8.
 */

public class UIUtils {
    /**
     * 加载资源文件===================
     */
    // 获取字符串
    public static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

    // 获取字符串数组
    public static String[] getStringArray(Context context,int id) {
        return context.getResources().getStringArray(id);
    }

    // 获取图片
    public static Drawable getDrawable(Context context, int id) {
        return context.getResources().getDrawable(id);
    }

    // 获取颜色
    public static int getColor(Context context,int id) {
        return context.getResources().getColor(id);
    }

    // 根据id获取颜色的状态选择器
    public static ColorStateList getColorStateList(Context context, int id) {
        return context.getResources().getColorStateList(id);
    }
}
