package com.justcode.xvs.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/2/8.
 */

public class PreferncesUtils {
    private static SharedPreferences sp;

    private static final String Catch_file = "com.justcode.xvs_preferences";

    public static String getString(Context ctx, String key, String defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(Catch_file, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    public static Boolean getBoolean(Context ctx, String key, boolean defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(Catch_file, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }
}
