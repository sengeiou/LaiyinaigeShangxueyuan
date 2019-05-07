package com.jianzhong.lyag.flexboxlayout;

import android.content.Context;

/**
 * Created by zhy on 16/5/16.
 */
public class DimenUtils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
