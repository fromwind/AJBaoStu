package com.udit.aijiabao.entitys;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class BookingsBean {

    private String message;
    private boolean enable;
    private String date;
    private String week;
    private List<ItemsBean> items;

    public String getMessage() {
        return message;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getDate() {
        return date;
    }

    public String getWeek() {
        return week;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "BookingsBean{" +
                "message='" + message + '\'' +
                ", enable=" + enable +
                ", date='" + date + '\'' +
                ", week='" + week + '\'' +
                ", items=" + items +
                '}';
    }
}
