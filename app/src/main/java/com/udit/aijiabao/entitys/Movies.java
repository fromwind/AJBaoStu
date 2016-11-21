package com.udit.aijiabao.entitys;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "movie")
public class Movies {
    @Column(name = "id",isId = true,autoGen = false)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "img")
    private int img;
    @Column(name = "url")
    private String url;
    @Column(name = "time")
    private String time;
    @Column(name = "size")
    private double size;
    @Column(name = "percent")
    private double precent=0;

    @Column(name = "project")
    private String project;//2or3
    public Movies() {
    }

    public Movies(int id,String name, int img, String url, String time,double size,String project) {
        this.id=id;
        this.name = name;
        this.img = img;
        this.url = url;
        this.time = time;
        this.size=size;
        this.project=project;
    }

    public double getPrecent() {
        return precent;
    }

    public void setPrecent(double precent) {
        this.precent = precent;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img=" + img +
                ", url='" + url + '\'' +
                ", time='" + time + '\'' +
                ", size=" + size +
                '}';
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public double getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
