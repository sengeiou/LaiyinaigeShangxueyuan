package com.jianzhong.lyag.model;

/**
 * 推送消息model
 * Created by chenzhikai on 2018/4/25.
 */

public class PushMsgModel extends BaseModel {

    // 外键ID
    private int foreign_id;
    // 类型
    private String asset_type;

    public int getForeign_id() {
        return foreign_id;
    }

    public void setForeign_id(int foreign_id) {
        this.foreign_id = foreign_id;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }
}
