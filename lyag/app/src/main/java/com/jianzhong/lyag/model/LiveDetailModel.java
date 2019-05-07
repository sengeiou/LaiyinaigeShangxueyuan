package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 直播详情数据model
 * Created by zhengwencheng on 2018/3/14 0014.
 * package com.jianzhong.bs.model
 */

public class LiveDetailModel extends BaseModel {
    private String live_id;
    private String live_type;
    private String title;
    private String start_at;
    private String create_at;
    private String end_at;
    private List<RecordUrlModel> record_url;
    private List<StudyModel> study;
    private String expert_user_id;
    private String cover;
    private String cover_in;
    private String favor_num;
    private String push_user_id;
    private String summary;
    private String is_del;
    private String is_all;
    private String push_url;
    private String pull_url;
    private String is_publish;
    private List<TagModel> tag;
    private ExpertModel expert;
    private String has_favor;
    private String online_user_num;
    private List<ExpertModel> docent;
    private String rc_talk;
    private String rc_chat;

    private String live_talk_room;
    private String live_chat_room;
    private String asset_type;
    private String is_finish;

    public String getOnline_user_num() {
        return online_user_num;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public void setOnline_user_num(String online_user_num) {
        this.online_user_num = online_user_num;
    }

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public String getLive_type() {
        return live_type;
    }

    public void setLive_type(String live_type) {
        this.live_type = live_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public List<RecordUrlModel> getRecord_url() {
        return record_url;
    }

    public void setRecord_url(List<RecordUrlModel> record_url) {
        this.record_url = record_url;
    }

    public List<StudyModel> getStudy() {
        return study;
    }

    public void setStudy(List<StudyModel> study) {
        this.study = study;
    }

    public String getExpert_user_id() {
        return expert_user_id;
    }

    public void setExpert_user_id(String expert_user_id) {
        this.expert_user_id = expert_user_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover_in() {
        return cover_in;
    }

    public void setCover_in(String cover_in) {
        this.cover_in = cover_in;
    }

    public String getFavor_num() {
        return favor_num;
    }

    public void setFavor_num(String favor_num) {
        this.favor_num = favor_num;
    }

    public String getPush_user_id() {
        return push_user_id;
    }

    public void setPush_user_id(String push_user_id) {
        this.push_user_id = push_user_id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getIs_all() {
        return is_all;
    }

    public void setIs_all(String is_all) {
        this.is_all = is_all;
    }

    public String getPush_url() {
        return push_url;
    }

    public void setPush_url(String push_url) {
        this.push_url = push_url;
    }

    public String getPull_url() {
        return pull_url;
    }

    public void setPull_url(String pull_url) {
        this.pull_url = pull_url;
    }

    public String getIs_publish() {
        return is_publish;
    }

    public void setIs_publish(String is_publish) {
        this.is_publish = is_publish;
    }

    public List<TagModel> getTag() {
        return tag;
    }

    public void setTag(List<TagModel> tag) {
        this.tag = tag;
    }

    public ExpertModel getExpert() {
        return expert;
    }

    public void setExpert(ExpertModel expert) {
        this.expert = expert;
    }

    public String getHas_favor() {
        return has_favor;
    }

    public void setHas_favor(String has_favor) {
        this.has_favor = has_favor;
    }

    public String getLive_chat_room() {
        return live_chat_room;
    }

    public void setLive_chat_room(String live_chat_room) {
        this.live_chat_room = live_chat_room;
    }

    public List<ExpertModel> getDocent() {
        return docent;
    }

    public void setDocent(List<ExpertModel> docent) {
        this.docent = docent;
    }

    public String getRc_talk() {
        return rc_talk;
    }

    public void setRc_talk(String rc_talk) {
        this.rc_talk = rc_talk;
    }

    public String getRc_chat() {
        return rc_chat;
    }

    public void setRc_chat(String rc_chat) {
        this.rc_chat = rc_chat;
    }

    public String getLive_talk_room() {
        return live_talk_room;
    }

    public void setLive_talk_room(String live_talk_room) {
        this.live_talk_room = live_talk_room;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getIs_finish() {
        return is_finish;
    }

    public void setIs_finish(String is_finish) {
        this.is_finish = is_finish;
    }
}
