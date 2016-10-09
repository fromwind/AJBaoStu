package com.udit.aijiabao.entitys;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class RespAppointInfo {

    private String end_date;
    private List<BookingsBean> bookings;

    public String getEnd_date() {
        return end_date;
    }

    public List<BookingsBean> getBookings() {
        return bookings;
    }

    @Override
    public String toString() {
        return "RespAppointInfo{" +
                "end_date='" + end_date + '\'' +
                ", bookings=" + bookings +
                '}';
    }
}
