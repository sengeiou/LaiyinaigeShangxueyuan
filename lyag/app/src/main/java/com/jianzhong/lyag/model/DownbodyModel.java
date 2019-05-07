package com.jianzhong.lyag.model;

import java.util.List;

/**
 * Created by zhengwencheng on 2018/4/11 0011.
 * package com.jianzhong.bs.model
 */

public class DownbodyModel extends BaseModel {
    private int type;       //0：音频，1：课程
    private int mediaType;  //文件类型 0：音频 1：视频
    private String head_id;
    private String section_id;
    private String url;
    private String title;
    private String duration_sec; //时长
    private String img_url;
    private String name;
    private String post;
    private List<String> userList;
    private int isSelected;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getHead_id() {
        return head_id;
    }

    public void setHead_id(String head_id) {
        this.head_id = head_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(String duration_sec) {
        this.duration_sec = duration_sec;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }
}
