package com.udit.aijiabao.entitys;

/**
 * Created by Administrator on 2016/5/13.
 */
public class RespUserInfo {

    private String coach_mobile;
    private int sex;
    private int corp_id;
    private int training_item_id;
    private String training_item_name;
    private String tel;
    private String link_address;
    private CorpBean corp;
    private TrainingItemsBean training_items;
    private int member_id;
    private int coach_id;
    private String level;
    private String email;
    private String card_code;
    private String coach_tel;
    private String name;
    private String created_at;
    private String car_no;
    private String coach_name;

    private  String  nickname;

    public String getNickname() {
        return nickname;
    }

    private String error;

    public String getError() {
        return error;
    }

    public String getCoach_mobile() {
        return coach_mobile;
    }

    public int getSex() {
        return sex;
    }

    public int getCorp_id() {
        return corp_id;
    }

    public int getTraining_item_id() {
        return training_item_id;
    }

    public String getTraining_item_name() {
        return training_item_name;
    }

    public String getTel() {
        return tel;
    }

    public String getLink_address() {
        return link_address;
    }

    public CorpBean getCorp() {
        return corp;
    }

    public TrainingItemsBean getTraining_items() {
        return training_items;
    }

    public int getMember_id() {
        return member_id;
    }

    public int getCoach_id() {
        return coach_id;
    }

    public String getLevel() {
        return level;
    }

    public String getEmail() {
        return email;
    }

    public String getCard_code() {
        return card_code;
    }

    public String getCoach_tel() {
        return coach_tel;
    }

    public String getName() {
        return name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getCar_no() {
        return car_no;
    }

    public String getCoach_name() {
        return coach_name;
    }

    @Override
    public String toString() {
        return "RespUserInfo{" +
                "coach_mobile='" + coach_mobile + '\'' +
                ", sex=" + sex +
                ", corp_id=" + corp_id +
                ", training_item_id=" + training_item_id +
                ", training_item_name='" + training_item_name + '\'' +
                ", tel='" + tel + '\'' +
                ", link_address='" + link_address + '\'' +
                ", corp=" + corp +
                ", training_items=" + training_items +
                ", member_id=" + member_id +
                ", coach_id=" + coach_id +
                ", level='" + level + '\'' +
                ", email='" + email + '\'' +
                ", card_code='" + card_code + '\'' +
                ", coach_tel='" + coach_tel + '\'' +
                ", name='" + name + '\'' +
                ", created_at='" + created_at + '\'' +
                ", car_no='" + car_no + '\'' +
                ", coach_name='" + coach_name + '\'' +
                '}';
    }
}
