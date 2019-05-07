package com.jianzhong.lyag.model;

import java.util.List;

/**
 * Created by zhengwencheng on 2018/1/22 0022.
 * package com.jianzhong.bs.model
 */

public class ProvinceModel extends BaseModel {
    private String id;
    private String name;
    private List<CityModel> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityModel> getList() {
        return list;
    }

    public void setList(List<CityModel> list) {
        this.list = list;
    }
}
