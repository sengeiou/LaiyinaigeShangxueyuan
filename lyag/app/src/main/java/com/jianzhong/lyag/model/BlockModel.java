package com.jianzhong.lyag.model;

/**
 * 模块的数据model 控制隐藏
 * Created by zhengwencheng on 2018/1/24 0024.
 * package com.jianzhong.bs.model
 */

public class BlockModel extends BaseModel {
    private String block_id;
    private String label;
    private String block_name;
    private String is_hide;
    private String sort;
    private String remark;
    private String pid;
    private String is_user_adapt;
    private String is_base;

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getIs_hide() {
        return is_hide;
    }

    public void setIs_hide(String is_hide) {
        this.is_hide = is_hide;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIs_user_adapt() {
        return is_user_adapt;
    }

    public void setIs_user_adapt(String is_user_adapt) {
        this.is_user_adapt = is_user_adapt;
    }

    public String getIs_base() {
        return is_base;
    }

    public void setIs_base(String is_base) {
        this.is_base = is_base;
    }
}
