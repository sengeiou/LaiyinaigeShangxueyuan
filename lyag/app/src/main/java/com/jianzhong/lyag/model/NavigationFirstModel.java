package com.jianzhong.lyag.model;

import java.util.List;

/**
 * Created by zhengwencheng on 2018/1/26 0026.
 * package com.jianzhong.bs.model
 */

public class NavigationFirstModel extends BaseModel {
    //是否选中
    private int isSelected;
    //value
    private String label;

    private String title;
    private String path;
    //
    private List<NavigationSecModel> sub;
    //
    private List<OrderKeyModel> order_by;

    public List<OrderKeyModel> getOrder_by() {
        return order_by;
    }

    public void setOrder_by(List<OrderKeyModel> order_by) {
        this.order_by = order_by;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NavigationSecModel> getSub() {
        return sub;
    }

    public void setSub(List<NavigationSecModel> sub) {
        this.sub = sub;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
