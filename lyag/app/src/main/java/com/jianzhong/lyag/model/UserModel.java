package com.jianzhong.lyag.model;

/**
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.model
 */

public class UserModel extends BaseModel {
    private String realname;
    private String user_id;
    private String avatar;
    private String pos_id;
    private String branch_id;
    private PosModel pos;
    private BranchModel branch;
    private String branch_path;
    private String branch_name;
    private String pos_name;

    public String getPos_id() {
        return pos_id;
    }

    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public PosModel getPos() {
        return pos;
    }

    public void setPos(PosModel pos) {
        this.pos = pos;
    }

    public BranchModel getBranch() {
        return branch;
    }

    public void setBranch(BranchModel branch) {
        this.branch = branch;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAvatar() {
        return avatar;
    }


    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBranch_path() {
        return branch_path;
    }

    public void setBranch_path(String branch_path) {
        this.branch_path = branch_path;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getPos_name() {
        return pos_name;
    }

    public void setPos_name(String pos_name) {
        this.pos_name = pos_name;
    }
}
