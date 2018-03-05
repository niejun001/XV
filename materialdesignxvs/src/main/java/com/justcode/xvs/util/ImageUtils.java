package com.justcode.xvs.util;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

import com.justcode.xvs.App;
import com.justcode.xvs.R;

public class ImageUtils {
    /**
     * 根据bitmap提取颜色
     *
     * @param bitmap
     * @return
     */
    public static int getColor(Bitmap bitmap) {
        if (bitmap != null) {
            Palette p = Palette.from(bitmap).generate();
            Palette.Swatch s_dm = p.getDarkMutedSwatch();
            Palette.Swatch s_dv = p.getDarkVibrantSwatch();
            if (s_dm != null) {
                return s_dm.getRgb();
            } else {
                if (s_dv != null) {
                    return s_dv.getRgb();
                } else {
                    return UIUtils.getColor(App.getApplication(), R.color.colorPrimary);
                }
            }
        } else {
            return UIUtils.getColor(App.getApplication(), R.color.colorPrimary);
        }
    }
}
