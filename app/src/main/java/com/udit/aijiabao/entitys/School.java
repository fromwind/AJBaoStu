package com.udit.aijiabao.entitys;

public class School {

    private String logo;
    private String distance;
    private String old_price;
    private String price;
    private String name;
    private String members_total;
    private String score;
    private String feature;
    private String signUpNum;
    private String comment_total;
    private String profile;
    private String corpId;

    public String getLogo() {
        return logo;
    }

    public String getDistance() {
        return distance;
    }

    public String getOld_price() {
        return old_price;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getMembers_total() {
        return members_total;
    }

    public String getScore() {
        return score;
    }

    public String getFeature() {
        return feature;
    }

    public String getSignUpNum() {
        return signUpNum;
    }

    public String getComment_total() {
        return comment_total;
    }

    public String getProfile() {
        return profile;
    }

    public String getCorpId() {
        return corpId;
    }

    @Override
    public String toString() {
        return "School{" +
                "logo='" + logo + '\'' +
                ", distance='" + distance + '\'' +
                ", old_price='" + old_price + '\'' +
                ", price='" + price + '\'' +
                ", name='" + name + '\'' +
                ", members_total='" + members_total + '\'' +
                ", score='" + score + '\'' +
                ", feature='" + feature + '\'' +
                ", signUpNum='" + signUpNum + '\'' +
                ", comment_total='" + comment_total + '\'' +
                ", profile='" + profile + '\'' +
                ", corpId='" + corpId + '\'' +
                '}';
    }
}
