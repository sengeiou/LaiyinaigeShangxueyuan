package com.jianzhong.lyag.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zhengwencheng on 2018/1/27.
 * E-mail 15889964542@163.com
 *
 * 搜索记录model
 */
@DatabaseTable(tableName = "tb_audio_record")
public class AudioRecorModel {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "audio_id")
    private String audio_id;

    @DatabaseField(columnName = "title")
    private String title;

    @DatabaseField(columnName = "audio_record")
    private long audio_record;

    @DatabaseField(columnName = "duration_sec")
    private String duration_sec;

    @DatabaseField(columnName = "create_at")
    private String create_at;

    @DatabaseField(columnName = "user_id")
    private String user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public long getAudio_record() {
        return audio_record;
    }

    public void setAudio_record(long audio_record) {
        this.audio_record = audio_record;
    }

    public String getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(String duration_sec) {
        this.duration_sec = duration_sec;
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
}
