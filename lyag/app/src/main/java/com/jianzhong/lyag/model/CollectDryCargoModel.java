package com.jianzhong.lyag.model;

import java.util.List;

/**
 * Created by zhengwencheng on 2018/3/9 0009.
 * package com.jianzhong.bs.model
 */

public class CollectDryCargoModel extends BaseModel {
    private String audio_id;
    private String title;
    private String audio_url;
    private String duration_sec;
    private String audio_cdn_url;
    private List<TagModel> tag;

    public String getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(String audio_id) {
        this.audio_id = audio_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(String duration_sec) {
        this.duration_sec = duration_sec;
    }

    public String getAudio_cdn_url() {
        return audio_cdn_url;
    }

    public void setAudio_cdn_url(String audio_cdn_url) {
        this.audio_cdn_url = audio_cdn_url;
    }

    public List<TagModel> getTag() {
        return tag;
    }

    public void setTag(List<TagModel> tag) {
        this.tag = tag;
    }
}
