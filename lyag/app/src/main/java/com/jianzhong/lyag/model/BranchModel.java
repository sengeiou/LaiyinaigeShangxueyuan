package com.jianzhong.lyag.model;

/**
 * Created by zhengwencheng on 2018/3/6 0006.
 * package com.jianzhong.bs.model
 */

public class BranchModel extends BaseModel {
    private String branch_path;
    private String branch_id;
    private String is_dealer;
    private String pid;
    private String branch_name;
    public String getBranch_path() {
        return branch_path;
    }

    public void setBranch_path(String branch_path) {
        this.branch_path = branch_path;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getIs_dealer() {
        return is_dealer;
    }

    public void setIs_dealer(String is_dealer) {
        this.is_dealer = is_dealer;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }
}
