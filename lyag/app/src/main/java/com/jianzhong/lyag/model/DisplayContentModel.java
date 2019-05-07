package com.jianzhong.lyag.model;

import java.util.List;

/**
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class DisplayContentModel extends BaseModel {
    private String cat_id; //音频分类id
    private String cat_name;  //音频分类名;
    private List<ContentDetailModel> detail;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public List<ContentDetailModel> getDetail() {
        return detail;
    }

    public void setDetail(List<ContentDetailModel> detail) {
        this.detail = detail;
    }
}
