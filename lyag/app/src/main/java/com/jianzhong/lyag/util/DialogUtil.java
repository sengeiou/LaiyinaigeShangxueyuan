package com.jianzhong.lyag.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.livecloud.live.AlivcMediaFormat;
import com.baselib.util.DeviceInfoUtil;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.CommonWebActivity;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.listener.OnItemsSelectInterface;
import com.jianzhong.lyag.listener.OnScoreListener;
import com.jianzhong.lyag.model.LiveDetailModel;
import com.jianzhong.lyag.ui.live.livepush.LiveCameraActivity;


/**
 * Dialog管理类
 * <p>
 * Created by zhengwencheng on 2017/2/13.
 * package com.jianzhong.ys.util
 */

public class DialogUtil {

    //window管理
    private static WindowManager windowManager;
    //display对象
    private static Display display;

    private static DialogUtil dialogUtil = null;

    /**
     * 获取单例
     *
     * @return
     */
    public static DialogUtil getInstance() {
        if (dialogUtil == null) {
            synchronized (DialogUtil.class) {
                if (dialogUtil == null) {
                    dialogUtil = new DialogUtil();
                }
            }
        }
        return dialogUtil;
    }

    /**
     * 获取屏幕宽、高信息
     */
    public static void getDisplayInfo(Activity activity) {
        windowManager = activity.getWindowManager();
        display = windowManager.getDefaultDisplay();
    }

    public AlertDialog showExamFail(final Activity activity, final OnItemsSelectInterface listener){
        getDisplayInfo(activity);
        final AlertDialog dialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        //设置窗体的宽高
//        lp.height = display.getHeight();
        lp.width = (int) (display.getWidth() * 0.85);

        window.setAttributes(lp);
        //设置Dialog布局
        window.setContentView(R.layout.dialog_exam_fail);

        ImageView iv_confirm = (ImageView) window.findViewById(R.id.iv_confirm);

        iv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                listener.OnClick(0);
            }
        });

        return dialog;
    }

    /**
     * 成功通关
     * @param activity
     * @param title
     * @param listener
     * @return
     */
    public AlertDialog showExamRight(final Activity activity,String title, final OnItemsSelectInterface listener){
        getDisplayInfo(activity);
        final AlertDialog dialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        //设置窗体的宽高
//        lp.height = display.getHeight();
        lp.width = (int) (display.getWidth() * 0.85);

        window.setAttributes(lp);
        //设置Dialog布局
        window.setContentView(R.layout.dialog_exam_right);

        ImageView iv_confirm = (ImageView) window.findViewById(R.id.iv_confirm);
        TextView tv_msg = (TextView) window.findViewById(R.id.tv_msg);
        tv_msg.setText(title+"考试");
        iv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                listener.OnClick(0);
            }
        });

        return dialog;
    }

    /**
     * 学习完之后的课程评价
     * @param activity
     * @param listener
     * @return
     */
    @SuppressLint("SetTextI18n")
    public AlertDialog showCourseComment(final Activity activity , final OnScoreListener listener){
        getDisplayInfo(activity);
        final AlertDialog dialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        //设置窗体的宽高
//        lp.height = display.getHeight();
        lp.width = (int) (display.getWidth() * 0.85);

        window.setAttributes(lp);
        //设置Dialog布局
        window.setContentView(R.layout.dialog_course_comment);

        TextView tv_confirm = window.findViewById(R.id.tv_confirm);
        TextView tv_cnecel = window.findViewById(R.id.tv_cancel);
        final TextView tv_score = window.findViewById(R.id.tv_score);
        final RatingBar ratingbar_course = window.findViewById(R.id.ratingbar_course);
        tv_score.setText((ratingbar_course.getRating()*2)+"分");

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                listener.onScoreCallback((ratingbar_course.getRating()*2)+"");
            }
        });

        tv_cnecel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ratingbar_course.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tv_score.setText(rating*2+"分");
            }
        });

        return dialog;
    }

    /**
     * 直播显示tip
     *
     * @param activity
     * @param data
     * @return
     */
    public AlertDialog showLiveTip(final Activity activity, final String onlineUser, final LiveDetailModel data) {
        getDisplayInfo(activity);
        final AlertDialog dialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        //设置窗体的宽高
//        lp.height = display.getHeight();
        lp.width = (int) (display.getWidth() * 0.85);

        window.setAttributes(lp);
        //设置Dialog布局
        window.setContentView(R.layout.dialog_live_tip);

        final EditText mEtContent = window.findViewById(R.id.et_content);
        TextView mTvStart = window.findViewById(R.id.tv_start);
        TextView mTvCopy = window.findViewById(R.id.tv_copy);
        TextView mTvChcek = window.findViewById(R.id.tv_check);
        TextView mTvCancel = window.findViewById(R.id.tv_cancel);

        mEtContent.setText(data.getPush_url());

        mTvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                initLive(data,activity);
            }
        });

        mTvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", mEtContent.getText().toString());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);

                ToastUtils.show(activity,"直播地址已复制");
            }
        });

        mTvChcek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, CommonWebActivity.class);
                intent.putExtra("title", "直播说明");
                intent.putExtra("url", HttpConfig.WEB_URL_BASE + HttpConfig.URL_LIVE_INTRODUCE);
                activity.startActivity(intent);
            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    /**
     * 初始化直播的参数
     */
    private void initLive(LiveDetailModel data, Activity mActivity) {
//        data.setPush_url("rtmp://video-center.alivecdn.com/lovica/Stream2?vhost=live.lovica.com.cn&auth_key=1521200826-0-0-91e0c5c8c376a3a177432bf805cbb726");
        int videoResolution;
        int cameraFrontFacing;
        videoResolution = AlivcMediaFormat.OUTPUT_RESOLUTION_540P; //默认设置540p
        cameraFrontFacing = AlivcMediaFormat.CAMERA_FACING_BACK;   //默认后置摄像头
        boolean screenOrientation = true;       //默认设置横屏
        if (StringUtils.isEmpty(data.getPush_url())) {
            ToastUtils.show(mActivity, "推流地址为空");
            return;
        }

        String watermark = data.getCover(); //水印图片
        int dx = 14; //水平和垂直的margin
        int dy = 14;
        int site = 1; //水印图片位置
        int minBitrate = 500;
        int maxBitrate = 800;
        int bestBitrate = 600;
        int initBitrate = 600;
        int watermarkWidth = 50;
        int watermarkHeight;
//        BitmapFactory.Options options = loadWatermarkInfo(watermark);
//        if(options == null) {
//            return;
//        }
        watermarkHeight = DeviceInfoUtil.getScreenWidth(mActivity);
        int frameRate = 30;

        LiveCameraActivity.RequestBuilder builder = new LiveCameraActivity.RequestBuilder()
                .bestBitrate(bestBitrate)
                .cameraFacing(cameraFrontFacing)
                .dx(dx)
                .dy(dy)
                .site(site)
                .rtmpUrl(data.getPush_url())
                .videoResolution(videoResolution)
                .portrait(screenOrientation)
                .watermarkUrl(watermark)
                .minBitrate(minBitrate)
                .maxBitrate(maxBitrate)
                .frameRate(frameRate)
                .watermarkWidth(watermarkWidth)
                .watermarkHeight(watermarkHeight)
                .initBitrate(initBitrate)
                .onlineUser(data.getOnline_user_num())
                .live_id(data.getLive_id());


        LiveCameraActivity.startActivity(mActivity, builder);
    }
}
