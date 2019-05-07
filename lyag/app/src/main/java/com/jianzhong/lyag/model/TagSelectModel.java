package com.jianzhong.lyag.model;


import com.jianzhong.lyag.widget.sortlistview.CharacterParser;
import com.jianzhong.lyag.widget.sortlistview.CommonPinyinComparator;

/**
 *
 * Created by zhengwencheng on 2018/1/19 0019.
 * package com.jianzhong.bs.model
 */

public class TagSelectModel extends BaseModel implements CommonPinyinComparator {
    private String tag;
    private int count;
    //是否选中
    private int isSelected;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
        String pinyin = characterParser.getSelling(tag);
        String sortString = pinyin.substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            return sortString.toUpperCase();
        } else {
            return "#";
        }
    }
}
