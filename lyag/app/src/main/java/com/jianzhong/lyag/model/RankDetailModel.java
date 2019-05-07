package com.jianzhong.lyag.model;

import java.util.List;

/**
 * create by zhengwencheng on 2018/3/21 0021
 * package com.jianzhong.bs.model
 */
public class RankDetailModel extends BaseModel {
    private RankMineModel my_rank;

    private List<RankModel> list;

    public RankMineModel getMy_rank() {
        return my_rank;
    }

    public void setMy_rank(RankMineModel my_rank) {
        this.my_rank = my_rank;
    }

    public List<RankModel> getList() {
        return list;
    }

    public void setList(List<RankModel> list) {
        this.list = list;
    }
}
