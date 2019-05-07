package com.jianzhong.lyag.model;

/**
 * 每日音频
 * Created by zhengwencheng on 2018/3/5 0005.
 * package com.jianzhong.bs.model
 */

public class AudioDetailModel extends BaseModel {
    private String audio_id;
    private String title;
    private String sub_title;
    private String duration_sec;
    private String audio_url;
    private String audio_cdn_url;
    private String cover;
    private String writing;
    private String expert_user_id;
    private String cat_id;
    private String play_num;
    private ExpertModel expert;
    private String asset_type;
    private String has_favor;
    private int isPlay;
    private String is_finish;
    private String cat_name;
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

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(String duration_sec) {
        this.duration_sec = duration_sec;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public String getExpert_user_id() {
        return expert_user_id;
    }

    public void setExpert_user_id(String expert_user_id) {
        this.expert_user_id = expert_user_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getPlay_num() {
        return play_num;
    }

    public void setPlay_num(String play_num) {
        this.play_num = play_num;
    }

    public ExpertModel getExpert() {
        return expert;
    }

    public void setExpert(ExpertModel expert) {
        this.expert = expert;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getHas_favor() {
        return has_favor;
    }

    public void setHas_favor(String has_favor) {
        this.has_favor = has_favor;
    }

    public int getIsPlay() {
        return isPlay;
    }

    public void setIsPlay(int isPlay) {
        this.isPlay = isPlay;
    }

    public String getIs_finish() {
        return is_finish;
    }

    public void setIs_finish(String is_finish) {
        this.is_finish = is_finish;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}
