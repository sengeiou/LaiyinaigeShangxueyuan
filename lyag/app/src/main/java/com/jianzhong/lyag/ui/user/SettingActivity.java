package com.jianzhong.lyag.ui.user;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.baselib.util.AppUtils;
import com.baselib.util.PreferencesUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.util.CacheDataManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jianzhong.lyag.util.MediaPlayerUtils.mMediaPlayer;

/**
 * 设置页面
 * Created by zhengwencheng on 2018/3/30 0030.
 * package com.jianzhong.bs.ui.user
 */

public class SettingActivity extends ToolbarActivity {

    @BindView(R.id.tv_cache)
    TextView mTvCache;
    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("设置");
        showHeadTitle();

        initController();
    }

    @SuppressLint("SetTextI18n")
    private void initController(){
        mTvVersion.setText("V"+ AppUtils.getVersionName(mContext));
        try {
            mTvCache.setText(CacheDataManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ll_revise_pwd, R.id.ll_clear_cache, R.id.ll_quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_revise_pwd:
                Intent intent = new Intent(mContext,RevisePwdActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_clear_cache:
                new AlertDialog.Builder(SettingActivity.this).setTitle("提示").
                        setMessage("是否清除商学院的缓存?(不包含已缓存的视频)").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new clearCache()).start();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();
                break;
            case R.id.ll_quit:
                new AlertDialog.Builder(SettingActivity.this).setTitle("提示").
                        setMessage("确定要退出当前账号吗?").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                unbindPushTag();
//                                quit();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();

                break;
        }
    }


    class clearCache implements Runnable {

        @Override
        public void run() {
            try {
                CacheDataManager.clearAllCache(SettingActivity.this);
                Thread.sleep(3000);
                if (CacheDataManager.getTotalCacheSize(SettingActivity.this).startsWith("0")) {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                return;
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtils.show(SettingActivity.this,"清理完成");
                    try {
                        mTvCache.setText(CacheDataManager.getTotalCacheSize(SettingActivity.this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    };


    /**
     * 退出当前账号
     */
    private void quit() {
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        PreferencesUtils.putInt(mContext, AppConstants.KEY_IS_LOGIN,0);
        PreferencesUtils.putString(mContext,AppConstants.KEY_PWD,"");
        startActivity(new Intent(mContext, LoginActivity.class));
//        ActivityManager.getActivityManager().finishActivity(MainActivity.class);
    }

    /**
     * 解绑推送
     */
    private void unbindPushTag() {
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.removeAlias(null, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFailed(String s, String s1) {
            }
        });
        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                quit();
            }

            @Override
            public void onFailed(String s, String s1) {
            }
        });
    }

}
