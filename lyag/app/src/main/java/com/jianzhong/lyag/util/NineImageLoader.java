package com.jianzhong.lyag.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.lzy.ninegrid.NineGridView;

/**
 * Created by chenzhikai
 * Email 1310741158@qq.com
 */
public class NineImageLoader implements NineGridView.ImageLoader {

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        GlideUtils.load(imageView, url);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }

}
