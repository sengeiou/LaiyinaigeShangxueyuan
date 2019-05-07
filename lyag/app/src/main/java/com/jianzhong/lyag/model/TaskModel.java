package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 通关考试model
 * Created by zhengwencheng on 2018/2/1 0001.
 * package com.jianzhong.bs.model
 */

public class TaskModel extends BaseModel {
    private List<TaskListModel> base;
    private List<TaskListModel> optional;

    public List<TaskListModel> getBase() {
        return base;
    }

    public void setBase(List<TaskListModel> base) {
        this.base = base;
    }

    public List<TaskListModel> getOptional() {
        return optional;
    }

    public void setOptional(List<TaskListModel> optional) {
        this.optional = optional;
    }
}
