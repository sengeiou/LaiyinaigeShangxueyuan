package com.jianzhong.lyag.model;

/**
 * 学习任务列表Model
 * create by zhengwencheng on 2018/3/20 0020
 * package com.jianzhong.bs.model
 */
public class StudyTaskModel extends BaseModel {
    private String dictate_id;
    private String asset_type;
    private String finish_at;
    private String foreign_id;
    private String total_ep;
    private String finish_ep;
    private String cover;
    private String title;
    private String duration_sec;
    private String total_study_sec;
    private ExpertModel expert;

    public String getDictate_id() {
        return dictate_id;
    }

    public void setDictate_id(String dictate_id) {
        this.dictate_id = dictate_id;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getFinish_at() {
        return finish_at;
    }

    public void setFinish_at(String finish_at) {
        this.finish_at = finish_at;
    }

    public String getForeign_id() {
        return foreign_id;
    }

    public void setForeign_id(String foreign_id) {
        this.foreign_id = foreign_id;
    }

    public String getTotal_ep() {
        return total_ep;
    }

    public void setTotal_ep(String total_ep) {
        this.total_ep = total_ep;
    }

    public String getFinish_ep() {
        return finish_ep;
    }

    public void setFinish_ep(String finish_ep) {
        this.finish_ep = finish_ep;
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

    public String getTotal_study_sec() {
        return total_study_sec;
    }

    public void setTotal_study_sec(String total_study_sec) {
        this.total_study_sec = total_study_sec;
    }

    public ExpertModel getExpert() {
        return expert;
    }

    public void setExpert(ExpertModel expert) {
        this.expert = expert;
    }
}
