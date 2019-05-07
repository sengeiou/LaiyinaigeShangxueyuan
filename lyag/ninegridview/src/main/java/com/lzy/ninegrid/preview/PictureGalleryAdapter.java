package com.lzy.ninegrid.preview;

import android.content.Context;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.NineGridViewAdapter;

import java.util.List;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/3/21
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class PictureGalleryAdapter extends NineGridViewAdapter {

    private int statusHeight;

    private OnItemCallBack listener;
    public PictureGalleryAdapter(Context context, List<ImageInfo> imageInfo, OnItemCallBack listener) {
        super(context, imageInfo);
        this.listener = listener;
        statusHeight = getStatusHeight(context);
    }

    @Override
    protected void onImageItemClick(Context context, NineGridView nineGridView, int index, List<ImageInfo> imageInfo) {
//        for (int i = 0; i < imageInfo.size(); i++) {
//            ImageInfo info = imageInfo.get(i);
//            View imageView;
//            if (i < nineGridView.getMaxSize()) {
//                imageView = nineGridView.getChildAt(i);
//            } else {
//                //如果图片的数量大于显示的数量，则超过部分的返回动画统一退回到最后一个图片的位置
//                imageView = nineGridView.getChildAt(nineGridView.getMaxSize() - 1);
//            }
//            info.imageViewWidth = imageView.getWidth();
//            info.imageViewHeight = imageView.getHeight();
//            int[] points = new int[2];
//            imageView.getLocationInWindow(points);
//            info.imageViewX = points[0];
//            info.imageViewY = points[1] - statusHeight;
//        }

        listener.callBack();
    }

    /**
     * 获得状态栏的高度
     */
    public int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
