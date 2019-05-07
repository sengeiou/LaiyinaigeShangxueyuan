package com.jianzhong.lyag.alipush;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.baselib.util.GsonUtils;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.logic.CommonLogic;
import com.jianzhong.lyag.model.PushMsgModel;
import com.jianzhong.lyag.util.SystemUtlis;

import java.util.Map;

/**
 * Created by zhengwencheng on 2018/4/21 0021.
 * package com.jianzhong.lyag.alipush
 */

public class LyagMessageReceiver extends MessageReceiver {

    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";

    private PushMsgModel item;

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        Log.e("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
        item = GsonUtils.jsonToBean(GsonUtils.toJson(extraMap), PushMsgModel.class);
        if (item == null) return;
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Log.e("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        //判断app进程是否存活
        if (SystemUtlis.isAppLive(context, AppConstants.KEY_PUSH_PACKAGE)) {
            item = GsonUtils.jsonToBean(extraMap, PushMsgModel.class);
            if (item != null){
                CommonLogic.startActivityPush(context, item.getAsset_type(), item.getForeign_id() + "", true);
            }
        } else { //app不存活
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(AppConstants.KEY_PUSH_PACKAGE);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            Bundle args = new Bundle();
            args.putString(AppConstants.KEY_PUSH_ASSET_TYPE, item.getAsset_type());
            args.putString(AppConstants.KEY_PUSH_FOREIGN_ID, item.getForeign_id() + "");
            launchIntent.putExtra(AppConstants.KEY_PUSH_MSG, args);
            context.startActivity(launchIntent);
        }
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("MyMessageReceiver", "onNotificationRemoved");
    }
}
