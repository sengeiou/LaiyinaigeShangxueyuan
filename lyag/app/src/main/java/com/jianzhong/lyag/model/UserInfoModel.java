package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 用户信息model
 * Created by zhengwencheng on 2018/1/18 0018.
 * package com.jianzhong.bs.model
 */

public class UserInfoModel extends BaseModel {
    private String user_id;
    private String realname;
    private String sex;
    private String mobile;
    private String ac_point; //总学分
    private String avatar;
    private String branch_id;//分支id
    private String pos_id; //职位id
    private String is_branch_head; //是否部门负责人
    private String is_expert; // 是否课程专家
    private String summary;
    private String is_frozen; //是否用户被冻结
    private String motion_group_id;
    private String birth;
    private String address;
    private String create_at;
    private String expire_at;
    private MotionModel motion;
    private List<UserTagModel> user_tag;
    private String branch_name; //登录接口没有返回的
    private String pos_duty;    //职位
    private String pos_name;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAc_point() {
        return ac_point;
    }

    public void setAc_point(String ac_point) {
        this.ac_point = ac_point;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getPos_id() {
        return pos_id;
    }

    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
    }

    public String getIs_branch_head() {
        return is_branch_head;
    }

    public void setIs_branch_head(String is_branch_head) {
        this.is_branch_head = is_branch_head;
    }

    public String getIs_expert() {
        return is_expert;
    }

    public void setIs_expert(String is_expert) {
        this.is_expert = is_expert;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIs_frozen() {
        return is_frozen;
    }

    public void setIs_frozen(String is_frozen) {
        this.is_frozen = is_frozen;
    }

    public String getMotion_group_id() {
        return motion_group_id;
    }

    public void setMotion_group_id(String motion_group_id) {
        this.motion_group_id = motion_group_id;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getExpire_at() {
        return expire_at;
    }

    public void setExpire_at(String expire_at) {
        this.expire_at = expire_at;
    }

    public MotionModel getMotion() {
        return motion;
    }

    public void setMotion(MotionModel motion) {
        this.motion = motion;
    }

    public List<UserTagModel> getUser_tag() {
        return user_tag;
    }

    public void setUser_tag(List<UserTagModel> user_tag) {
        this.user_tag = user_tag;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getPos_duty() {
        return pos_duty;
    }

    public void setPos_duty(String pos_duty) {
        this.pos_duty = pos_duty;
    }

    public String getPos_name() {
        return pos_name;
    }

    public void setPos_name(String pos_name) {
        this.pos_name = pos_name;
    }
}
