package com.udit.aijiabao.utils;

import android.content.Context;
import android.os.Environment;

import com.udit.aijiabao.MyApplication;
import com.udit.aijiabao.entitys.AnswerInfo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;

public class ConstantData {
    public Context context;
    DbManager db;
    public static String answerId[] = {"1", "2", "3", "4"};
    public static String answerName[] = {"您的年龄是？", "您的工作是？", "下列属于腾讯开发的游戏?", "网络游戏一定需要付费"};
    public static String answerType[] = {"0", "0", "1", "2"};
    public static String answerOptionA[] = {"18岁以下", "学生", "梦幻西游", "对"};
    public static String answerOptionB[] = {"18岁至25岁", "公务单位", "英雄联盟", "错"};
    public static String answerOptionC[] = {"25岁至35岁", "工薪一族", "诛仙", ""};
    public static String answerOptionD[] = {"35岁至45岁", "自己当老板", "逆战", ""};
    public static String answerOptionE[] = {"45岁以上", "其他", "劲舞团", ""};
    public static String answerAnalysis[] = {"暂无解答", "自己当老板", "此题太简单了", "简单"};
    public static String answerScore[] = {"2", "2", "1", "2"};
    public static String answerCorrect[] = {"A", "D", "BD", "B"};

    public ConstantData(Context context) throws DbException {
        this.context = context;
        init();
        File file=Environment.getExternalStorageDirectory();
        DbManager.DaoConfig daoconfig = null;
        daoconfig.setDbName("sss");
        daoconfig.setDbVersion(1);
        daoconfig.setDbDir(file);
        db = x.getDb(daoconfig);

    }

    private void init() throws DbException {
        AnswerInfo answerinfo=new AnswerInfo();
        db.saveOrUpdate(answerinfo);
    }

}
