package com.jianzhong.lyag.model;

/**
 * 分段课程数据model
 * Created by zhengwencheng on 2018/2/27 0027.
 * package com.jianzhong.bs.model
 */

public class SectionModel extends BaseModel {
    private String section_id;      //分段id
    private String course_id;      //课程id
    private String title;           //分段标题
    private String video_url;      //课程播放地址
    private String video_cdn_url;      //课程加速播放地址
    private String audio_url;      //音频播放地址
    private String audio_cdn_url;      //音频加速播放地址
    private String is_del;          //是否已经删除
    private String duration_sec;      //单个时长
    private int isSelected;         //是否选中
    private int isPlay;
    private int is_section_finish;

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_cdn_url() {
        return video_cdn_url;
    }

    public void setVideo_cdn_url(String video_cdn_url) {
        this.video_cdn_url = video_cdn_url;
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

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(String duration_sec) {
        this.duration_sec = duration_sec;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public int getIsPlay() {
        return isPlay;
    }

    public void setIsPlay(int isPlay) {
        this.isPlay = isPlay;
    }

    public int getIs_section_finish() {
        return is_section_finish;
    }

    public void setIs_section_finish(int is_section_finish) {
        this.is_section_finish = is_section_finish;
    }
}
