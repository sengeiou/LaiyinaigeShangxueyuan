package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 评论对象
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.model
 */

public class CommentModel extends BaseModel {
    private String content;     //评论内容
    private String comment_id;  //评论id
    private String asset_type;  //评论id
    private String create_at;   //创建时间
    private String user_id;     //用户id
    private String top_id;      //回复的评论id
    private String reply_num;  //回复数量
    private String like_num;  //点赞数量
    private String is_top;
    private String is_adopt;
    private List<CommentSubModel> sub;
    private UserModel user;
    private String has_like;
    private String audio_url;
    private String audio_duration_sec;
    private List<String> images;
    private String self_type;

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

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
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

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public String getIs_top() {
        return is_top;
    }

    public void setIs_top(String is_top) {
        this.is_top = is_top;
    }

    public String getIs_adopt() {
        return is_adopt;
    }

    public void setIs_adopt(String is_adopt) {
        this.is_adopt = is_adopt;
    }

    public List<CommentSubModel> getSub() {
        return sub;
    }

    public void setSub(List<CommentSubModel> sub) {
        this.sub = sub;
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

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getAudio_duration_sec() {
        return audio_duration_sec;
    }

    public void setAudio_duration_sec(String audio_duration_sec) {
        this.audio_duration_sec = audio_duration_sec;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSelf_type() {
        return self_type;
    }

    public void setSelf_type(String self_type) {
        this.self_type = self_type;
    }
}
