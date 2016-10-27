package com.udit.aijiabao.entitys;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/10/24.
 */
@Table(name = "real")
public class RealTest {
    @Column(name = "id", isId = true)
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
    @Column(name = "youranswer")
    public String yourAnswer;
    public RealTest(){}
    public RealTest(String topicId, String correctAnswer, String topic, String optionA, String optionB,
                    String optionC, String optionD, String yourAnswer) {
        this.topicId = topicId;
        this.correctAnswer = correctAnswer;
        this.topic = topic;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.yourAnswer = yourAnswer;
    }

    public RealTest(Question question, String yourAnswer){
        this.topicId=question.getTopicId();
        this.correctAnswer=question.getCorrectAnswer();
        this.topic=question.getTopic();
        this.optionA=question.getOptionA();
        this.optionB=question.getOptionB();
        this.optionC=question.getOptionC();
        this.optionD=question.getOptionD();
        this.yourAnswer=yourAnswer;
    }
    public RealTest(Question question){
        this.topicId=question.getTopicId();
        this.correctAnswer=question.getCorrectAnswer();
        this.topic=question.getTopic();
        this.optionA=question.getOptionA();
        this.optionB=question.getOptionB();
        this.optionC=question.getOptionC();
        this.optionD=question.getOptionD();
    }
    @Override
    public String toString() {
        return "RealTest{" +
                "id=" + id +
                ", topicId='" + topicId + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", topic='" + topic + '\'' +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                ", yourAnswer='" + yourAnswer + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getYourAnswer() {
        return yourAnswer;
    }

    public void setYourAnswer(String yourAnswer) {
        this.yourAnswer = yourAnswer;
    }

}
