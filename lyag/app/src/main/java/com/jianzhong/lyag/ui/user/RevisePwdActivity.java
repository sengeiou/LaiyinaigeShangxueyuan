package com.jianzhong.lyag.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import com.baselib.util.ActivityManager;
import com.baselib.util.GsonUtils;
import com.baselib.util.PreferencesUtils;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.util.DialogHelper;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 密码修改
 * Created by zhengwencheng on 2017/4/27.
 * package com.jianzhong.ys.ui.user.setting
 */

public class RevisePwdActivity extends ToolbarActivity {

    @BindView(R.id.et_old_pwd)
    EditText mEtOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText mEtNewPwd;
    @BindView(R.id.et_pwd_again)
    EditText mEtPwdAgain;

    private String oldPwd;
    private String newPwd;
    private String rePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_pwd);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();
        setHeadTitle("修改密码");
        showHeadTitle();
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        revisePwd();
    }

    /**
     * 修改密码
     */
    private void revisePwd(){
        oldPwd = mEtOldPwd.getText().toString().trim();
        newPwd = mEtNewPwd.getText().toString().trim();
        rePwd = mEtPwdAgain.getText().toString().trim();

        if(StringUtils.isBlank(oldPwd)){
            ToastUtils.show(this, "请输入当前密码！");
            return;
        }
        if(newPwd.length() < 6){
            ToastUtils.show(this, "新密码不能少于6位！");
            return;
        }
        if(!StringUtils.isEquals(newPwd, rePwd)){
            ToastUtils.show(this, "两次输入的密码不一样！");
            return;
        }

        DialogHelper.showLoadingDialog(this);
        Map<String,Object> params = new HashMap<>();
        params.put("password_old", oldPwd);
        params.put("password_new", newPwd);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_PWD_UPDATE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    PreferencesUtils.putInt(mContext, AppConstants.KEY_IS_LOGIN,0);
                    ActivityManager.getActivityManager().finishAllActivity();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                }
                DialogHelper.dismissLoadingDialog();
                ToastUtils.show(mContext, result.getMessage());
            }

            @Override
            public void onFailure(String msg) {
                DialogHelper.dismissLoadingDialog();
                ToastUtils.show(mContext, msg);
            }
        });



    }

}
