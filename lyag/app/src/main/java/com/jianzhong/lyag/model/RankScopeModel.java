package com.jianzhong.lyag.model;

/**
 * Created by zhengwencheng on 2017/2/28.
 * package com.jianzhong.ys.model
 */

public class RankScopeModel extends BaseModel {

    private String key;
    private String name;
    private boolean isSelected;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
