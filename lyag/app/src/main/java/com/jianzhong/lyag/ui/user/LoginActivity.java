package com.jianzhong.lyag.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.baselib.util.ActivityManager;
import com.baselib.util.GsonUtils;
import com.baselib.util.LogUtil;
import com.baselib.util.PreferencesUtils;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.MainActivity;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.LoginInfoModel;
import com.jianzhong.lyag.model.UserInfoModel;
import com.jianzhong.lyag.util.DialogHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录页
 * Created by zhengwencheng on 2018/1/12 0012.
 * package com.jianzhong.bs.ui
 */

public class LoginActivity extends ToolbarActivity {

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.ll_item)
    LinearLayout mLlItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

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

    @OnClick({R.id.tv_forget_pwd, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_pwd: //忘记密码
                Intent intent = new Intent(mContext, ForgetPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login: //登录
                login();
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        if (StringUtils.isEmpty(mEtPhone.getText().toString())) {
            ToastUtils.show(mContext, "请输入手机号码");
        } else if (StringUtils.isEmpty(mEtPassword.getText().toString())) {
            ToastUtils.show(mContext, "请输入密码");
        } else {
            DialogHelper.showDialog(mContext);
            Map<String, Object> params = new HashMap<>();
            params.put("mobile", mEtPhone.getText().toString());
            params.put("password", mEtPassword.getText().toString());

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
                        DialogHelper.dismissDialog();
                        ToastUtils.show(mContext, result != null ? result.getMessage() : "数据解析错误");
                    }
                }

                @Override
                public void onFailure(String msg) {
                    DialogHelper.dismissDialog();
                    ToastUtils.show(mContext, msg);
                }
            });
        }
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
                    bindPushTag(result.getData().getUser_id());
                    PreferencesUtils.putInt(mContext, AppConstants.KEY_IS_LOGIN, 1);
                    PreferencesUtils.putString(mContext, AppConstants.KEY_MOBILE, mEtPhone.getText().toString().trim());
                    PreferencesUtils.putString(mContext, AppConstants.KEY_PWD, mEtPassword.getText().toString().trim());
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } else {
                    ToastUtils.show(mContext, result.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                DialogHelper.dismissDialog();
                ToastUtils.show(mContext, msg);
            }
        });
    }

    private long preTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - preTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            preTime = System.currentTimeMillis();
        } else {
            ActivityManager.getActivityManager().finishAllActivity();
            // 杀掉进程，退出系统
        }
    }

    /**
     * 绑定推送
     * @param userId 用户ID
     */
    private void bindPushTag(String userId) {
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.addAlias(userId, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFailed(String s, String s1) {
            }
        });
        pushService.bindAccount(userId, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                LogUtil.e("绑定推送的TAG", s);
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtil.e("绑定推送的TAG", s + s1);
            }
        });
    }

}
