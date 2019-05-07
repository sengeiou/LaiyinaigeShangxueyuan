package com.jianzhong.lyag.model;

/**
 *
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class NaviBlockCourseModel extends BaseModel {
    private String block_name;
    private String label;
    private String is_hide;

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIs_hide() {
        return is_hide;
    }

    public void setIs_hide(String is_hide) {
        this.is_hide = is_hide;
    }
}
