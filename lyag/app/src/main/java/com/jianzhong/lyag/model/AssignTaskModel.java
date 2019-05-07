package com.jianzhong.lyag.model;

/**
 * 指派学习任务数据model
 * create by zhengwencheng on 2018/3/21 0021
 * package com.jianzhong.bs.model
 */
public class AssignTaskModel extends BaseModel {

    private String dictate_id;
    private String asset_type;
    private String create_at;
    private String foreign_id;
    private String assign_user_id;
    private String title;
    private String dictate_title;
    private String un_finish_num;
    private String is_read;

    public String getDictate_id() {
        return dictate_id;
    }

    public void setDictate_id(String dictate_id) {
        this.dictate_id = dictate_id;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getForeign_id() {
        return foreign_id;
    }

    public void setForeign_id(String foreign_id) {
        this.foreign_id = foreign_id;
    }

    public String getAssign_user_id() {
        return assign_user_id;
    }

    public void setAssign_user_id(String assign_user_id) {
        this.assign_user_id = assign_user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDictate_title() {
        return dictate_title;
    }

    public void setDictate_title(String dictate_title) {
        this.dictate_title = dictate_title;
    }

    public String getUn_finish_num() {
        return un_finish_num;
    }

    public void setUn_finish_num(String un_finish_num) {
        this.un_finish_num = un_finish_num;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }
}
