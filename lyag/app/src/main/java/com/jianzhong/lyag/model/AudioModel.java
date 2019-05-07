package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 音频数据model
 * Created by zhengwencheng on 2018/3/27 0027.
 * package com.jianzhong.bs.model
 */

public class AudioModel extends BaseModel {
    private String title;
    private String audio_id;
    private String create_at;
    private String duration_sec;
    private String expert_user_id;
    private String audio_url;
    private String audio_cdn_url;
    private ExpertModel expert;
    private List<TagModel> tag;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(String audio_id) {
        this.audio_id = audio_id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(String duration_sec) {
        this.duration_sec = duration_sec;
    }

    public String getExpert_user_id() {
        return expert_user_id;
    }

    public void setExpert_user_id(String expert_user_id) {
        this.expert_user_id = expert_user_id;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getAudio_cdn_url() {
        return audio_cdn_url;
    }

    public void setAudio_cdn_url(String audio_cdn_url) {
        this.audio_cdn_url = audio_cdn_url;
    }

    public ExpertModel getExpert() {
        return expert;
    }

    public void setExpert(ExpertModel expert) {
        this.expert = expert;
    }

    public List<TagModel> getTag() {
        return tag;
    }

    public void setTag(List<TagModel> tag) {
        this.tag = tag;
    }
}
