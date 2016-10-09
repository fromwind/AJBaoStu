package com.udit.aijiabao.entitys;

/**
 * Created by Administrator on 2016/5/13.
 */
public class TrainingItemsBean {

    private String coach_mobile;
    private String coach_id;
    private String id;
    private String coach;
    private String name;
    private String no_training_times;
    private String total_times;
    private String trained_times;
    private String car_no;

    public String getCoach_mobile() {
        return coach_mobile;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public String getId() {
        return id;
    }

    public String getCoach() {
        return coach;
    }

    public String getName() {
        return name;
    }

    public String getNo_training_times() {
        return no_training_times;
    }

    public String getTotal_times() {
        return total_times;
    }

    public String getTrained_times() {
        return trained_times;
    }

    public String getCar_no() {
        return car_no;
    }

    @Override
    public String toString() {
        return "TrainingItemsBean{" +
                "coach_mobile='" + coach_mobile + '\'' +
                ", coach_id='" + coach_id + '\'' +
                ", id='" + id + '\'' +
                ", coach='" + coach + '\'' +
                ", name='" + name + '\'' +
                ", no_training_times='" + no_training_times + '\'' +
                ", total_times='" + total_times + '\'' +
                ", trained_times='" + trained_times + '\'' +
                ", car_no='" + car_no + '\'' +
                '}';
    }
}
