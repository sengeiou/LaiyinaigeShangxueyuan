package com.jianzhong.lyag.model;

/**
 * 通关考试首页必修、选修课属性model
 * Created by zhengwencheng on 2018/2/1 0001.
 * package com.jianzhong.bs.model
 */

public class TaskListModel extends BaseModel {
    private String task_id;
    private String title;
    private String task_type;
    private String sub_num;
    private String is_pass;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public String getSub_num() {
        return sub_num;
    }

    public void setSub_num(String sub_num) {
        this.sub_num = sub_num;
    }

    public String getIs_pass() {
        return is_pass;
    }

    public void setIs_pass(String is_pass) {
        this.is_pass = is_pass;
    }
}
