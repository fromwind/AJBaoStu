package com.udit.aijiabao.entitys;

import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public class RespSignInfo {


    /**
     * id : 303
     * auth_token : fe10a11b-8af8-4d10-ad9f-f3d0d14d1b48
     * user_name : 游客
     * email :
     * mobile : 18555607088
     * training_items : []
     */

    private int id;
    private String auth_token;
    private String user_name;
    private String email;
    private String mobile;
    private List<TrainingItemsBean> training_items;

    public int getId() {
        return id;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public List<TrainingItemsBean> getTraining_items() {
        return training_items;
    }
}
