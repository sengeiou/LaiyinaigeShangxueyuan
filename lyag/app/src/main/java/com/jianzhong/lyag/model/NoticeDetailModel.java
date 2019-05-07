package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 重要通知详情model
 * Created by zhengwencheng on 2018/3/1 0001.
 * package com.jianzhong.bs.model
 */

public class NoticeDetailModel extends BaseModel {
    private String thread_id;
    private String thread_type;
    private String create_at;
    private String update_at;
    private String user_id;
    private String is_del;
    private String title;
    private String content;
    private List<String> images;
    private String favor_num;
    private String share_num;
    private String reply_num;
    private String like_num;
    private String is_all;
    private List<TagModel> tag;
    private String has_like;
    private String has_favor;
    private String operator_name;
    private String operator_duty;
    private String asset_type;
    private String avatar;
    private List<AttachmentModel> attachment;

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getThread_type() {
        return thread_type;
    }

    public void setThread_type(String thread_type) {
        this.thread_type = thread_type;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
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

    public String getFavor_num() {
        return favor_num;
    }

    public void setFavor_num(String favor_num) {
        this.favor_num = favor_num;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
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

    public String getIs_all() {
        return is_all;
    }

    public void setIs_all(String is_all) {
        this.is_all = is_all;
    }

    public List<TagModel> getTag() {
        return tag;
    }

    public void setTag(List<TagModel> tag) {
        this.tag = tag;
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

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public String getOperator_duty() {
        return operator_duty;
    }

    public void setOperator_duty(String operator_duty) {
        this.operator_duty = operator_duty;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<AttachmentModel> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<AttachmentModel> attachment) {
        this.attachment = attachment;
    }
}
