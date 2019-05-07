package com.jianzhong.lyag.model;

/**
 * Created by zhengwencheng on 2018/3/29 0029.
 * package com.jianzhong.bs.model
 */

public class TaskRecordSubModel extends BaseModel {
    private String task_id;
    private String title;

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
}
