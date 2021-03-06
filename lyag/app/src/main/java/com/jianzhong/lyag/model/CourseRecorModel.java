package com.jianzhong.lyag.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zhengwencheng on 2018/1/27.
 * E-mail 15889964542@163.com
 *
 * 课程播放记录model
 */
@DatabaseTable(tableName = "tb_course_record")
public class CourseRecorModel {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "course_id")
    private String course_id;

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


    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
