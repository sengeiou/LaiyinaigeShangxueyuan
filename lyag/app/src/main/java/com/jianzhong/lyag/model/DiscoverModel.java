package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 搜索发现数据model
 * Created by zhengwencheng on 2018/3/27 0027.
 * package com.jianzhong.bs.model
 */

public class DiscoverModel extends BaseModel {
    private List<LiveModel> live;
    private List<CourseListModel> course;
    private List<ColumnContentModel> column;
    private List<AudioModel> audio;

    public List<LiveModel> getLive() {
        return live;
    }

    public void setLive(List<LiveModel> live) {
        this.live = live;
    }

    public List<CourseListModel> getCourse() {
        return course;
    }

    public void setCourse(List<CourseListModel> course) {
        this.course = course;
    }

    public List<ColumnContentModel> getColumn() {
        return column;
    }

    public void setColumn(List<ColumnContentModel> column) {
        this.column = column;
    }

    public List<AudioModel> getAudio() {
        return audio;
    }

    public void setAudio(List<AudioModel> audio) {
        this.audio = audio;
    }
}
