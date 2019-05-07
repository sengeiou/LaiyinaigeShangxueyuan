package com.jianzhong.lyag.model;

/**
 *
 * create by zhengwencheng on 2018/3/23 0023
 * package com.jianzhong.bs.model
 */
public class StudyModel extends BaseModel {
    private String section_id;
    private String study_sec;
    private String is_section_finish;
    private String course_id;

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getStudy_sec() {
        return study_sec;
    }

    public void setStudy_sec(String study_sec) {
        this.study_sec = study_sec;
    }

    public String getIs_section_finish() {
        return is_section_finish;
    }

    public void setIs_section_finish(String is_section_finish) {
        this.is_section_finish = is_section_finish;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
}
