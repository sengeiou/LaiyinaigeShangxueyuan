package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 直播收藏列表model
 * Created by zhengwencheng on 2018/3/27 0027.
 * package com.jianzhong.bs.model
 */

public class LiveCollectModel extends BaseModel {

    private String live_id;
    private String title;
    private String cover;
    private List<TagModel> tag;
    private ExpertModel expert;
    private String online_user_num;
    private String start_at;
    private String expert_user_id;
    private String is_publish;

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
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

    public String getExpert_user_id() {
        return expert_user_id;
    }

    public void setExpert_user_id(String expert_user_id) {
        this.expert_user_id = expert_user_id;
    }

    public List<TagModel> getTag() {
        return tag;
    }

    public void setTag(List<TagModel> tag) {
        this.tag = tag;
    }

    public ExpertModel getExpert() {
        return expert;
    }

    public void setExpert(ExpertModel expert) {
        this.expert = expert;
    }

    public String getOnline_user_num() {
        return online_user_num;
    }

    public void setOnline_user_num(String online_user_num) {
        this.online_user_num = online_user_num;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getIs_publish() {
        return is_publish;
    }

    public void setIs_publish(String is_publish) {
        this.is_publish = is_publish;
    }
}
