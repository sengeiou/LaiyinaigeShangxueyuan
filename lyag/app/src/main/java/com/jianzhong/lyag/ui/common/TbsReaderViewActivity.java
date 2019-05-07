package com.jianzhong.lyag.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 通用文件显示类
 * Created by Administrator on 2017/11/24.
 */

public class TbsReaderViewActivity extends ToolbarActivity {

    @BindView(R.id.ll_content)
    LinearLayout mLlContent;

    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "title";

    private TbsReaderView mTbsReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tbs_reader_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        String url = intent.getStringExtra(KEY_URL);
        String title = intent.getStringExtra(KEY_TITLE);

        setHeadTitle(title);
        showHeadTitle();
        showCommonLoading();
        initTbsReaderView();

        OkGo.get(url)
                .tag(this)
                .execute(new FileCallback() {  //文件下载时，可以指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        hideCommonLoading();
                        showFile(file.getPath());
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调下载进度(该回调在主线程,可以直接更新ui)
                    }
                });

    }

    /**
     * 初始化TbsReaderView
     */
    private void initTbsReaderView() {
        mTbsReaderView = new TbsReaderView(this, new TbsReaderView.ReaderCallback() {
            @Override
            public void onCallBackAction(Integer integer, Object o, Object o1) {

            }
        });
        mLlContent.addView(mTbsReaderView, new LinearLayout.LayoutParams(-1, -1));
    }

    /**
     * 显示文件
     *
     * @param filePath
     */
    public void showFile(String filePath) {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", filePath);
        bundle.putString("tempPath", Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");
        try {
            boolean bool = mTbsReaderView.preOpen(getFileType(filePath), false);
            if (bool) {
                mTbsReaderView.openFile(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 获取文件类型
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";
        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = paramString.substring(i + 1);
        return str;
    }

}
