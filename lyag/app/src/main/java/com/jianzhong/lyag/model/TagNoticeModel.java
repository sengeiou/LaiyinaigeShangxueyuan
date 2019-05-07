package com.jianzhong.lyag.model;

/**
 * 重要通知筛选的tag
 * Created by zhengwencheng on 2018/2/26 0026.
 * package com.jianzhong.bs.model
 */

public class TagNoticeModel extends BaseModel {
    //是否选择
    private int isSelected;
    private String tag_name;
    private String tag_id;

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }
}
