package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 首页数据model
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class HomeIndexModel extends BaseModel {
    private List<AudioDetailModel> audio; //代表首页"每日音频"块
    private FocusModel focus; //代表每日推荐块
    private List<LiveModel> live;
    private List<CourseDisplayContentModel> course; //代表首页的精品课程块
    private List<ColumnContentModel> column; //标识首页的专栏块
    private NaviBlockModel navi_block; //代表首页公告下的导航
    private List<BannerDisplayModel> banner; //标识首页的banner
    private List<NoticeMsgModel> notice;

    public List<AudioDetailModel> getAudio() {
        return audio;
    }

    public void setAudio(List<AudioDetailModel> audio) {
        this.audio = audio;
    }

    public FocusModel getFocus() {
        return focus;
    }

    public void setFocus(FocusModel focus) {
        this.focus = focus;
    }

    public List<LiveModel> getLive() {
        return live;
    }

    public void setLive(List<LiveModel> live) {
        this.live = live;
    }

    public List<CourseDisplayContentModel> getCourse() {
        return course;
    }

    public void setCourse(List<CourseDisplayContentModel> course) {
        this.course = course;
    }

    public List<ColumnContentModel> getColumn() {
        return column;
    }

    public void setColumn(List<ColumnContentModel> column) {
        this.column = column;
    }

    public NaviBlockModel getNavi_block() {
        return navi_block;
    }

    public void setNavi_block(NaviBlockModel navi_block) {
        this.navi_block = navi_block;
    }

    public List<BannerDisplayModel> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerDisplayModel> banner) {
        this.banner = banner;
    }

    public List<NoticeMsgModel> getNotice() {
        return notice;
    }

    public void setNotice(List<NoticeMsgModel> notice) {
        this.notice = notice;
    }
}
