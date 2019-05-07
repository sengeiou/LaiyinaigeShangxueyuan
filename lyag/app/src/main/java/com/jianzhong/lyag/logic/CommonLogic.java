package com.jianzhong.lyag.logic;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.model.UserModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;
import com.jianzhong.lyag.ui.exam.ColumnDetailActivity;
import com.jianzhong.lyag.ui.interact.HelpDetailActivity;
import com.jianzhong.lyag.ui.interact.ShareDetailActivity;
import com.jianzhong.lyag.ui.live.LiveDetailActivity;
import com.jianzhong.lyag.ui.notice.NoticeDetailActivity;

/**
 * Created by Administrator on 2018/4/25.
 */

public class CommonLogic {

    private CommonLogic(){}

    /**
     * 推送的跳转
     * @param mContext
     * @param assetType
     * @param foreignId
     */
    public static void startActivityPush(Context mContext, String assetType, String foreignId) {
        startActivityPush(mContext, assetType, foreignId, false);
    }

    /**
     * 推送的跳转
     * @param mContext
     * @param assetType
     * @param foreignId
     * @param isNewTask
     */
    public static void startActivityPush(Context mContext, String assetType, String foreignId, boolean isNewTask) {
        // notice, course, column, live, help, share
        Intent intent = null;
        if (assetType.equals(AppConstants.TAG_NOTICE)) {
            intent = new Intent(mContext, NoticeDetailActivity.class);
            intent.putExtra("notice_id", foreignId);
        } else if (assetType.equals(AppConstants.TAG_COURSE)) {
            intent = new Intent(mContext, ClassDetailActivity.class);
            intent.putExtra("course_id", foreignId);
        } else if (assetType.equals(AppConstants.TAG_COLUMN)) {
            intent = new Intent(mContext, ColumnDetailActivity.class);
            intent.putExtra("column_id", foreignId);
        } else if (assetType.equals(AppConstants.TAG_LIVE)) {
            intent = new Intent(mContext, LiveDetailActivity.class);
            intent.putExtra("live_id", foreignId);
        } else if (assetType.equals(AppConstants.TAG_HELP)) {
            intent = new Intent(mContext, HelpDetailActivity.class);
            intent.putExtra("help_id", foreignId);
        } else if (assetType.equals(AppConstants.TAG_SHARE)) {
            intent = new Intent(mContext, ShareDetailActivity.class);
            intent.putExtra("share_id", foreignId);
        }

        if (intent != null) {
            // 推送的必须设置全新的栈
            if (isNewTask) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            mContext.startActivity(intent);
        }

    }

    /**
     * 设置用户名字、职位、公司
     * @param userModel
     * @return
     */
    public static String getIdentify(UserModel userModel) {
        String indentify = "";
        if (userModel != null) {
            if (!TextUtils.isEmpty(userModel.getRealname()) && !TextUtils.isEmpty(userModel.getPos_name()) && !TextUtils.isEmpty(userModel.getBranch_name())) {
                indentify = userModel.getRealname() + " | " + userModel.getPos_name() + " | " + userModel.getBranch_name();
            } else if (TextUtils.isEmpty(userModel.getRealname()) && !TextUtils.isEmpty(userModel.getPos_name()) && !TextUtils.isEmpty(userModel.getBranch_name())) {
                indentify = userModel.getPos_name() + " | " + userModel.getBranch_name();
            } else if (!TextUtils.isEmpty(userModel.getRealname()) && TextUtils.isEmpty(userModel.getPos_name()) && !TextUtils.isEmpty(userModel.getBranch_name())) {
                indentify = userModel.getRealname() + " | " + userModel.getBranch_name();
            } else if (!TextUtils.isEmpty(userModel.getRealname()) && !TextUtils.isEmpty(userModel.getPos_name()) && TextUtils.isEmpty(userModel.getBranch_name())) {
                indentify = userModel.getRealname() + " | " + userModel.getPos_name();
            }
        }
        return indentify;
    }

}
