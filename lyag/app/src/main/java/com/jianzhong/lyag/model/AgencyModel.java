package com.jianzhong.lyag.model;


import com.jianzhong.lyag.widget.sortlistview.CharacterParser;
import com.jianzhong.lyag.widget.sortlistview.CommonPinyinComparator;
/**
 * 经销商列表数据model
 * Created by zhengwencheng on 2018/1/20 0020.
 * package com.jianzhong.bs.model
 */

public class AgencyModel extends BaseModel implements CommonPinyinComparator {
    //人数
    private int count;
    //是否选中
    private int isSelected;
    //
    private String branch_name;
    private String branch_id;

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
