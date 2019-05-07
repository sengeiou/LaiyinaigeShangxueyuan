package com.jianzhong.lyag.model;

import com.jianzhong.lyag.widget.sortlistview.CharacterParser;
import com.jianzhong.lyag.widget.sortlistview.CommonPinyinComparator;

import java.util.List;

/**
 * 公司部门列表数据model
 * Created by zhengwencheng on 2018/1/20 0020.
 * package com.jianzhong.bs.model
 */


public class DepartmentModel extends BaseModel implements CommonPinyinComparator {
    //部门人数
    private int count;
    //是否选中
    private int isSelected;
    //
    private String branch_name;
    private String branch_id;
    private String pos_name;
    private String pos_id;
    //是否为经销商
    private String is_dealer;
    private List<ContactsModel> member;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getPos_name() {
        return pos_name;
    }

    public void setPos_name(String pos_name) {
        this.pos_name = pos_name;
    }

    public String getPos_id() {
        return pos_id;
    }

    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
    }

    public String getIs_dealer() {
        return is_dealer;
    }

    public void setIs_dealer(String is_dealer) {
        this.is_dealer = is_dealer;
    }

    public List<ContactsModel> getMember() {
        return member;
    }

    public void setMember(List<ContactsModel> member) {
        this.member = member;
    }

    @Override
    public String getSortLetters() {
        CharacterParser characterParser = CharacterParser.getInstance();
        //汉字转换成拼音
        String pinyin = characterParser.getSelling(branch_name);
        String sortString = pinyin.substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            return sortString.toUpperCase();
        } else {
            return "#";
        }
    }
}
