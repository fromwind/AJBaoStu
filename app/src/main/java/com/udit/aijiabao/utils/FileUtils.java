package com.udit.aijiabao.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/5/14.
 */
public class FileUtils {
    public static final String PROJECT_1_URL = "http://bmob-cdn-6792.b0.upaiyun.com/2016/10/24/16d4746340ccb74d80cb02a77d537164.zip";
    public static final String PROJECT_2_URL = "";
    public static final String PROJECT_3_URL = "";
    public static final String PROJECT_401_URL = "http://bmob-cdn-6792.b0.upaiyun.com/2016/10/21/e392cf084055d37d8056d2b28a31d081.zip";
    public static final String PROJECT_402_URL = "http://bmob-cdn-6792.b0.upaiyun.com/2016/10/24/ba2fa72040ab07a58050921b3ec9e795.zip";
    public static final String PROJECT_DB_URL="http://bmob-cdn-6792.b0.upaiyun.com/2016/10/26/39d5566840a48086805818c043c0d444.zip";
    public static void save(String json, String fileName) {

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(getSDPath(), fileName + ".txt"));
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
     * 获取aijiabao路径
     *
     * @return
     */
    public static String getAJBaoPath() {
        File AJBaoDir;
        if (!getSDPath().isEmpty()) {
            AJBaoDir = new File(getSDPath() + "/.AJBao");
            return AJBaoDir.toString();
        }
        return null;
    }

    /**
     * 获取科目一路径
     *
     * @return
     */
    public static String getproject1Path() {
        File project1Dir;
        if (!getSDPath().isEmpty()) {
            project1Dir = new File(getAJBaoPath() + "/project1");
            return project1Dir.toString();
        }
        return null;
    }

    /**
     * 获取科目二路径
     *
     * @return
     */
    public static String getproject2Path() {
        File project2Dir;
        if (!getSDPath().isEmpty()) {
            project2Dir = new File(getAJBaoPath() + "/project2");
            return project2Dir.toString();
        }
        return null;
    }

    /**
     * 获取科目三路径
     *
     * @return
     */
    public static String getproject3Path() {
        File project3Dir;
        if (!getSDPath().isEmpty()) {
            project3Dir = new File(getAJBaoPath() + "/project3");
            return project3Dir.toString();
        }
        return null;
    }

    /**
     * 获取科目四路径
     *
     * @return
     */
    public static String getproject4Path() {
        File project4Dir;
        if (!getSDPath().isEmpty()) {
            project4Dir = new File(getAJBaoPath() + "/project4");
            return project4Dir.toString();
        }
        return null;
    }
    /**
     * 获取数据库路径
     *
     * @return
     */
    public static String getDBPath() {
        File DBDir;
        if (!getSDPath().isEmpty()) {
            DBDir = new File(getAJBaoPath() + "/DB");
            return DBDir.toString();
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

