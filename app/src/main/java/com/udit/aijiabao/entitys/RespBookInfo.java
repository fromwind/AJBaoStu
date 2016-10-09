package com.udit.aijiabao.entitys;

/**
 * Created by Administrator on 2016/5/17.
 */
public class RespBookInfo {


    private boolean result;
    private String notice;
    private String message;

    private String error;

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public boolean isResult() {
        return result;
    }


    public String getNotice() {
        return notice;
    }

}
