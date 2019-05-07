package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 标识首页的专栏块
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class ColumnModel extends BaseModel {

    private String column_id;
    private String course_id;

    private List<ColumnContentModel> content;

    public String getColumn_id() {
        return column_id;
    }

    public void setColumn_id(String column_id) {
        this.column_id = column_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public List<ColumnContentModel> getContent() {
        return content;
    }

    public void setContent(List<ColumnContentModel> content) {
        this.content = content;
    }
}
