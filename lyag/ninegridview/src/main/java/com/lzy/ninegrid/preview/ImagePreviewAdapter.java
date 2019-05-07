package com.lzy.ninegrid.preview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.thumbnail.CustomImageModelLoader;
import com.lzy.thumbnail.CustomImageSizeModel;
import com.lzy.thumbnail.CustomImageSizeModelImp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/3/21
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImagePreviewAdapter extends PagerAdapter implements PhotoViewAttacher.OnPhotoTapListener {

    private List<ImageInfo> imageInfo;
    private Context context;
    private View currentView;

    public ImagePreviewAdapter(Context context, @NonNull List<ImageInfo> imageInfo) {
        super();
        this.imageInfo = imageInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageInfo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentView = (View) object;
    }

    public View getPrimaryItem() {
        return currentView;
    }

    public ImageView getPrimaryImageView() {
        return (ImageView) currentView.findViewById(R.id.pv);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photoview, container, false);
        View loading = view.findViewById(R.id.progressBar);
        final PhotoView imageView = (PhotoView) view.findViewById(R.id.pv);

        final ImageInfo info = this.imageInfo.get(position);
        imageView.setOnPhotoTapListener(this);
        showExcessPic(info, imageView);

        //如果需要加载的loading,需要自己改写,不能使用这个方法
        if (!TextUtils.isEmpty(info.bigImageUrl)) {
            displayImage(info.getBigImageUrl(), imageView, loading);
        } else {
            displayImage(info.getThumbnailUrl(), imageView, loading);
        }

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupWindow((Activity) context, v, !TextUtils.isEmpty(info.bigImageUrl) ? info.bigImageUrl : info.thumbnailUrl);
                return false;
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 展示过度图片
     */
    private void showExcessPic(ImageInfo imageInfo, PhotoView imageView) {
        //先获取大图的缓存图片
        Bitmap cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.bigImageUrl);
        //如果大图的缓存不存在,在获取小图的缓存
        if (cacheImage == null) {
            cacheImage = NineGridView.getImageLoader().getCacheImage(imageInfo.thumbnailUrl);
        }
        //如果没有任何缓存,使用默认图片,否者使用缓存
        if (cacheImage == null) {
            imageView.setImageResource(R.drawable.ic_default_image);
        } else {
            imageView.setImageBitmap(cacheImage);
        }
    }

    /**
     * 单击屏幕关闭
     */
    @Override
    public void onPhotoTap(View view, float x, float y) {
        ((ImagePreviewActivity) context).finishActivityAnim();
    }

    public void saveImage(String url) {
        OkGo.get(url)
                .tag(this)
                .execute(new FileCallback() {  //文件下载时，可以指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        // file 即为文件数据，文件保存在指定目录
                        Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();
                        // 最后通知图库更新
                        try {
                            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(file);
                        intent.setData(uri);
                        context.sendBroadcast(intent);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(context, "保存失败", Toast.LENGTH_LONG).show();
                    }

                });
    }

    /**
     * 查看图片，
     * 1、 先加载本地的默认图
     * 2、然后load原图地址的缩略图（规则查看 ：https://www.alibabacloud.com/help/zh/doc-detail/44687.htm）
     * 3、最后load原图
     */
    public void displayImage(final String baseUrl, final ImageView imageView, final View loading) {
        DrawableRequestBuilder thumbnailBuilder = Glide
                .with(imageView.getContext())
                .load(new CustomImageSizeModelImp(baseUrl)
                        .requestCustomSizeUrl(100, 50))
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(context)
                .using(new CustomImageModelLoader(imageView.getContext()))
                .load(new CustomImageSizeModelImp(baseUrl))
                .listener(new RequestListener<CustomImageSizeModel, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, CustomImageSizeModel model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, CustomImageSizeModel model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.e("加载成功-------------", model.getBaseUrl());
                        loading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .thumbnail(thumbnailBuilder)
                .placeholder(R.drawable.homepage_lovica1)
                .into(imageView);
    }

    public void showPopupWindow(final Activity mActivity, View view, final String url) {
        View popwindow_view = mActivity.getLayoutInflater().inflate(R.layout.popwindow_save_image, null, false);
        // 创建PopupWindow实例
        final PopupWindow popupWindow = new PopupWindow(popwindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 让pop可以点击外面消失掉
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
//        params.alpha = 0.7f;
        mActivity.getWindow().setAttributes(params);
        // 这里是位置显示方式,在屏幕的左侧
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        // 点击其它地方关闭对话框
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1f;
                mActivity.getWindow().setAttributes(params);
                popupWindow.dismiss();
            }
        });

        LinearLayout mLlSave = (LinearLayout) popwindow_view.findViewById(R.id.ll_save);
        LinearLayout mLlCancel = (LinearLayout) popwindow_view.findViewById(R.id.ll_cancel);

        mLlSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(url);
                popupWindow.dismiss();
            }
        });

        mLlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }


}