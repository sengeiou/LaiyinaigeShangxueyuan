package com.jianzhong.lyag.model;

import com.jianzhong.lyag.widget.sortlistview.CharacterParser;
import com.jianzhong.lyag.widget.sortlistview.CommonPinyinComparator;

/**
 * 联系人model
 * Created by zhengwencheng on 2018/1/19 0019.
 * package com.jianzhong.bs.model
 */

public class ContactsModel extends BaseModel implements CommonPinyinComparator {
    //加号
    private int icon;
    private String realname;
    //可能会用到的id
    private String user_id;
    private String pos_id;
    private String branch_id;
    private String parent_branch_id;
    private String is_dealer;
    private String branch_name;
    private String branch_path;
    private String pos_name;
    //主标题
    private String title;
    //副标题
    private String assist_title;
    //是否选中
    private int isSelected;
    //图片可能是头像
    private String avatar;
    private String is_outermost;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssist_title() {
        return assist_title;
    }

    public void setAssist_title(String assist_title) {
        this.assist_title = assist_title;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPos_id() {
        return pos_id;
    }

    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getParent_branch_id() {
        return parent_branch_id;
    }

    public void setParent_branch_id(String parent_branch_id) {
        this.parent_branch_id = parent_branch_id;
    }

    public String getIs_dealer() {
        return is_dealer;
    }

    public void setIs_dealer(String is_dealer) {
        this.is_dealer = is_dealer;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getPos_name() {
        return pos_name;
    }

    public void setPos_name(String pos_name) {
        this.pos_name = pos_name;
    }

    public String getIs_outermost() {
        return is_outermost;
    }

    public void setIs_outermost(String is_outermost) {
        this.is_outermost = is_outermost;
    }

    public String getBranch_path() {
        return branch_path;
    }

    public void setBranch_path(String branch_path) {
        this.branch_path = branch_path;
    }

    @Override
    public String getSortLetters() {
        CharacterParser characterParser = CharacterParser.getInstance();
        //汉字转换成拼音
        String pinyin = characterParser.getSelling(realname);
        String sortString = pinyin.substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            return sortString.toUpperCase();
        } else {
            return "#";
        }
    }
}
