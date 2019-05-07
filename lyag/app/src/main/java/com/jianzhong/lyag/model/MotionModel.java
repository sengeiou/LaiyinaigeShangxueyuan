package com.jianzhong.lyag.model;

import java.util.List;

/**
 * Created by zhengwencheng on 2018/1/20 0020.
 * package com.jianzhong.bs.model
 */

public class MotionModel extends BaseModel {
    private List<String> operation;
//    private String user_range;
    private String is_branch_head;

    public List<String> getOperation() {
        return operation;
    }

    public void setOperation(List<String> operation) {
        this.operation = operation;
    }

//    public String getUser_range() {
//        return user_range;
//    }
//
//    public void setUser_range(String user_range) {
//        this.user_range = user_range;
//    }

    public String getIs_branch_head() {
        return is_branch_head;
    }

    public void setIs_branch_head(String is_branch_head) {
        this.is_branch_head = is_branch_head;
    }
}
