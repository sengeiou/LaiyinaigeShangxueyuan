package com.jianzhong.lyag.model;

/**
 * Created by zhengwencheng on 2018/2/2 0002.
 * package com.jianzhong.bs.model
 */

public class ExaminationItemModel extends BaseModel {
    private int isSelected;
    private String label;
    private String description;
    private String is_right;
    private String exam_id;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIs_right() {
        return is_right;
    }

    public void setIs_right(String is_right) {
        this.is_right = is_right;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }
}
