package com.udit.aijiabao.entitys;

import java.util.List;

/**
 * Created by Administrator on 2016/5/20.
 */
public class RespCommentList {

    private String message;

    private boolean result=true;

    public String getMessage() {
        return message;
    }

    public boolean isResult() {
        return result;
    }

    private String fwtd;
    private String jxnl;
    private String count;
    private String jxsf;
    private String clzk;
    private String ztmyd;
    private String jxcd;
    private String cnzj;

    private List<CommentListBean> commentList;


    public String getFwtd() {
        return fwtd;
    }

    public String getJxnl() {
        return jxnl;
    }

    public String getCount() {
        return count;
    }

    public String getJxsf() {
        return jxsf;
    }

    public String getClzk() {
        return clzk;
    }

    public String getZtmyd() {
        return ztmyd;
    }

    public String getJxcd() {
        return jxcd;
    }

    public String getCnzj() {
        return cnzj;
    }

    public List<CommentListBean> getCommentList() {
        return commentList;
    }
}
