package com.lzy.thumbnail;

/**
 * Created by Administrator on 2016/7/7.
 */
public interface CustomImageSizeModel {
    String requestCustomSizeUrl(int width, int height);

    String getBaseUrl();
}
