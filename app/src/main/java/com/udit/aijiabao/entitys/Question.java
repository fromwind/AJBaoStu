package com.udit.aijiabao.entitys;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.view.annotation.ContentView;

/**
 * Created by Administrator on 2016/9/23.
 */
@Table(name = "question")
public class Question {
    @Column(name = "id",isId = true)
    public int id;
    @Column(name = "topicId")
    public String topicId;
    @Column(name = "correctAnswer")
    public String correctAnswer;//正确答案
    @Column(name = "topic")
    public String topic;   //题目
    @Column(name = "optionA")
    public String optionA; // 正确答案A
    @Column(name = "optionB")
    public String optionB; // 正确答案B
    @Column(name = "optionC")
    public String optionC; // 正确答案C
    @Column(name = "optionD")
    public String optionD; // 正确答案D
    @Column(name = "collect")
    public boolean collect;//是否收藏
    @Column(name = "error")
    public String error;//是否错题(上一次做错答案)

    public Question(){}

    public Question(String topicId, String correctAnswer, String topic, String optionA, String optionB, String
            optionC, String optionD) {
        this.topicId = topicId;
        this.correctAnswer = correctAnswer;
        this.topic = topic;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", topicId=" + topicId +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", topic='" + topic + '\'' +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                '}';
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
