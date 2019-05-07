package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 专栏详情数据model
 * Created by zhengwencheng on 2018/3/1 0001.
 * package com.jianzhong.bs.model
 */

public class ColumnDetailModel extends BaseModel {
    private String column_id;
    private String title;
    private String total_ep;
    private String now_ep;
    private String brief;
    private String summary;
    private String expert_user_id;
    private String cover;
    private String play_num;
    private ExpertModel expert;
    private List<ColumnCourseModel> course;
    private String has_favor;
    private String asset_type;
    private String cover_in;

    public String getHas_favor() {
        return has_favor;
    }

    public void setHas_favor(String has_favor) {
        this.has_favor = has_favor;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getColumn_id() {
        return column_id;
    }

    public void setColumn_id(String column_id) {
        this.column_id = column_id;
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

    public String getNow_ep() {
        return now_ep;
    }

    public void setNow_ep(String now_ep) {
        this.now_ep = now_ep;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getExpert_user_id() {
        return expert_user_id;
    }

    public void setExpert_user_id(String expert_user_id) {
        this.expert_user_id = expert_user_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPlay_num() {
        return play_num;
    }

    public void setPlay_num(String play_num) {
        this.play_num = play_num;
    }

    public ExpertModel getExpert() {
        return expert;
    }

    public void setExpert(ExpertModel expert) {
        this.expert = expert;
    }

    public List<ColumnCourseModel> getCourse() {
        return course;
    }

    public void setCourse(List<ColumnCourseModel> course) {
        this.course = course;
    }

    public String getCover_in() {
        return cover_in;
    }

    public void setCover_in(String cover_in) {
        this.cover_in = cover_in;
    }
}
