package com.jianzhong.lyag.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.baselib.util.GsonUtils;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.util.DialogHelper;
import com.jianzhong.lyag.widget.customview.CountDownButton;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 忘记密码页面
 * Created by zhengwencheng on 2018/1/18 0018.
 * package com.jianzhong.bs.ui.user
 */
public class ForgetPwdActivity extends ToolbarActivity {

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.btn_get_code)
    Button mBtnGetCode;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_password_again)
    EditText mEtPasswordAgain;
    @BindView(R.id.ll_item)
    LinearLayout mLlItem;

    private CountDownButton countDownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_pwd);
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

        // 倒计时按扭
        countDownButton = new CountDownButton();
        countDownButton.init(mContext, mBtnGetCode);
    }

    @OnClick({R.id.btn_get_code, R.id.btn_back, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_code:
                getMsgCode();
                break;
            case R.id.btn_back:
                ForgetPwdActivity.this.finish();
                break;
            case R.id.btn_confirm:
                resetPwd();
                break;
        }
    }

    /**
     * 获取短信验证码
     */
    private void getMsgCode() {
        if (StringUtils.isEmpty(mEtPhone.getText().toString())) {
            ToastUtils.show(mContext, "请输入手机号码");
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("mobile", mEtPhone.getText().toString());
            HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_MSG_CODE, params, new HttpCallBack() {
                @Override
                public void onSuccess(String s) {
                    Result result = GsonUtils.json2Bean(s, Result.class);
                    if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                        // 开起Button倒计时
                        countDownButton.start();
                    } else {
                        ToastUtils.show(mContext, result.getMessage());
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.show(mContext, msg);
                }
            });
        }
    }

    /**
     * 确认重置密码
     */
    private void resetPwd() {
        if (StringUtils.isEmpty(mEtPhone.getText().toString())) {
            ToastUtils.show(mContext, "请输入手机号码");
        } else if (StringUtils.isEmpty(mEtCode.getText().toString())) {
            ToastUtils.show(mContext, "请输入验证码");
        } else if (StringUtils.isEmpty(mEtPassword.getText().toString())) {
            ToastUtils.show(mContext, "请输入新密码");
        } else if (StringUtils.isEmpty(mEtPasswordAgain.getText().toString())) {
            ToastUtils.show(mContext, "请再次输入新密码");
        } else if (!mEtPassword.getText().toString().equals(mEtPasswordAgain.getText().toString())) {
            ToastUtils.show(mContext, "新密码前后出入不一致");
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("mobile",mEtPhone.getText().toString());
            params.put("sms_code",mEtCode.getText().toString());
            params.put("new_password",mEtPassword.getText().toString());
            params.put("confirm_password",mEtPasswordAgain.getText().toString());
            DialogHelper.showDialog(mContext);
            HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_FORGET_PWD, params, new HttpCallBack() {
                @Override
                public void onSuccess(String s) {
                    DialogHelper.dismissDialog();
                    Result result = GsonUtils.json2Bean(s, Result.class);
                    if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                        ForgetPwdActivity.this.finish();
                    } else {
                        ToastUtils.show(mContext, result.getMessage());
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.show(mContext, msg);
                }
            });
        }
    }
}
