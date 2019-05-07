package com.jianzhong.lyag.model;

import java.util.List;

/**
 * Created by zhengwencheng on 2018/3/13 0013.
 * package com.jianzhong.bs.model
 */

public class CollectHomeModel extends BaseModel {
    private String asset_type;
    private List<CollectModel> detail;

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public List<CollectModel> getDetail() {
        return detail;
    }

    public void setDetail(List<CollectModel> detail) {
        this.detail = detail;
    }
}
