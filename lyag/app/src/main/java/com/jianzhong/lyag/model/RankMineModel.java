package com.jianzhong.lyag.model;

/**
 * 我的排名model
 * create by zhengwencheng on 2018/3/21 0021
 * package com.jianzhong.bs.model
 */
public class RankMineModel extends BaseModel {
    private String ranking;
    private RankModel info;

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public RankModel getInfo() {
        return info;
    }

    public void setInfo(RankModel info) {
        this.info = info;
    }
}
