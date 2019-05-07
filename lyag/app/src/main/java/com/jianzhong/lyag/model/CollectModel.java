package com.jianzhong.lyag.model;

/**
 * 课程收藏数据model
 * Created by zhengwencheng on 2018/3/9 0009.
 * package com.jianzhong.bs.model
 */

public class CollectModel extends BaseModel {
    private String foreign_id;
    private String create_at;
    private String item_id;
    private int isSelected;
    private CourseListModel course;
    private ColumnCollectModel column;
    private CollectDryCargoModel audio;
    private NoticeCollectModel notice;
    private NoticeCollectModel share;
    private NoticeCollectModel help;
    private LiveCollectModel live;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public CourseListModel getCourse() {
        return course;
    }

    public void setCourse(CourseListModel course) {
        this.course = course;
    }

    public String getForeign_id() {
        return foreign_id;
    }

    public void setForeign_id(String foreign_id) {
        this.foreign_id = foreign_id;
    }

    public ColumnCollectModel getColumn() {
        return column;
    }

    public void setColumn(ColumnCollectModel column) {
        this.column = column;
    }

    public CollectDryCargoModel getAudio() {
        return audio;
    }

    public void setAudio(CollectDryCargoModel audio) {
        this.audio = audio;
    }

    public NoticeCollectModel getNotice() {
        return notice;
    }

    public void setNotice(NoticeCollectModel notice) {
        this.notice = notice;
    }

    public NoticeCollectModel getShare() {
        return share;
    }

    public void setShare(NoticeCollectModel share) {
        this.share = share;
    }

    public NoticeCollectModel getHelp() {
        return help;
    }

    public void setHelp(NoticeCollectModel help) {
        this.help = help;
    }

    public LiveCollectModel getLive() {
        return live;
    }

    public void setLive(LiveCollectModel live) {
        this.live = live;
    }
}
