package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 广告位model
 * Created by zhengwencheng on 2018/1/23 0023.
 * package com.jianzhong.bs.model
 */

public class BannerModel extends BaseModel {
    private List<BannerDisplayModel> content;

    public List<BannerDisplayModel> getContent() {
        return content;
    }

    public void setContent(List<BannerDisplayModel> content) {
        this.content = content;
    }
}
