package com.jianzhong.lyag.model;

/**
 * 成长通关记录数据model
 * Created by zhengwencheng on 2018/3/29 0029.
 * package com.jianzhong.bs.model
 */

public class TaskRecordModel extends BaseModel {
    private String task_id;
    private String is_pass;
    private String create_at;
    private TaskRecordSubModel task;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getIs_pass() {
        return is_pass;
    }

    public void setIs_pass(String is_pass) {
        this.is_pass = is_pass;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public TaskRecordSubModel getTask() {
        return task;
    }

    public void setTask(TaskRecordSubModel task) {
        this.task = task;
    }
}
