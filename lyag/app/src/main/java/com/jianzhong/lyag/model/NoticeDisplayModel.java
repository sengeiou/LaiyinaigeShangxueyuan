package com.jianzhong.lyag.model;

import java.util.List;

/**
 * Created by zhengwencheng on 2018/1/25 0025.
 * package com.jianzhong.bs.model
 */

public class NoticeDisplayModel extends BaseModel {
    private List<NoticeMsgModel> content;

    public List<NoticeMsgModel> getContent() {
        return content;
    }

    public void setContent(List<NoticeMsgModel> content) {
        this.content = content;
    }
}
