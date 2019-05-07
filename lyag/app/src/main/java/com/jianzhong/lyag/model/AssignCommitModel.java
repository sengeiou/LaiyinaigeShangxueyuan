package com.jianzhong.lyag.model;

import java.util.HashSet;

/**
 * Created by zhengwencheng on 2018/3/13 0013.
 * package com.jianzhong.bs.model
 */

public class AssignCommitModel {

    private HashSet<String> branch;
    private HashSet<String> user;

    public HashSet<String> getBranch() {
        return branch;
    }

    public void setBranch(HashSet<String> branch) {
        this.branch = branch;
    }

    public HashSet<String> getUser() {
        return user;
    }

    public void setUser(HashSet<String> user) {
        this.user = user;
    }
}
