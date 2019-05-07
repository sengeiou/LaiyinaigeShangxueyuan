package com.jianzhong.lyag.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zhengwencheng on 2018/1/27.
 * E-mail 15889964542@163.com
 *
 * 课程播放记录model
 */
@DatabaseTable(tableName = "tb_live_record")
public class LiveRecorModel {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "live_id")
    private String live_id;

    @DatabaseField(columnName = "section_id")
    private String section_id;

    @DatabaseField(columnName = "course_record")
    private long course_record;

    @DatabaseField(columnName = "user_id")
    private String user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public long getCourse_record() {
        return course_record;
    }

    public void setCourse_record(long course_record) {
        this.course_record = course_record;
    }

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
