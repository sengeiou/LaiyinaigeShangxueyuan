package com.jianzhong.lyag.util;

import com.baselib.util.DateUtils;
import com.baselib.util.ListUtils;
import com.jianzhong.lyag.model.CommonMenuModel;
import com.lzy.imagepicker.bean.ImageItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.baselib.util.DateUtils.LONG_DATE_FORMAT;

/**
 * Created by zhengwencheng on 2018/3/12 0012.
 * package com.jianzhong.bs.util
 */

public class CommonMethodLogic {


    /**
     * 添加菜单栏按钮选项
     * @return
     */
    public static List<CommonMenuModel> getShareInteractMenu() {
        List<CommonMenuModel> list = new ArrayList();
        CommonMenuModel model;

        model = new CommonMenuModel();
        model.setIcon(0);
        model.setTitle("发布求助");
        list.add(model);

        model = new CommonMenuModel();
        model.setIcon(0);
        model.setTitle("发布分享");
        list.add(model);

        return list;
    }

    /**
     * 添加菜单栏按钮选项
     * @return
     */
    public static List<CommonMenuModel> getShareMenu() {
        List<CommonMenuModel> list = new ArrayList();
        CommonMenuModel model;

        model = new CommonMenuModel();
        model.setIcon(0);
        model.setTitle("编辑");
        list.add(model);

        model = new CommonMenuModel();
        model.setIcon(0);
        model.setTitle("删除");
        list.add(model);

        return list;
    }

    /**
     * 拍照获取图片回来初始化
     * @param imageItemList
     */
    public static void initImageItemStatus(List<ImageItem> imageItemList) {
        if (!ListUtils.isEmpty(imageItemList)) {
            for (int i = 0; i < imageItemList.size(); i++) {
                imageItemList.get(i).mimeType = "";
                imageItemList.get(i).name = "";
            }
        }
    }

    /**
     * 把符合日期格式的字符串转换为日期类型
     *
     * @param dateStr
     * @return
     */
    public static Date stringtoDate(String dateStr, String format) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            d = formater.parse(dateStr);
        } catch (Exception e) {
            // log.error(e);
            d = null;
        }
        return d;
    }

    /**
     * 直播开始时间
     * @param date
     * @return
     */
    public static String getLiveStartAt(String date) {
        long type = DateUtils.dayDiffCurr(CommonUtils.timeStampToDate(date, LONG_DATE_FORMAT));
        long now = Calendar.getInstance().getTimeInMillis();
        String temp;
        if (type == 0) {
            temp = "今天 " + CommonUtils.timeStampToDate(date, "HH:mm");
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
                temp = CommonUtils.timeStampToDate(date, "MM-dd HH:mm");
            } else {
                temp = CommonUtils.timeStampToDate(date, "yyyy-MM-dd HH:mm");
            }

        }
        return temp;
    }
}
