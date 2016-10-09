package com.udit.aijiabao.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/5/14.
 */
public class FileUtils {


    public static void save(String json, String fileName) {

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(getSDPath(), fileName +".txt"));
            fos.write(json.getBytes("UTF-8"));
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDPath() {
        File sdDir;
        if (isSDEnable()) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }
        return null;
    }

    /**
     * 检测SD卡是否有效
     *
     * @return
     */
    public static boolean isSDEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
    }

}

