package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 登录信息model
 * Created by zhengwencheng on 2018/1/18 0018.
 * package com.jianzhong.bs.model
 */

public class LoginInfoModel extends BaseModel {
    private String access_token;
    private String rc_token;
    private UserInfoModel user_info_all;
    private List<BlockModel> all_block; //所有模块的隐藏显示

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public UserInfoModel getUser_info_all() {
        return user_info_all;
    }

    public void setUser_info_all(UserInfoModel user_info_all) {
        this.user_info_all = user_info_all;
    }

    public List<BlockModel> getAll_block() {
        return all_block;
    }

    public void setAll_block(List<BlockModel> all_block) {
        this.all_block = all_block;
    }

    public String getRc_token() {
        return rc_token;
    }

    public void setRc_token(String rc_token) {
        this.rc_token = rc_token;
    }
}
