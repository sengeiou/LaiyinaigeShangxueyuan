package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 求助详情model
 * create by zhengwencheng on 2018/3/19 0019
 * package com.jianzhong.bs.model
 */
public class HelpDetailModel extends BaseModel {
    private String help_id;
    private String share_id;
    private String user_id;
    private String create_at;
    private String update_at;
    private String title;
    private String content;
    private List<String> images;
    private List<TagModel> tag;
    private String asset_type;
    private String has_like;
    private String has_favor;
    private UserModel user;

    public String getShare_id() {
        return share_id;
    }

    public void setShare_id(String share_id) {
        this.share_id = share_id;
    }

    public String getHelp_id() {
        return help_id;
    }

    public void setHelp_id(String help_id) {
        this.help_id = help_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<TagModel> getTag() {
        return tag;
    }

    public void setTag(List<TagModel> tag) {
        this.tag = tag;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getHas_like() {
        return has_like;
    }

    public void setHas_like(String has_like) {
        this.has_like = has_like;
    }

    public String getHas_favor() {
        return has_favor;
    }

    public void setHas_favor(String has_favor) {
        this.has_favor = has_favor;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
