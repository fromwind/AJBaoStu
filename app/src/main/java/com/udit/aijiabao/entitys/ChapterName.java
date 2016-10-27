package com.udit.aijiabao.entitys;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/10/20.
 */
@Table(name = "chapterName")
public class ChapterName {
    @Column(name = "id",isId = true,autoGen = false)
    public int id;
    @Column(name = "chapterNum")
    public String chapterNum;
    @Column(name = "chapterName")
    public String chapterName;
    @Column(name = "position")
    public int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getChapterNum() {
        return chapterNum;
    }

    public String getChapterName() {
        return chapterName;
    }

    @Override
    public String toString() {
        return "ChapterName{" +
                "chapterNum='" + chapterNum + '\'' +
                ", chapterName='" + chapterName + '\'' +
                ", position=" + position +
                '}';
    }
}
