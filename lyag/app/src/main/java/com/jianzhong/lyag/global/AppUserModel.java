package com.jianzhong.lyag.global;

import android.content.Context;

import com.jianzhong.lyag.model.BlockModel;
import com.jianzhong.lyag.model.LoginInfoModel;
import com.jianzhong.lyag.model.UserInfoModel;

import java.util.List;

/**
 * App的UserModel
 * Created by zhengwencheng on 2018/1/18.
 * package com.jianzhong.bs.model
 */
public class AppUserModel {

    private volatile static AppUserModel appUserModel = null;
    // 用户信息
    private UserInfoModel mUserInfoModel;
    private LoginInfoModel mLoginInfoModel;
    //
    private List<BlockModel> all_block;
    //
    private Context mContext;

    private AppUserModel() {
    }

    public static AppUserModel getInstance() {
        if (null == appUserModel) {
            appUserModel = new AppUserModel();
        }
        return appUserModel;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public LoginInfoModel getmLoginInfoModel() {
        return mLoginInfoModel;
    }

    public void setmLoginInfoModel(LoginInfoModel mLoginInfoModel) {
        this.mLoginInfoModel = mLoginInfoModel;
    }

    public UserInfoModel getmUserModel() {
        return mUserInfoModel;
    }

    public void setmUserModel(UserInfoModel mUserInfoModel) {
        this.mUserInfoModel = mUserInfoModel;
    }

    public List<BlockModel> getAll_block() {
        return all_block;
    }

    public void setAll_block(List<BlockModel> all_block) {
        this.all_block = all_block;
    }
}
