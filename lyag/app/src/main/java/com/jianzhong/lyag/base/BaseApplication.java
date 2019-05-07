package com.jianzhong.lyag.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alivc.player.AliVcMediaPlayer;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpUtils;
import com.jianzhong.lyag.util.FileUtils;
import com.jianzhong.lyag.util.ImagePickerUtils;
import com.jianzhong.lyag.util.NineImageLoader;
import com.jianzhong.lyag.util.PreferenceUtil;
import com.lzy.ninegrid.NineGridView;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.tencent.smtt.sdk.QbSdk;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import io.rong.imkit.RongIM;

/**
 * Created by zhengwencheng on 2017/2/7.
 * package com.jianzhong.ys.base
 */
public class BaseApplication extends Application {
    private static final String TAG = "Init";
    public static DownloadManager mDownloadManager;
    private static BaseApplication baseApplication;

    public static BaseApplication getInstance() {
        return baseApplication;
    }

    public BaseApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        baseApplication = this;
        HttpUtils.getInstance().init(baseApplication);
        PreferenceUtil.init(baseApplication);
        // 设置九宫格图片加载图片
        NineGridView.setImageLoader(new NineImageLoader());
        // 初始化图片选择器
        ImagePickerUtils.initImagePicker();
        // 创建文件目录
        FileUtils.makeDirs(baseApplication);
        // 二维码
        ZXingLibrary.initDisplayOpinion(baseApplication);
        // 百度统计-打开调试开关，正式版本请关闭，以免影响性能
//        StatService.setDebugOn(false);
        // 百度统计-打开异常收集开关，默认收集java层异常，如果有嵌入SDK提供的so库，则可以收集native crash异常
//        StatService.setOn(application, StatService.EXCEPTION_LOG);
        //初始化阿里云播放器
        AliVcMediaPlayer.init(getApplicationContext());
        GroupVarManager.getInstance().initOSS();
        //融云的初始化 正式key：e0x9wycfe4grq  测试key：82hegw5u8yksx
        RongIM.init(baseApplication, "82hegw5u8yksx");

        mDownloadManager = DownloadService.getDownloadManager();
        mDownloadManager.setTargetFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/lyag/");

        //阿里云推送初始化
        initCloudChannel(baseApplication);
        // 初始化X5浏览器
        QbSdk.initX5Environment(baseApplication, null);

        // 监听Activity生命周期
        registerActivityLifecycle();
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    /**
     * 监听Activity生命周期
     */
    private void registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(TAG, "onActivityCreated: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(TAG, "onActivityStarted: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(TAG, "onActivityResumed: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(TAG, "onActivityPaused: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(TAG, "onActivityStopped: " + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(TAG, "onActivitySaveInstanceState: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(TAG, "onActivityDestroyed: " + activity.getLocalClassName());
            }
        });
    }

}
