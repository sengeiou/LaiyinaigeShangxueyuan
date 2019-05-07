package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 课程详情数据model
 * Created by zhengwencheng on 2018/2/27 0027.
 * package com.jianzhong.bs.model
 */

public class ClassDetailModel extends BaseModel {
    private String course_id;   //课程id
    private String cover;       //封面
    private String title;       //标题
    private String duration_sec;  //总时长
    private String expert_user_id;  //专家id
    private String judge_sum;       //评分总分
    private String judge_num;       //评分总人数
    private String summary;         //简介描述
    private String play_num;        //播放次数
    private String comment_num;        //评论次数
    private String favor_num;        //被赞次数
    private String share_num;        //分享次数
    private ExpertModel expert;
    private String asset_type;      //
    private ColumnModel column;
    private List<SectionModel> section; //分段课程列表
    private String cover_in;
    private String judge_star;  //评分的星数
    private String has_favor;

    public ColumnModel getColumn() {
        return column;
    }

    public void setColumn(ColumnModel column) {
        this.column = column;
    }

    private List<StudyModel> study;

    public List<StudyModel> getStudy() {
        return study;
    }

    public void setStudy(List<StudyModel> study) {
        this.study = study;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getExpert_user_id() {
        return expert_user_id;
    }

    public void setExpert_user_id(String expert_user_id) {
        this.expert_user_id = expert_user_id;
    }

    public String getJudge_sum() {
        return judge_sum;
    }

    public void setJudge_sum(String judge_sum) {
        this.judge_sum = judge_sum;
    }

    public String getJudge_num() {
        return judge_num;
    }

    public void setJudge_num(String judge_num) {
        this.judge_num = judge_num;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public List<SectionModel> getSection() {
        return section;
    }

    public void setSection(List<SectionModel> section) {
        this.section = section;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getFavor_num() {
        return favor_num;
    }

    public void setFavor_num(String favor_num) {
        this.favor_num = favor_num;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getCover_in() {
        return cover_in;
    }

    public void setCover_in(String cover_in) {
        this.cover_in = cover_in;
    }

    public String getJudge_star() {
        return judge_star;
    }

    public void setJudge_star(String judge_star) {
        this.judge_star = judge_star;
    }

    public String getHas_favor() {
        return has_favor;
    }

    public void setHas_favor(String has_favor) {
        this.has_favor = has_favor;
    }
}
