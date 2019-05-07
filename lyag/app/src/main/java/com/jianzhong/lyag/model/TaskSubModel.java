package com.jianzhong.lyag.model;

/**
 * 子学习任务列表数据model
 * Created by zhengwencheng on 2018/2/1 0001.
 * package com.jianzhong.bs.model
 */

public class TaskSubModel extends BaseModel {
    private String task_id;
    private String title;
    private String pid;
    private String top_task_id;
    private String is_pass;
    private String pass_str;

    public String getPass_str() {
        return pass_str;
    }

    public void setPass_str(String pass_str) {
        this.pass_str = pass_str;
    }

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTop_task_id() {
        return top_task_id;
    }

    public void setTop_task_id(String top_task_id) {
        this.top_task_id = top_task_id;
    }

    public String getIs_pass() {
        return is_pass;
    }

    public void setIs_pass(String is_pass) {
        this.is_pass = is_pass;
    }
}
