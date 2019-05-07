package com.jianzhong.lyag.model;

/**
 * Created by zhengwencheng on 2018/3/7 0007.
 * package com.jianzhong.bs.model
 */

public class ReplyModel extends BaseModel {
    private String comment_id;
    private String create_at;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
}
