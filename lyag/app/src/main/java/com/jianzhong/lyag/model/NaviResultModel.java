package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 导航列表数据适配器
 * Created by zhengwencheng on 2018/1/29 0029.
 * package com.jianzhong.bs.model
 */

public class NaviResultModel extends BaseModel {
    private String asset;
    private String count; //总数量
    private List<NaviResultListModel> list;

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<NaviResultListModel> getList() {
        return list;
    }

    public void setList(List<NaviResultListModel> list) {
        this.list = list;
    }
}
