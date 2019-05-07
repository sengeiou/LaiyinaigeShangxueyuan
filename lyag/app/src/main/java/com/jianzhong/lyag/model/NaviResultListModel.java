package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 导航列表数据适配器
 * Created by zhengwencheng on 2018/1/29 0029.
 * package com.jianzhong.bs.model
 */

public class NaviResultListModel extends BaseModel {
    private String title;
    private String asset_id;
    private String cover;
    private String play_num;
    private String expert_user_id;
    private String duration_min;
    private String duration_sec;
    private ExpertModel expert;
    private String create_at;
    private String total_ep;
    private String update_day;
    private String now_ep;
    private String start_at;
    private List<TagModel> tag;
    private String is_publish;
    private String online_user_num;
    private String brief;
    private String is_finish;
    public List<TagModel> getTag() {
        return tag;
    }

    public void setTag(List<TagModel> tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
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

    public String getExpert_user_id() {
        return expert_user_id;
    }

    public void setExpert_user_id(String expert_user_id) {
        this.expert_user_id = expert_user_id;
    }

    public String getDuration_min() {
        return duration_min;
    }

    public void setDuration_min(String duration_min) {
        this.duration_min = duration_min;
    }

    public String getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(String duration_sec) {
        this.duration_sec = duration_sec;
    }

    public ExpertModel getExpert() {
        return expert;
    }

    public void setExpert(ExpertModel expert) {
        this.expert = expert;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
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

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getNow_ep() {
        return now_ep;
    }

    public void setNow_ep(String now_ep) {
        this.now_ep = now_ep;
    }

    public String getOnline_user_num() {
        return online_user_num;
    }

    public void setOnline_user_num(String online_user_num) {
        this.online_user_num = online_user_num;
    }

    public String getIs_publish() {
        return is_publish;
    }

    public void setIs_publish(String is_publish) {
        this.is_publish = is_publish;
    }

    public String getIs_finish() {
        return is_finish;
    }

    public void setIs_finish(String is_finish) {
        this.is_finish = is_finish;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
}
