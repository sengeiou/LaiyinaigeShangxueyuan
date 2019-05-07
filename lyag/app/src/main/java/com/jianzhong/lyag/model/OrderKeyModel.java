package com.jianzhong.lyag.model;

/**
 * Created by zhengwencheng on 2018/1/27 0027.
 * package com.jianzhong.bs.model
 */

public class OrderKeyModel extends BaseModel {
    private int isSelected;
    private String key;
    private String str;

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
