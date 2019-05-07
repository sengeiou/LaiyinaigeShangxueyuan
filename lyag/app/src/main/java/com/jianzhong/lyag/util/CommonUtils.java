package com.jianzhong.lyag.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.util.ActivityManager;
import com.baselib.util.DateUtils;
import com.baselib.util.DeviceInfoUtil;
import com.baselib.util.JSONUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.RandomUtils;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.BaseActivity;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.listener.OnDateListener;
import com.jianzhong.lyag.model.ContactsModel;
import com.jianzhong.lyag.model.CoursewareModel;
import com.jianzhong.lyag.model.DepartmentModel;
import com.jianzhong.lyag.model.ExpertModel;
import com.jianzhong.lyag.model.TagModel;
import com.jianzhong.lyag.ui.user.LoginActivity;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import net.bither.util.imagecompressor.XCImageCompressor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 通用的逻辑
 * Created by chenzhikai
 * Email 1310741158@qq.com
 */
public class CommonUtils {

    /**
     * 九宫格图片
     */
    public static void NineGridViewPicShow(Context context, List<ImageInfo> fileModels, NineGridView mNineGridView) {
        mNineGridView.setMaxSize(fileModels.size());
        mNineGridView.setAdapter(new NineGridViewClickAdapter(context, fileModels));
    }

    public static String getExpertDontent(List<ExpertModel> data){
        String dontent = "";
        if(!ListUtils.isEmpty(data)){
            for (int i = 0; i < data.size(); i++) {
                dontent = dontent + data.get(i).getRealname()+"/"+data.get(i).getPos_duty()+"\n";
            }
        }
        return (!StringUtils.isEmpty(dontent) ? dontent.substring(0,dontent.length()-1):"");
    }

    /**
     * 输入的宽高是图片的分辨率 div是要将屏幕分成几段
     *
     * @param mWidth
     * @param mHeight
     * @param div
     * @return
     */
    public static ViewGroup.LayoutParams getImageLayoutParams(Context context, ImageView mImageView, int mWidth, int mHeight, int div, int margin) {
        /**获取屏幕宽度 设计图片的高度*/
        WindowManager wm = ((BaseActivity) context).getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        //获取控件现在的布局参数对象
        int mScreenWidth = dm.widthPixels - DeviceInfoUtil.dip2px(context, margin);// 获取屏幕分辨率宽度 减去的20dp是布局中margin的
        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
        //图片的分辨率为mWidth x mHeight
        layoutParams.height = ((mScreenWidth / div) * mHeight) / mWidth;
        return layoutParams;
    }

    /**
     * 获取指派的时候部门、经销商以及成员
     * @param departmentModels
     * @param anecnyModels
     * @param memberModels
     * @return
     */
    public static String getAssignObject(List<DepartmentModel> departmentModels, List<DepartmentModel> anecnyModels,List<DepartmentModel> mPostModels, List<ContactsModel> memberModels){
        String assignStr = "";

        if(!ListUtils.isEmpty(departmentModels)){
            if(departmentModels.size() > 1){
                assignStr = assignStr+ departmentModels.size()+"个部门、";
            }else {
                assignStr = assignStr+ departmentModels.get(0).getBranch_name()+"、";
            }
        }

        if(!ListUtils.isEmpty(anecnyModels)){
            if(anecnyModels.size() > 1){
                assignStr = assignStr+ anecnyModels.size()+"个经销商、";
            }else {
                assignStr = assignStr+ anecnyModels.get(0).getBranch_name()+"、";
            }

        }

        if(!ListUtils.isEmpty(mPostModels)){
            if(mPostModels.size() > 1){
                assignStr = assignStr+ mPostModels.size()+"个岗位、";
            }else {
                assignStr = assignStr+ mPostModels.get(0).getBranch_name()+"、";
            }
        }

        if(!ListUtils.isEmpty(memberModels)){
            if(memberModels.size() == 1){
                assignStr = assignStr + memberModels.get(0).getRealname();
            }else {
                assignStr = assignStr + memberModels.size()+"个员工、";
            }

        }

        return "已选择："+ (!StringUtils.isEmpty(assignStr) ? assignStr.substring(0,assignStr.length()-1):"");
    }
    
