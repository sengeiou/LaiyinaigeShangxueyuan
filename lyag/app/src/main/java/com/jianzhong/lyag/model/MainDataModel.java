package com.jianzhong.lyag.model;

import java.util.List;

/**
 * 首页TAB数据
 * Created by Administrator on 2017/5/5.
 */

public class MainDataModel {

    //定义数组来存放Fragment对象
    private List<Class> fragmentArray;
    //tab选项卡的文字
    private List<String> textViewArray;
    //按钮图片数组
    private List<Integer> imageViewArray;

    public List<Class> getFragmentArray() {
        return fragmentArray;
    }

    public void setFragmentArray(List<Class> fragmentArray) {
        this.fragmentArray = fragmentArray;
    }

    public List<String> getTextViewArray() {
        return textViewArray;
    }

    public void setTextViewArray(List<String> textViewArray) {
        this.textViewArray = textViewArray;
    }

    public List<Integer> getImageViewArray() {
        return imageViewArray;
    }

    public void setImageViewArray(List<Integer> imageViewArray) {
        this.imageViewArray = imageViewArray;
    }
}
