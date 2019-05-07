package com.jianzhong.lyag.model;

/**
 * 课件列表数据model
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.model
 */

public class CoursewareModel extends BaseModel {
    private String ware_id;     //课件id
    private String ware_url;    //课件地址
    private String course_id;
    private String sort;        //排序
    private String create_at;   //创建时间
    private String is_del;      //是否删除

    public String getWare_id() {
        return ware_id;
    }

    public void setWare_id(String ware_id) {
        this.ware_id = ware_id;
    }

    public String getWare_url() {
        return ware_url;
    }

    public void setWare_url(String ware_url) {
        this.ware_url = ware_url;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }
}
