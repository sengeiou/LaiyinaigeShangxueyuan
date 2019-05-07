package com.jianzhong.lyag.model;

/**
 * 求助列表排序标签
 * Created by Max on 2018/3/18.
 */

public class HelpSortFieldModel extends BaseModel {
    private int isSelected;
    private String field;
    private String str;

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
