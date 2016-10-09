package com.udit.aijiabao.entitys;

/**
 * Created by Administrator on 2016/5/12.
 */
public class RespLogin {


    /**
     * message :
     * id : 280
     * user_name : 小华0
     * email :
     * auth_token : 712c633e-fc4f-472d-ba91-0dfb6bf094ed
     * member_id : 45326
     * mobile : 18751949811
     */

    private String message;
    private int id;
    private String user_name;
    private String email;
    private String auth_token;
    private int member_id;
    private String mobile;
    private boolean result=true;

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getEmail() {
        return email;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public int getMember_id() {
        return member_id;
    }

    public String getMobile() {
        return mobile;
    }

    public boolean isResult() {
        return result;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RespLogin{" +
                "message='" + message + '\'' +
                ", id=" + id +
                ", user_name='" + user_name + '\'' +
                ", email='" + email + '\'' +
                ", auth_token='" + auth_token + '\'' +
                ", member_id=" + member_id +
                ", mobile='" + mobile + '\'' +
                ", result=" + result +
                '}';
    }
}
