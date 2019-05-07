package com.jianzhong.lyag.model;

/**
 * 已学习 或者未阅读成员列表
 * Created by zhengwencheng on 2018/3/6 0006.
 * package com.jianzhong.bs.model
 */

public class MemberConModel extends BaseModel {

    private String user_id;
    private String avatar;
    private String realname;
    private String branch_id;
    private String pos_id;
    private BranchModel branch;
    private PosModel pos;

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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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

    public BranchModel getBranch() {
        return branch;
    }

    public void setBranch(BranchModel branch) {
        this.branch = branch;
    }

    public PosModel getPos() {
        return pos;
    }

    public void setPos(PosModel pos) {
        this.pos = pos;
    }
}
