package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 试题数据类型model
 * Created by zhengwencheng on 2018/2/2 0002.
 * package com.jianzhong.bs.model
 */

public class ExaminationModel extends BaseModel {
    private String exam_id;
    private String title;
    private String react_sec;
    private List<ExaminationItemModel> item;

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReact_sec() {
        return react_sec;
    }

    public void setReact_sec(String react_sec) {
        this.react_sec = react_sec;
    }

    public List<ExaminationItemModel> getItem() {
        return item;
    }

    public void setItem(List<ExaminationItemModel> item) {
        this.item = item;
    }
}
