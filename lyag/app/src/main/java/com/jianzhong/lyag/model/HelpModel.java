package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 求助列表数据model
 * create by zhengwencheng on 2018/3/19 0019
 * package com.jianzhong.bs.model
 */
public class HelpModel extends BaseModel {
    private String help_id;
    private String share_id;
    private String create_at;
    private String update_at;
    private String title;
    private String content;
    private String comment_num;
    private String like_num;
    private String is_elite;
    private String is_no_answer;
    private UserModel user;
    private List<TagModel> tag;

    public String getHelp_id() {
        return help_id;
    }

    public void setHelp_id(String help_id) {
        this.help_id = help_id;
    }

    public String getShare_id() {
        return share_id;
    }

    public void setShare_id(String share_id) {
        this.share_id = share_id;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public String getIs_elite() {
        return is_elite;
    }

    public void setIs_elite(String is_elite) {
        this.is_elite = is_elite;
    }

    public String getIs_no_answer() {
        return is_no_answer;
    }

    public void setIs_no_answer(String is_no_answer) {
        this.is_no_answer = is_no_answer;
    }

    public List<TagModel> getTag() {
        return tag;
    }

    public void setTag(List<TagModel> tag) {
        this.tag = tag;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

}
