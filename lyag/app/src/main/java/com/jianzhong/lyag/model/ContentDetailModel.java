package com.jianzhong.lyag.model;

/**
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class ContentDetailModel extends BaseModel {
    private String title; //这条音频的标题
    private String audio_id ; //这条音频的id

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
}
