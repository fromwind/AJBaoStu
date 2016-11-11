package com.udit.aijiabao.entitys;

public class Movies {

    private String name;
    private int img;
    private String path;
    private boolean isLocal;
    private String time;

    public Movies(String name, int img, String path, boolean isLocal, String time) {
        this.name = name;
        this.img = img;
        this.path = path;
        this.isLocal = isLocal;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", path='" + path + '\'' +
                ", isLocal=" + isLocal +
                ", time='" + time + '\'' +
                '}';
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
