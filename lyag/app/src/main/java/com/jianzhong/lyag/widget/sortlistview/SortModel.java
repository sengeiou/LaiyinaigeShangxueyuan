package com.jianzhong.lyag.widget.sortlistview;

import com.jianzhong.lyag.model.BaseModel;

public class SortModel extends BaseModel implements CommonPinyinComparator{
    //加号
    private int icon;
    //名字
    private String name;
    //可能会用到的id
    private String id;
    //主标题
    private String title;
    //副标题
    private String assist_title;
    //是否选中
    private int isSelected;
    //图片可能是头像
    private String avatar;
    //职位 个人会用到
    private String job;
    //部门 个人信息会用到
    private String department;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String getSortLetters() {
        CharacterParser characterParser = CharacterParser.getInstance();
        //汉字转换成拼音
        String pinyin = characterParser.getSelling(name);
        String sortString = pinyin.substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            return sortString.toUpperCase();
        } else {
            return "#";
        }
    }
}


