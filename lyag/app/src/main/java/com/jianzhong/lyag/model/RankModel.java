package com.jianzhong.lyag.model;

/**
 * create by zhengwencheng on 2018/3/21 0021
 * package com.jianzhong.bs.model
 */
public class RankModel extends BaseModel {
    private String user_id;
    private String rank_factor;
    private String sum_point;
    private UserModel user;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRank_factor() {
        return rank_factor;
    }

    public void setRank_factor(String rank_factor) {
        this.rank_factor = rank_factor;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getSum_point() {
        return sum_point;
    }

    public void setSum_point(String sum_point) {
        this.sum_point = sum_point;
    }
}
