package com.jianzhong.lyag.util;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.glide.CropCircleTransformation;

import java.io.File;

/**
 * Created by zhengwencheng on 2017/2/7.
 * package com.jianzhong.ys.util
 */

public class GlideUtils {

    public static void load(ImageView imageView, String url) {
        Glide.with(BaseApplication.getInstance().getApplicationContext())
                .load(url)
                .dontAnimate()
//                .centerCrop()
                .error(R.drawable.homepage_lovica1)
                .placeholder(R.drawable.homepage_lovica1)
                .into(imageView);
    }

    /**
     * 加载本地图片
     *
     * @param imageView
     * @param path
     */
    public static void loadlocalImage(ImageView imageView, String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Glide.with(BaseApplication.getInstance().getApplicationContext()).load(uri)
                .dontAnimate()
                .error(R.drawable.homepage_lovica1)
                .placeholder(R.drawable.homepage_lovica1)
                .into(imageView);
    }

    /**
     * 加载本地圆形图片
     *
     * @param imageView
     * @param path      本地图片地址
     */
    public static void loadLocalCircleImage(ImageView imageView, String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Glide.with(BaseApplication.getInstance().getApplicationContext()).load(uri)
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(BaseApplication.getInstance().getApplicationContext()))
                .error(R.drawable.mine_zwtx)
                .placeholder(R.drawable.mine_zwtx)
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param imageView
     * @param url
     */
    public static void loadCircleImage(ImageView imageView, String url) {
        Glide.with(BaseApplication.getInstance().getApplicationContext())
                .load(url)
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(BaseApplication.getInstance().getApplicationContext()))
                .error(R.drawable.mine_zwtx)
                .placeholder(R.drawable.mine_zwtx)
                .into(imageView);
    }

    /**
     * 加载圆头像图片
     *
     * @param imageView
     * @param url
     */
    public static void loadAvatarImage(ImageView imageView, String url) {
        Glide.with(BaseApplication.getInstance().getApplicationContext())
                .load(url)
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(BaseApplication.getInstance().getApplicationContext()))
                .error(R.drawable.mine_zwtx)
                .placeholder(R.drawable.mine_zwtx)
                .into(imageView);
    }

}
