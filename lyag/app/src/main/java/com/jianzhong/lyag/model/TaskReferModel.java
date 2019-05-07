package com.jianzhong.lyag.model;

/**
 * 通关任务[子任务]参考资料 数据列表model
 * Created by zhengwencheng on 2018/2/1 0001.
 * package com.jianzhong.bs.model
 */

public class TaskReferModel extends BaseModel {

    private String foreign_id;
    private String course_id;
    private String asset_type;
    private String cover;
    private String title;
    private String total_ep;
    private String finish_ep;
    private String total_study_sec;
    private String duration_sec;
    private String create_at;

    public String getForeign_id() {
        return foreign_id;
    }

    public void setForeign_id(String foreign_id) {
        this.foreign_id = foreign_id;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal_ep() {
        return total_ep;
    }

    public void setTotal_ep(String total_ep) {
        this.total_ep = total_ep;
    }

    public String getFinish_ep() {
        return finish_ep;
    }

    public void setFinish_ep(String finish_ep) {
        this.finish_ep = finish_ep;
    }

    public String getTotal_study_sec() {
        return total_study_sec;
    }

    public void setTotal_study_sec(String total_study_sec) {
        this.total_study_sec = total_study_sec;
    }

    public String getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(String duration_sec) {
        this.duration_sec = duration_sec;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
}
