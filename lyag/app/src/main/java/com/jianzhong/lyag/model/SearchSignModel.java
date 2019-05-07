package com.jianzhong.lyag.model;

/**
 * 热门搜索的标签
 * Created by zhengwencheng on 2018/1/27 0027.
 * package com.jianzhong.bs.model
 */

public class SearchSignModel extends BaseModel {
    private String id;
    private String Value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
