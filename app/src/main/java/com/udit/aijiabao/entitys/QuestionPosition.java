package com.udit.aijiabao.entitys;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/10/24.
 */
@Table(name = "position")
public class QuestionPosition {
    @Column(name = "id",isId = true,autoGen = false)
    public int id;
    @Column(name = "type")
    public String type;
    @Column(name = "position")
    public int position;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "QuestionPosition{" +
                "type='" + type + '\'' +
                ", position=" + position +
                '}';
    }
}
