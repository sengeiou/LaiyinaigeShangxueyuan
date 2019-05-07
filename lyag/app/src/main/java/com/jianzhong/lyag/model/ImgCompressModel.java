package com.jianzhong.lyag.model;

/**
 * create by zhengwencheng on 2018/3/19 0019
 * package com.jianzhong.bs.model
 */
public class ImgCompressModel extends BaseModel {
    private String md5Sting;

    private String imgPath;

    public String getMd5Sting() {
        return md5Sting;
    }

    public void setMd5Sting(String md5Sting) {
        this.md5Sting = md5Sting;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
