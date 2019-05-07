package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 学分明细Expandviewmodel
 * create by zhengwencheng on 2018/3/26 0026
 * package com.jianzhong.bs.model
 */
public class PointExpandModel extends BaseModel {
    private String tag;
    private List<PointDetailModel> point_detail;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<PointDetailModel> getPoint_detail() {
        return point_detail;
    }

    public void setPoint_detail(List<PointDetailModel> point_detail) {
        this.point_detail = point_detail;
    }
}
