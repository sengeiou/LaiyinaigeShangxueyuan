package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 专栏收藏
 * Created by zhengwencheng on 2018/3/9 0009.
 * package com.jianzhong.bs.model
 */

public class ColumnCollectModel extends BaseModel {
    private String column_id;
    private String title;
    private String cover;
    private String total_ep;
    private String now_ep;
    private String update_day;
    private String expert_user_id;
    private String play_num;;
    private ExpertModel expert;
    private List<TagModel> tag;

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getUpdate_day() {
        return update_day;
    }

    public void setUpdate_day(String update_day) {
        this.update_day = update_day;
    }

    public String getExpert_user_id() {
        return expert_user_id;
    }

    public void setExpert_user_id(String expert_user_id) {
        this.expert_user_id = expert_user_id;
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

    public List<TagModel> getTag() {
        return tag;
    }

    public void setTag(List<TagModel> tag) {
        this.tag = tag;
    }
}
