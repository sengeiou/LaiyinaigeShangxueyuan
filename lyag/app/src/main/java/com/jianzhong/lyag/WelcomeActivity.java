package com.jianzhong.lyag;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import com.baselib.util.GsonUtils;
import com.baselib.util.PreferencesUtils;
import com.baselib.util.Result;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.LoginInfoModel;
import com.jianzhong.lyag.model.UserInfoModel;
import com.jianzhong.lyag.ui.user.LoginActivity;
import com.jianzhong.lyag.util.DialogHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 欢迎页
 * Created by zhengwencheng on 2018/1/18 0018.
 * package com.jianzhong.bs
 */

public class WelcomeActivity extends ToolbarActivity {

    @BindView(R.id.ll_item)
    LinearLayout mLlItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLlItem.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void initData() {
        super.initData();

        new Handler().postDelayed(r, 2000);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent;
            if (PreferencesUtils.getInt(mContext, AppConstants.KEY_IS_FIRST, 0) == 0) { //第一次进来app 预留给app介绍
                PreferencesUtils.putInt(mContext, AppConstants.KEY_IS_FIRST, 1);
                intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            } else {
                if (PreferencesUtils.getInt(mContext, AppConstants.KEY_IS_LOGIN, 0) == 1) { //上次登录没有退出 将自动登录
                    login();
                } else {
                    intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }
    };

    /**
     * 登录
     */
    private void login() {

        Map<String, Object> params = new HashMap<>();
        params.put("mobile", PreferencesUtils.getString(mContext, AppConstants.KEY_MOBILE));
        params.put("password", PreferencesUtils.getString(mContext, AppConstants.KEY_PWD));

        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_LOGIN, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {

                Result<LoginInfoModel> result = GsonUtils.json2Bean(s, LoginInfoModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    PreferencesUtils.putString(mContext, AppConstants.KEY_ACCESS_TOKEN, result.getData().getAccess_token());
                    AppUserModel.getInstance().setAll_block(result.getData().getAll_block());
                    AppUserModel.getInstance().setmLoginInfoModel(result.getData());
                    getUserInfo();
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage():"数据解析错误");
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 获取用户资料
     */
    private void getUserInfo() {
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_USER_INFO, null, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                DialogHelper.dismissDialog();
                Result<UserInfoModel> result = GsonUtils.json2Bean(s, UserInfoModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    AppUserModel.getInstance().setmUserModel(result.getData());
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                } else {
                    ToastUtils.show(mContext, result.getMessage());
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
