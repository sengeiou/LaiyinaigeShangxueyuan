package com.jianzhong.lyag.model;

/**
 * 下级评论对象
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.model
 */

public class CommentSubModel extends BaseModel {

    private String content;     //评论内容
    private String comment_id;     //评论id
    private String create_at;     //创建时间
    private String user_id;     //用户id
    private String top_id;      //回复的评论id
    private String reply_num;      //回复数量
    private UserModel user;
    private String has_like;
    private String like_num;
    private String self_type;
    private String asset_type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTop_id() {
        return top_id;
    }

    public void setTop_id(String top_id) {
        this.top_id = top_id;
    }

    public String getReply_num() {
        return reply_num;
    }

    public void setReply_num(String reply_num) {
        this.reply_num = reply_num;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getHas_like() {
        return has_like;
    }

    public void setHas_like(String has_like) {
        this.has_like = has_like;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public String getSelf_type() {
        return self_type;
    }

    public void setSelf_type(String self_type) {
        this.self_type = self_type;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }
}
