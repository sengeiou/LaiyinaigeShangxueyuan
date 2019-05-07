package com.jianzhong.lyag.model;

/**
 *
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class NaviBlockDispalyModel extends BaseModel {
    private NaviBlockCourseModel navi_course;
    private NaviBlockCourseModel navi_column;
    private NaviBlockCourseModel navi_live;
    private NaviBlockCourseModel navi_catgory;

    public NaviBlockCourseModel getNavi_course() {
        return navi_course;
    }

    public void setNavi_course(NaviBlockCourseModel navi_course) {
        this.navi_course = navi_course;
    }

    public NaviBlockCourseModel getNavi_column() {
        return navi_column;
    }

    public void setNavi_column(NaviBlockCourseModel navi_column) {
        this.navi_column = navi_column;
    }

    public NaviBlockCourseModel getNavi_live() {
        return navi_live;
    }

    public void setNavi_live(NaviBlockCourseModel navi_live) {
        this.navi_live = navi_live;
    }

    public NaviBlockCourseModel getNavi_catgory() {
        return navi_catgory;
    }

    public void setNavi_catgory(NaviBlockCourseModel navi_catgory) {
        this.navi_catgory = navi_catgory;
    }
}
