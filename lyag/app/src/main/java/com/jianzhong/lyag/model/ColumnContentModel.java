package com.jianzhong.lyag.model;

/**
 * 标识这块呈现的内容
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class ColumnContentModel extends BaseModel {
    private String column_id;
    private String title; //专栏名称
    private String total_ep;    //总期数
    private String update_day;  //星期几更新的
    private String now_ep;      //当前是第几集
    private String play_num; //订阅的数量
    private String cover;       //专栏的封面
    private String expert_user_id;       //专家的用户id
    private ExpertModel expert;

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
}
