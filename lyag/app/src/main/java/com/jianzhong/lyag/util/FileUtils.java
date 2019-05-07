package com.jianzhong.lyag.util;

/**
 * Created by chenzhikai
 * Email 1310741158@qq.com
 */

import android.content.Context;
import android.os.Environment;

import com.baselib.util.DeviceInfoUtil;

import java.io.File;
import java.io.IOException;


/**
 * 文件操作类
 *
 * @author hezhaoxia
 *
 */
public class FileUtils {

    // 默认保存文件的路径
    private static String PATH = Environment.getExternalStorageDirectory().getPath() + "/yingshang";
    private static String PATH_IMAGE = PATH + "/image/";
    private static String PATH_IMAGE_COMPRESS = PATH + "/image_compress/";
    public static String PATH_CACHE = PATH + "/cache/";

    public static final String IMAGE_FILE_NAME = System.currentTimeMillis() + ".jpg";

    public static void makeDirs(Context mContext) {
        // 判断SD卡是否存在
        if(!DeviceInfoUtil.isSDAva()){
            PATH = getDataPath(mContext);
        }
        com.baselib.util.FileUtils.makeDirs(PATH_IMAGE);
        com.baselib.util.FileUtils.makeDirs(PATH_IMAGE_COMPRESS);
        com.baselib.util.FileUtils.makeDirs(PATH_CACHE);

    }

    /**
     * 获取内部存储路径
     * @return
     */
    public static String getDataPath(Context mContext) {
        return "/data/data/" + mContext.getPackageName();
    }

    public static String getPath(){
        return PATH;
    }

    public static String getPathImage(){
        return PATH_IMAGE;
    }

    public static String getPathImageCompress(){
        return PATH_IMAGE_COMPRESS;
    }

    /**
     * 创建一个文件
     * @return
     */
    public static File createFile(String name) {
        File file = new File(name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }



}

