package com.jianzhong.lyag.model;

/**
 * 代表每日推荐块
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class FocusModel extends BaseModel{
    private String title;
    private String total_ep;
    private String update_day;
    private String now_ep;
    private String play_num;
    private String cover;
    private String expert_user_id;
    private ExpertModel expert;
    private String focus_type;
    private String foreign_id;
    private String duration_sec;

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

    public String getUpdate_day() {
        return update_day;
    }

    public void setUpdate_day(String update_day) {
        this.update_day = update_day;
    }

    public String getNow_ep() {
        return now_ep;
    }

    public void setNow_ep(String now_ep) {
        this.now_ep = now_ep;
    }

    public String getPlay_num() {
        return play_num;
    }

    public void setPlay_num(String play_num) {
        this.play_num = play_num;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getExpert_user_id() {
        return expert_user_id;
    }

    public void setExpert_user_id(String expert_user_id) {
        this.expert_user_id = expert_user_id;
    }

    public ExpertModel getExpert() {
        return expert;
    }

    public void setExpert(ExpertModel expert) {
        this.expert = expert;
    }

    public String getFocus_type() {
        return focus_type;
    }

    public void setFocus_type(String focus_type) {
        this.focus_type = focus_type;
    }

    public String getForeign_id() {
        return foreign_id;
    }

    public void setForeign_id(String foreign_id) {
        this.foreign_id = foreign_id;
    }

    public String getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(String duration_sec) {
        this.duration_sec = duration_sec;
    }
}
