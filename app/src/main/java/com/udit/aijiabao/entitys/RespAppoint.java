package com.udit.aijiabao.entitys;

import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class RespAppoint {

    private String traning_status;
    private int times;
    private int training_item_id;
    private int totaltimes;
    private String name;
    private String member_id;
    private List<Details> detail;

    public String getTraning_status() {
        return traning_status;
    }

    public int getTimes() {
        return times;
    }

    public int getTraining_item_id() {
        return training_item_id;
    }

    public int getTotaltimes() {
        return totaltimes;
    }

    public String getName() {
        return name;
    }

    public String getMember_id() {
        return member_id;
    }

    public List<Details> getDetail() {
        return detail;
    }
}
