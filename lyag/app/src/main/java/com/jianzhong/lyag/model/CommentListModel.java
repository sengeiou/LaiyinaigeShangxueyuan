package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 评论对象
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.model
 */

public class CommentListModel extends BaseModel {
    private String count;
    private List<CommentModel> list;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<CommentModel> getList() {
        return list;
    }

    public void setList(List<CommentModel> list) {
        this.list = list;
    }
}
