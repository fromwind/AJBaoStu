package com.udit.aijiabao.entitys;

/**
 * Created by Administrator on 2016/5/13.
 */
public class CorpBean {

    private String id;
    private String address;
    private String tel;
    private String name;

    @Override
    public String toString() {
        return "CorpBean{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getTel() {
        return tel;
    }

    public String getName() {
        return name;
    }
}
