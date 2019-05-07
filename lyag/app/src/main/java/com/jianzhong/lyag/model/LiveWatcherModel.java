package com.jianzhong.lyag.model;

import java.util.List;

/**
 * create by zhengwencheng on 2018/3/26 0026
 * package com.jianzhong.bs.model
 */
public class LiveWatcherModel extends BaseModel {
    private String total;
    private List<UserModel> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<UserModel> getList() {
        return list;
    }

    public void setList(List<UserModel> list) {
        this.list = list;
    }
}
