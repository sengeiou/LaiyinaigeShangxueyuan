package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 代表首页的精品课程块
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class CourseModel extends BaseModel {
    private List<CourseDisplayContentModel> content;

    public List<CourseDisplayContentModel> getContent() {
        return content;
    }

    public void setContent(List<CourseDisplayContentModel> content) {
        this.content = content;
    }
}
