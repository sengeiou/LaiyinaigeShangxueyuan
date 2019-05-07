package com.jianzhong.lyag.model;

/**
 * Created by Administrator on 2018/4/25.
 */

public class MessageModel extends BaseModel {

    private String title;
    private String user_id;
    private String content;
    private int create_at;
    private String asset_type;
    private int foreign_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public int getCreate_at() {
        return create_at;
    }

    public void setCreate_at(int create_at) {
        this.create_at = create_at;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public int getForeign_id() {
        return foreign_id;
    }

    public void setForeign_id(int foreign_id) {
        this.foreign_id = foreign_id;
    }
}
