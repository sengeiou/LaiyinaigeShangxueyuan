package com.lzy.thumbnail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/7.
 */
public class CustomImageSizeModelImp implements CustomImageSizeModel, Parcelable {
    private String baseUrl;
    private static final String TAG = "CustomImageSizeModelImp";

    public CustomImageSizeModelImp(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String requestCustomSizeUrl(int width, int height) {
        /**
         * 这个是oss的缩略处理规则，一般的图片的是 ： "?imageView2/3/h/" + height + "/w/" + width
         * oss: ?x-oss-process=image/resize,w_200
         */
        String url = baseUrl + "?x-oss-process=image/resize,w_" + width;  // 一般图片使用这个 ，但是oss的图片地址不起作用
        return url;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.baseUrl);
    }

    protected CustomImageSizeModelImp(Parcel in) {
        this.baseUrl = in.readString();
    }

    public static final Creator<CustomImageSizeModelImp> CREATOR = new Creator<CustomImageSizeModelImp>() {
        @Override
        public CustomImageSizeModelImp createFromParcel(Parcel source) {
            return new CustomImageSizeModelImp(source);
        }

        @Override
        public CustomImageSizeModelImp[] newArray(int size) {
            return new CustomImageSizeModelImp[size];
        }
    };
}
