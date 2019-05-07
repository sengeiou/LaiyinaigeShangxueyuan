package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 互动首页数据model
 * create by zhengwencheng on 2018/3/17 0017
 * com.jianzhong.bs.model
 */
public class InteractModel extends BaseModel {
    private String create_at;
    private String update_at;
    private String title;
    private String like_num; //点赞数
    private String comment_num;
    private String is_elite; //是否精华
    private List<TagModel> tag;
    private String foreign_id;
    private String from;
    private String content;
    private UserModel user;

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getIs_elite() {
        return is_elite;
    }

    public void setIs_elite(String is_elite) {
        this.is_elite = is_elite;
    }

    public List<TagModel> getTag() {
        return tag;
    }

    public void setTag(List<TagModel> tag) {
        this.tag = tag;
    }

    public String getForeign_id() {
        return foreign_id;
    }

    public void setForeign_id(String foreign_id) {
        this.foreign_id = foreign_id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
