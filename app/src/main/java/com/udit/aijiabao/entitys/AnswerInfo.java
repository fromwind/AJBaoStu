package com.udit.aijiabao.entitys;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "AnswerInfo")
public class AnswerInfo {
    @Column(name = "questionId", isId = true, autoGen = false)
    public String questionId; // 试题主键
    @Column(name = "questionName")
    public String questionName; // 试题题目
    @Column(name = "QuestionType")
    public String questionType; // 试题类型
    @Column(name = "analysis")
    public String analysis; // 试题分析
    @Column(name = "correctAnswer")
    public String correctAnswer; // 正确答案
    @Column(name = "optionA")
    public String optionA; // 正确答案A
    @Column(name = "optionB")
    public String optionB; // 正确答案B
    @Column(name = "optionC")
    public String optionC; // 正确答案C
    @Column(name = "optionD")
    public String optionD; // 正确答案D
    @Column(name = "optionE")
    public String optionE; // 正确答案E
    @Column(name = "score")
    public String score; // 分值
    @Column(name = "isSelect")
    public String isSelect;//用户的选项
    @Column(name = "option_type")
    public String option_type;
    @Column(name = "img")
    public String img; // 是否是图片题0是1否
    @Column(name = "random")
    public int random = 0;

    public String getQuestionId() {
        return questionId;
    }
    public AnswerInfo(){}
    public AnswerInfo(String questionId, String questionName, String questionType, String analysis, String
            correctAnswer, String optionA, String optionB, String optionC, String optionD, String optionE, String
                              score, String img) {
        this.questionId = questionId;
        this.questionName = questionName;
        this.questionType = questionType;
        this.analysis = analysis;
        this.correctAnswer = correctAnswer;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.optionE = optionE;
        this.score = score;
        this.img = img;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }

    public String getOption_type() {
        return option_type;
    }

    public void setOption_type(String option_type) {
        this.option_type = option_type;
    }

    public int getRandom() {
        return random;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getOptionE() {
        return optionE;
    }

    public void setOptionE(String optionE) {
        this.optionE = optionE;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
