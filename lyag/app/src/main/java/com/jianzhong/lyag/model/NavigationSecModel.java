package com.jianzhong.lyag.model;

import java.util.List;
/**
 * 导航二级选择器
 * Created by zhengwencheng on 2018/1/26 0026.
 * package com.jianzhong.bs.adapter
 */
public class NavigationSecModel extends BaseModel {
    private int isSelected;
    private String navi_id;
    private String title;
    private String search_key;
    private String search;
    private String path;
    private List<NavigationSecSonModel> sub;

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getNavi_id() {
        return navi_id;
    }

    public void setNavi_id(String navi_id) {
        this.navi_id = navi_id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSearch_key() {
        return search_key;
    }

    public void setSearch_key(String search_key) {
        this.search_key = search_key;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<NavigationSecSonModel> getSub() {
        return sub;
    }

    public void setSub(List<NavigationSecSonModel> sub) {
        this.sub = sub;
    }
}
