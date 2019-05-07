package com.jianzhong.lyag.model;

/**
 * Created by zhengwencheng on 2018/1/30 0030.
 * package com.jianzhong.bs.model
 */

public class TagModel extends BaseModel {
    private String tag_id;
    private String tag_name;
    private int isSelected;

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }
}
