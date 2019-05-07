package com.jianzhong.lyag.model;

/**
 * create by zhengwencheng on 2018/3/23 0023
 * package com.jianzhong.bs.model
 */
public class RecordCommitModel extends BaseModel {
    private String finish_course_id;
    private String finish_column_id;

    public String getFinish_course_id() {
        return finish_course_id;
    }

    public void setFinish_course_id(String finish_course_id) {
        this.finish_course_id = finish_course_id;
    }

    public String getFinish_column_id() {
        return finish_column_id;
    }

    public void setFinish_column_id(String finish_column_id) {
        this.finish_column_id = finish_column_id;
    }
}
