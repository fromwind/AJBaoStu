package com.udit.aijiabao;

import com.udit.aijiabao.configs.Constants;
import com.udit.aijiabao.encrypt.Sec;
import com.udit.aijiabao.entitys.RespLogin;
import com.udit.aijiabao.utils.PreferencesUtils;

/**
 * Created by Administrator on 2016/5/12.
 */
public class User {

    private static final String USER_NAME = "user_name";
    private static final String USER_PASS = "user_pass";

    private static final String USER_ID = "user_id";
    private static final String NAME = "name";
    private static final String PHONE_NUMBER = "phone";
    private static final String MEMBER_ID = "member_id";
    private static final String AUTH_TOKEN = "auth_token";
    private static final String EMAIL = "email";
    private static final String FACE_URL = "face_url";
    private static final String AGE = "age";
    private static final String SEX = "sex";

    public static void setLocalUserName(String userName) {
        PreferencesUtils.putString(User.USER_NAME, userName);
    }

    public static String getLocalUserName() {
        return PreferencesUtils.getString(User.USER_NAME, "");
    }

    public static void setLocalUserPass(String userPass) {
        PreferencesUtils.putString(User.USER_PASS, Sec.DESEncrypt(userPass, "password"));
    }

    public static String getLocalUserPass() {
        return Sec.DESDecrypt(PreferencesUtils.getString(User.USER_PASS, ""), "password");
    }

    public static void saveUserInfo(RespLogin info) {
        PreferencesUtils.putInt(User.USER_ID, info.getId());
        PreferencesUtils.putString(User.NAME, info.getUser_name());

        PreferencesUtils.putString(User.PHONE_NUMBER, info.getMobile());
        PreferencesUtils.putString(User.MEMBER_ID, String.valueOf(info.getMember_id()));

        PreferencesUtils.putString(User.AUTH_TOKEN, info.getAuth_token());
        PreferencesUtils.putString(User.EMAIL, info.getEmail());
    }

    public static void clearUserInfo() {
        PreferencesUtils.putInt(User.USER_ID, -1);
        PreferencesUtils.putString(User.NAME, "");

        PreferencesUtils.putString(User.PHONE_NUMBER, "");
        PreferencesUtils.putString(User.MEMBER_ID, "");

        PreferencesUtils.putString(User.AUTH_TOKEN, "");
        PreferencesUtils.putString(User.EMAIL, "");

        PreferencesUtils.putString(User.USER_PASS, "");

        PreferencesUtils.putString(Constants.SUBJECT, "");
        PreferencesUtils.putString(Constants.TEACHER, "");
        PreferencesUtils.putString(Constants.CARNUM, "");
        PreferencesUtils.putString(Constants.TRAINED, "");
        PreferencesUtils.putString(Constants.NO_TRAINED, "");
        PreferencesUtils.putString(Constants.TEACHER_PHONE, "");

        PreferencesUtils.putString(Constants.NICKNAME, "");

        PreferencesUtils.putString(Constants.SUBJECT, "");
        PreferencesUtils.putString(Constants.TEACHER, "");
        PreferencesUtils.putString(Constants.CARNUM, "");
        PreferencesUtils.putString(Constants.TRAINED, "");
        PreferencesUtils.putString(Constants.NO_TRAINED, "");
        PreferencesUtils.putString(Constants.TEACHER_PHONE, "");

        PreferencesUtils.putString(Constants.CORP_ID,"");
        PreferencesUtils.putString(Constants.COACH_ID,"");

        PreferencesUtils.putString(Constants.TRAINED_ITEM_ID ,"");

        PreferencesUtils.putString(Constants.SCHOOL,"");
    }

    public static int getUserId() {
        return PreferencesUtils.getInt(User.USER_ID, -1);
    }

    public static String getName() {
        return PreferencesUtils.getString(User.NAME, "");
    }

    public static String getPhoneNumber() {
        return PreferencesUtils.getString(User.PHONE_NUMBER, "");
    }

    public static String getMemberId() {
        return PreferencesUtils.getString(User.MEMBER_ID, "");
    }

    public static String getAuthToken() {
        return PreferencesUtils.getString(User.AUTH_TOKEN, "");
    }

    public static String getEMAIL() {
        return PreferencesUtils.getString(User.EMAIL, "");
    }

}
