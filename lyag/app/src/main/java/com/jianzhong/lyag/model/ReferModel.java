package com.jianzhong.lyag.model;

/**
 * Created by zhengwencheng on 2018/2/1 0001.
 * package com.jianzhong.bs.model
 */

public class ReferModel extends BaseModel {
    private String cover;
    private String title;
    private String duration_sec;
    private String my_study_sec;
    private String total_ep;
    private String achieve;

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

    public String getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(String duration_sec) {
        this.duration_sec = duration_sec;
    }

    public String getMy_study_sec() {
        return my_study_sec;
    }

    public void setMy_study_sec(String my_study_sec) {
        this.my_study_sec = my_study_sec;
    }

    public String getTotal_ep() {
        return total_ep;
    }

    public void setTotal_ep(String total_ep) {
        this.total_ep = total_ep;
    }

    public String getAchieve() {
        return achieve;
    }

    public void setAchieve(String achieve) {
        this.achieve = achieve;
    }
}
