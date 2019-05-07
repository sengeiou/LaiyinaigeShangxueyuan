package com.jianzhong.lyag.util;

import com.jianzhong.lyag.glide.GlideImageLoader;
import com.lzy.imagepicker.view.CropImageView;
/**
 * Created by chenzhikai
 * Email 1310741158@qq.com
 */
public class ImagePickerUtils {

    private static int maxImgCount = 9;               //允许选择图片最大数

    public static void initImagePicker() {
        com.lzy.imagepicker.ImagePicker imagePicker = com.lzy.imagepicker.ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

}
