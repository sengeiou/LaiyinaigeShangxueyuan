package com.baselib.widget;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Android7.0 popwindow适配
 * 解决地址：http://blog.csdn.net/CHITTY1993/article/details/78297638
 * 适配 Android 7.0  7.1
 * Created by chenzhikai on 2018/1/2.
 */

public class FixedPopupWindow extends PopupWindow {

    public FixedPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height, false);
    }

    public FixedPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

}
