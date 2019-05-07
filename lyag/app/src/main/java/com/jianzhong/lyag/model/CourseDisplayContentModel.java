package com.jianzhong.lyag.model;

import java.util.List;

/**
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class CourseDisplayContentModel extends BaseModel {
    private String top_cat_id; //精品课程分类的id
    private String cat_name;    //精品课程分类的名称
    private List<CourseListModel> detail;

    public String getTop_cat_id() {
        return top_cat_id;
    }

    public void setTop_cat_id(String top_cat_id) {
        this.top_cat_id = top_cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public List<CourseListModel> getDetail() {
        return detail;
    }

    public void setDetail(List<CourseListModel> detail) {
        this.detail = detail;
    }
}
