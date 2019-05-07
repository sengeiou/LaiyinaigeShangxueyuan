package com.jianzhong.lyag.model;

/**
 * 积分详细数据model
 * create by zhengwencheng on 2018/3/26 0026
 * package com.jianzhong.bs.model
 */
public class PointDetailModel extends BaseModel {
    private String stream_id;
    private String create_at;
    private String label;
    private String point;
    private RuleModel rule;

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public RuleModel getRule() {
        return rule;
    }

    public void setRule(RuleModel rule) {
        this.rule = rule;
    }
}
