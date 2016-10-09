package com.udit.aijiabao.entitys;

/**
 * Created by Administrator on 2016/5/17.
 */
public class ItemsBean {

    private int available_count;
    private String message;
    private boolean booked;
    private boolean enable;
    private String period;

    @Override
    public String toString() {
        return "ItemsBean{" +
                "available_count=" + available_count +
                ", message='" + message + '\'' +
                ", booked=" + booked +
                ", enable=" + enable +
                ", period='" + period + '\'' +
                '}';
    }

    public int getAvailable_count() {
        return available_count;
    }

    public String getMessage() {
        return message;
    }

    public boolean isBooked() {
        return booked;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getPeriod() {
        return period;
    }
}
