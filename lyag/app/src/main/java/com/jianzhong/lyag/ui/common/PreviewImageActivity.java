package com.jianzhong.lyag.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.widget.photoview.PhotoView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 查看附件
 * Created by zhengwencheng on 2017/3/13.
 * package com.jianzhong.ys.ui.customer
 */

public class PreviewImageActivity extends ToolbarActivity {


    @BindView(R.id.photo_view)
    PhotoView mPhotoView;

    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ORIGINAL_URL = "original_url";

    private String url;
    private String title;
    private String originalUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        url = intent.getStringExtra(KEY_URL);
        originalUrl = intent.getStringExtra(KEY_ORIGINAL_URL);
        title = intent.getStringExtra(KEY_TITLE);

        setHeadTitle(title);
        showHeadTitle();

        if (!TextUtils.isEmpty(originalUrl) && originalUrl.length() > 0){
            GlideUtils.load(mPhotoView, originalUrl);
        }else {
            GlideUtils.load(mPhotoView, url);
        }

        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupWindow(mActivity, view, url);
                return false;
            }
        });
    }

    public void saveImage(String url) {
        OkGo.get(url)
                .tag(this)
                .execute(new FileCallback() {  //文件下载时，可以指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        // file 即为文件数据，文件保存在指定目录
                        ToastUtils.show(mContext, "保存成功");
                        // 最后通知图库更新
                        try {
                            MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(file);
                        intent.setData(uri);
                        mContext.sendBroadcast(intent);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        ToastUtils.show(mContext, "保存失败");
                    }

                });
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
