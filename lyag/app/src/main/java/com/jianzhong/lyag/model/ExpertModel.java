package com.jianzhong.lyag.model;

/**
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class ExpertModel extends BaseModel {
    private String realname;
    private String pos_duty;
    private String user_id;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPos_duty() {
        return pos_duty;
    }

    public void setPos_duty(String pos_duty) {
        this.pos_duty = pos_duty;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