    /**
     * 获取一下checkBox的宽度
     *
     * @param mCb
     * @return
     */
    public static int measureCheckWidth(CheckBox mCb) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        mCb.measure(w, h);
        return mCb.getMeasuredWidth();
    }

    /**
     * 获取一下LinearLayaout的宽度
     *
     * @param ll
     * @return
     */
    public static int measureLlWidth(LinearLayout ll) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        ll.measure(w, h);
        return ll.getMeasuredWidth();
    }

    /**
     * 输入的宽高是图片的分辨率 div是要将屏幕分成几段
     *
     * @param mWidth
     * @param mHeight
     * @param div
     * @return
     */
    public static ViewGroup.LayoutParams getImageLayoutParamsForViewPager(Context context, ImageView mImageView, int mWidth, int mHeight, int div) {
        /**获取屏幕宽度 设计图片的高度*/
        WindowManager wm = ((BaseActivity) context).getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        //获取控件现在的布局参数对象
        int mScreenWidth = dm.widthPixels;
        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
        //图片的分辨率为mWidth x mHeight
        layoutParams.height = ((mScreenWidth / div) * mHeight) / mWidth;
        return layoutParams;
    }

    /**
     * 获取一个当前的时间
     *
     * @return
     */
    public static String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * 获取年月日
     *
     * @return
     */
    public static String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
    }

    /**
     * 判断网络是否有效
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show(context, "网络异常，请检查网络是否连通");
            return false;
        }
        return false;
    }

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public static String formatDate(String date) {
        if (!StringUtils.isEmpty(date)) {
            if (date.length() <= 10) {
                return DateUtils.getTimeLeft(Long.parseLong(date + "000"));
            } else {
                return DateUtils.getTimeLeft(Long.parseLong(date));
            }
        }
        return "";
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String timeStampToDate(String date, String format) {
        if (!StringUtils.isEmpty(date) && !date.equals("0")) {
            if (date.length() <= 10) {
                return DateUtils.timeStampToDate(Long.parseLong(date + "000"), format);
            } else {
                return DateUtils.timeStampToDate(Long.parseLong(date), format);
            }
        }
        return "";
    }

    /**
     * 将时间格式化为多少天前
     *
     * @param date
     * @return
     */
    public static String tranTimeToString(String date) {
        if (!StringUtils.isEmpty(date) && !date.equals("0")) {
            if (date.length() <= 10) {
                return DateUtils.getTimeLeft(Long.parseLong(date + "000"));
            } else {
                return DateUtils.getTimeLeft(Long.parseLong(date));
            }
        }
        return "";
    }


    public static long getDayDiffCurr(String date) {
        if (!StringUtils.isEmpty(date) && !date.equals("0")) {
            if (date.length() <= 10) {
                return DateUtils.dayDiffCurr(Long.parseLong(date + "000") + "");
            } else {
                return DateUtils.dayDiffCurr(Long.parseLong(date) + "");
            }
        }
        return 0;
    }

    /**
     * 两个时间比较
     *
     * @param time
     * @return
     */
    public static long getTimeCompare(String time) {
        String t1 = CommonUtils.timeStampToDate(time, DateUtils.FORMAT_TWO);
        String t2 = CommonUtils.timeStampToDate(new Date().getTime() + "", DateUtils.FORMAT_TWO);
        long result = DateUtils.timeCompare(t1, t2);
        return result;
    }

    /**
     * 格式化时间，显示今天、明天、后天，其它时间。
     *
     * @param date
     * @param format
     * @return
     */
    public static String getDistanceDays(String date, String format) {
        long type = DateUtils.dayDiffCurr(timeStampToDate(date, DateUtils.LONG_DATE_FORMAT));
        String temp;
        if (type == 0) {
            temp = "今天 " + timeStampToDate(date, "HH:mm");
        } else if (type == -1) {
            temp = "明天 " + timeStampToDate(date, "HH:mm");
        } else if (type == -2) {
            temp = "后天 " + timeStampToDate(date, "HH:mm");
        } else {
            temp = timeStampToDate(date, format);
        }
        return temp;
    }

    /**
     * 1)当前1小时内显示：N分钟前 / N 分钟后
     * 2)当前6小时内显示：N小时前 / N小时后
     * 3)最近3天处理：明天 HH:NN / 今天HH:NN / 昨天HH:NN
     * 4)本年内：MM-DD HH:NN
     * 5)其他：YYYY-MM-DD HH:NN
     *
     * @param date
     * @return
     */
    public static String getTimeActivity(String date) {
        int diff;
        int t = 60;
        int cache;
        long type = DateUtils.dayDiffCurr(timeStampToDate(date, DateUtils.LONG_DATE_FORMAT));
        long now = Calendar.getInstance().getTimeInMillis();
        if (date.length() <= 10) {
            cache = (int) ((now - Long.parseLong(date + "000")) / 1000);
            diff = Math.abs(cache);
        } else {
            cache = (int) ((now - Long.parseLong(date)) / 1000);
            diff = Math.abs(cache);
        }
        String temp;
        if (type == 0) {
            if (diff < t) { // 1分钟内
                if (cache >= 0) {  // 加了个=号 by jam
                    temp = "1分钟前";
                } else {
                    temp = "1分钟后";
                }
            } else if (diff < (60 * 60)) { // 1小时内
                if (cache > 0) {
                    temp = (int) (diff / 60) + "分钟前";
                } else {
                    temp = (int) (diff / 60) + "分钟后";
                }
            } else if (diff > (60 * 60) && diff < (60 * 360)) { // 1小时上 6小时下
                if (cache > 0) {
                    temp = (int) (diff / 3600) + "小时前";
                } else {
                    temp = (int) (diff / 3600) + "小时后";
                }
            } else {
                temp = "今天 " + timeStampToDate(date, "HH:mm");
            }
        } else if (type == -1) {
            temp = "明天 " + timeStampToDate(date, "HH:mm");
        } else if (type == -2) {
            temp = "后天 " + timeStampToDate(date, "HH:mm");
        } else {
            SimpleDateFormat f = new SimpleDateFormat("yyyy");
            String nowYear = f.format(new Date(now));
            String mYear;
            if (date.length() <= 10) {
                mYear = f.format(new Date(Long.parseLong(date + "000")));
            } else {
                mYear = f.format(new Date(Long.parseLong(date)));
            }
            if (nowYear.equals(mYear)) {
                temp = timeStampToDate(date, "MM-dd HH:mm");
            } else {
                temp = timeStampToDate(date, "yyyy-MM-dd HH:mm");
            }

        }
        return temp;
    }

    /**
     * 如果是今天返回时分 不是今天看看是否是今年 今年就显示月日 如果不是就yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getDryTime(String date) {
        long type = DateUtils.dayDiffCurr(timeStampToDate(date, DateUtils.LONG_DATE_FORMAT));
        long now = Calendar.getInstance().getTimeInMillis();
        String temp;
        if (type == 0) {
            temp = "今天 " + timeStampToDate(date, "HH:mm");
        } else {
            SimpleDateFormat f = new SimpleDateFormat("yyyy");
            String nowYear = f.format(new Date(now));
            String mYear;
            if (date.length() <= 10) {
                mYear = f.format(new Date(Long.parseLong(date + "000")));
            } else {
                mYear = f.format(new Date(Long.parseLong(date)));
            }
            if (nowYear.equals(mYear)) {
                temp = timeStampToDate(date, "MM-dd");
            } else {
                temp = timeStampToDate(date, "yyyy-MM-dd");
            }

        }
        return temp;
    }

    public static String getCustomerFollowDays(String date, String format) {
        long type = DateUtils.dayDiffCurr(timeStampToDate(date, DateUtils.LONG_DATE_FORMAT));

        String temp;
        if (type == 0) {
            temp = "今天 ";
        } else if (type == 1) {
            temp = "昨天 ";
        } else {
            temp = timeStampToDate(date, format);
        }
        return temp;
    }

    public static long getDistanceDaysType(String date) {
        long type = DateUtils.dayDiffCurr(timeStampToDate(date, DateUtils.LONG_DATE_FORMAT));

        return type;
    }

    /**
     * 把字符串转成数字，然后+1
     *
     * @param count
     * @return
     */
    public static int getCountAddOne(String count) {
        if (!StringUtils.isEmpty(count)) {
            return Integer.parseInt(count) + 1;
        }
        return 0;
    }

    /**
     * 把字符串转成数字，然后-1
     *
     * @param count
     * @return
     */
    public static int getCountDeleteOne(String count) {
        if (!StringUtils.isEmpty(count) && Integer.parseInt(count) >= 1) {
            return Integer.parseInt(count) - 1;
        }
        return 0;
    }

    /**
     * string按下划线分割后获取第二个
     *
     * @param str
     * @return
     */
    public static String getStringSplitTwo(String str) {
        String result;
        if (!StringUtils.isEmpty(str)) {
            String[] temp = str.split("_");
            if (temp.length > 1) {
                result = temp[1];
                return result;
            }
        }
        return "";
    }

    /**
     * 检查登录是否成功
     *
     * @param json
     * @return
     */
    public static boolean checkLogin(String json) {
        int temp = JSONUtils.getInt(json, "code", 0);
        if (temp == 101 || temp == 102 || temp == 103 || temp == 104) {
            ActivityManager.getActivityManager().finishAllActivity();
            Intent intent = new Intent(BaseApplication.getInstance().getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseApplication.getInstance().getApplicationContext().startActivity(intent);
            return false;
        }
        return true;
    }

    /**
     * 拨打电话
     *
     * @param mActivity
     * @param mobile
     */

    public static void phone(Activity mActivity, String mobile) {
        Uri data = Uri.parse("tel:" + mobile);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(data);
        mActivity.startActivity(intent);
    }

    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public static void doSendSMSTo(Activity mActivity, String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            mActivity.startActivity(intent);
        }
    }

    /**
     * 选择日期
     *
     * @param et
     */
    public static void getDate(Activity activity, final TextView et) {
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setTitleStringId("选择日期")
                .setType(Type.YEAR_MONTH_DAY)
                .setCyclic(false)
                .setThemeColor(activity.getResources().getColor(R.color.color_bg_main))
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        String date = DateUtils.timeStampToDate(millseconds, "yyyy-MM-dd");
                        et.setText(date);
                    }
                }).build();

        mDialogAll.show(((AppCompatActivity) activity).getSupportFragmentManager(), "YEAR_MONTH_DAY");
    }

    /**
     * 选择日期
     *
     * @param et
     */
    public static void getTime(Activity activity, final TextView et) {
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setTitleStringId("选择日期")
                .setType(Type.ALL)
                .setCyclic(false)
                .setThemeColor(activity.getResources().getColor(R.color.color_bg_main))
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        String date = DateUtils.timeStampToDate(millseconds, "yyyy-MM-dd HH:mm");
                        et.setText(date);
                    }
                }).build();

        mDialogAll.show(((AppCompatActivity) activity).getSupportFragmentManager(), "YEAR_MONTH_DAY");
    }

    /**
     * 选择日期
     *
     * @param activity
     * @param listener
     */
    public static void getDate(Activity activity, OnDateSetListener listener) {
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setTitleStringId("选择日期")
                .setType(Type.YEAR_MONTH_DAY)
                .setCyclic(false)
                .setMinMillseconds(957009943)
                .setThemeColor(activity.getResources().getColor(R.color.color_bg_main))
                .setCallBack(listener).build();

        mDialogAll.show(((AppCompatActivity) activity).getSupportFragmentManager(), "YEAR_MONTH_DAY");
    }

    /**
     * 选择日期，时间添加无限
     *
     * @param activity
     * @param et
     */
    public static void getCustomTime(final Activity activity, final TextView et) {
        long currentMillseconds = new Date().getTime();
        if (et != null && et.getTag() != null) {
            Date date = DateUtils.stringtoDate(et.getTag().toString(), DateUtils.FORMAT_TWO);
            if (date != null) {
                currentMillseconds = date.getTime();
            }
        } else if (et != null && !StringUtils.isEmpty(et.getText().toString())) {
            Date date = DateUtils.stringtoDate(et.getText().toString(), DateUtils.FORMAT_TWO);
            if (date != null) {
                currentMillseconds = date.getTime();
            }
        } else {
            String dateStr = DateUtils.timeStampToDate(currentMillseconds, "yyyy-MM-dd") + " 23:59";
            Date date = DateUtils.stringtoDate(dateStr, DateUtils.FORMAT_TWO);
            currentMillseconds = date.getTime();
        }
        com.jianzhong.lyag.widget.pickerview.TimePickerDialog mDialogAll = new com.jianzhong.lyag.widget.pickerview.TimePickerDialog.Builder()
                .setTitleStringId("选择日期")
                .setType(Type.ALL)
                .setCyclic(false)
                .setCurrentMillseconds(currentMillseconds)
                .setThemeColor(activity.getResources().getColor(R.color.color_bg_main))
                .setCallBack(new com.jianzhong.lyag.widget.pickerview.OnDateSetListener() {
                    @Override
                    public void onDateSet(com.jianzhong.lyag.widget.pickerview.TimePickerDialog timePickerView, long millseconds, int currentHour) {
                        if (currentHour == 0) {
                            String date = DateUtils.timeStampToDate(millseconds, "yyyy-MM-dd") + " 23:59";
                            et.setTag(date);
                            et.setText(date);
                        } else {
                            String date = DateUtils.timeStampToDate(millseconds, "yyyy-MM-dd HH:mm");
                            et.setTag(date);
                            et.setText(date);
                        }
                    }
                }).build();

        mDialogAll.show(((AppCompatActivity) activity).getSupportFragmentManager(), "YEAR_MONTH_DAY");
    }

    /**
     * 图片压缩工具类
     *
     * @param selImageList
     * @param imageCompressListener
     */
    public static void compress(List<ImageItem> selImageList, XCImageCompressor.ImageCompressListener imageCompressListener) {
        if (!ListUtils.isEmpty(selImageList)) {
            final List<String> srcFilePathList = new ArrayList<>();
            final List<String> outputFilePathList = new ArrayList<>();
            for (int i = 0; i < selImageList.size(); i++) {
                srcFilePathList.add(selImageList.get(i).path);
                outputFilePathList.add(FileUtils.getPathImageCompress() + RandomUtils.getRandomNumbersAndLetters(15) + System.currentTimeMillis() + "_new.jpg");
            }
            XCImageCompressor.compress(srcFilePathList, outputFilePathList, imageCompressListener);
        }
    }

    /**
     * 回调一个日期
     *
     * @param activity
     * @param onDateListener
     */
    public static void getDateCallBack(Activity activity, final OnDateListener onDateListener) {
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setTitleStringId("选择日期")
                .setType(Type.YEAR_MONTH_DAY)
                .setCyclic(false)
                .setThemeColor(activity.getResources().getColor(R.color.color_bg_main))
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        String date = DateUtils.timeStampToDate(millseconds, "yyyy-MM-dd");
                        onDateListener.onDateCallback(date);
                    }
                }).build();

        mDialogAll.show(((AppCompatActivity) activity).getSupportFragmentManager(), "YEAR_MONTH_DAY");
    }

    /**
     * 获取性别列表
     *
     * @return
     */
    public static List<String> getSexList() {
        List list = new ArrayList();
        list.add("男");
        list.add("女");
        return list;
    }

    /**
     * 获取默认图表列表
     *
     * @return
     */
    public static List<Float> getDefaultChartList() {
        List<Float> list = new ArrayList();
        list.add(0f);
        list.add(0f);
        list.add(0f);
        list.add(0f);
        list.add(0f);
        list.add(0f);
        return list;
    }

    /**
     * 点击查看图片(单图的)
     *
     * @param context
     * @param image
     */
    public static void startImagePreviewActivity(Context context, String image) {
        if (StringUtils.isEmpty(image)) return;

        List<ImageInfo> imageInfo = new ArrayList<>();
        ImageInfo info = new ImageInfo();
        info.setBigImageUrl(image);
        info.setThumbnailUrl(image);
        imageInfo.add(info);

        Intent intent = new Intent(context, ImagePreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 点击查看图片(单图的)
     *
     * @param context
     * @param imageList
     */
    public static void startImagePreviewActivity(Context context, List<CoursewareModel> imageList, int index) {
        if (ListUtils.isEmpty(imageList)) return;

        List<ImageInfo> imageInfo = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            ImageInfo info = new ImageInfo();
            info.setBigImageUrl(imageList.get(i).getWare_url());
            info.setThumbnailUrl(imageList.get(i).getWare_url());
            imageInfo.add(info);
        }

        Intent intent = new Intent(context, ImagePreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 获取当前时间，多少个小时后的整点。
     *
     * @param hour 小时单位
     * @return
     */
    public static String getTimeByHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);
        calendar.set(Calendar.MINUTE, 0);
        return DateUtils.dateToString(calendar.getTime(), DateUtils.FORMAT_TWO);
    }

    /**
     * 验证微信是否安装
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 打开微信
     *
     * @param context
     */
    public static void startWeixin(Context context) {
        if (isWeixinAvilible(context)) {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } else {
            ToastUtils.show(context, "您还没有安装微信或没有权限！");
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isPhoneNumberValid(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、170、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/
        String telRegex = "[1][3456789]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 获得两个日期之间的所有月份
     *
     * @param minDate
     * @param maxDate
     * @return
     */
    public static List<Integer> getMonthBetween(String minDate, String maxDate) {
        ArrayList<Integer> result = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();

            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

            Calendar curr = min;
            while (curr.before(max)) {
                result.add(curr.get(Calendar.MONTH));
//                result.add(sdf.format(curr.getTime()));
                curr.add(Calendar.MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 一分钟的秒数
     */
    private static final int MINUTE_SECOND = 60;
    private static final int HOUR_SECOND = 3600;

    public static String secToTime2(int second) {
        if (second <= 0) {
            return "00:00";
        }

        int minutes = second / MINUTE_SECOND;
        if (minutes > 0) {

            second -= minutes * MINUTE_SECOND;
        }

        return (minutes > 10 ? (minutes + "") : ("0" + minutes)) + ":"
                + (second > 10 ? (second + "") : ("0" + second));
    }

    /**
     * 计时
     * @param second
     * @return
     */
    public static String timimg(int second) {
        if (second <= 0) {
            return "00:00:00";
        }

        int hour = second / HOUR_SECOND;

        if(hour > 0){
            second -= hour * HOUR_SECOND;
        }

        int minutes = second / MINUTE_SECOND;
        if (minutes > 0) {

            second -= minutes * MINUTE_SECOND;
        }


        return (hour >= 10 ? (hour + "") : ("0" + hour)) +":"+(minutes >= 10 ? (minutes + "") : ("0" + minutes)) + ":"
                + (second >= 10 ? (second + "") : ("0" + second));
    }

    /**
     * 将秒转换成时分秒显示
     *
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr;
        int hour;
        int minute;
        int second;
        if (time < 60) {
            return time +"秒";
        }else {
            minute = time / 60;
            second = time % 60;
            timeStr = minute+"分钟" + (second != 0 ? second+"秒":"");
//            if (minute < 60) {
//                second = time % 60;
//                timeStr = minute+"分钟" + (second != 0 ? second+"秒":"");
//            } else {
//                hour = minute / 60;
//                minute = minute % 60;
//                second = time - hour * 3600 - minute * 60;
//                timeStr = hour + "小时" + (minute != 0 ? minute+"分":"") + (second != 0 ? second+"秒":"");
//            }
        }
        Log.e("timeStr------", timeStr);
        return timeStr;
    }

    /**
     * 标签的字符串
     * @param mList
     * @return
     */
    public static String getTagStr(List<TagModel> mList){
        String str = "";
        if(!ListUtils.isEmpty(mList)){
            for (int i = 0; i < mList.size(); i++) {
                if(i == 0){
                    str = str + mList.get(i).getTag_name();
                }else {
                    str = str + "、" +mList.get(i).getTag_name();
                }
            }
        }
        return str;
    }

    /**
     * string转int
     *
     * @param str
     * @return
     */
    public static int parseInt(String str) {
        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
