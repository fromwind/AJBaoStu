package com.udit.aijiabao.entitys;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "ErrorQuestionInfo")
public class ErrorQuestionInfo {
    @Column(name = "questionId", isId = true, autoGen = true)
    public int questionId;
    @Column(name = "questionName")
    public String questionName;
    @Column(name = "questionType")
    public String questionType;
    @Column(name = "questionAnswer")
    public String questionAnswer;
    @Column(name = "questionSelect")
    public String questionSelect;
    @Column(name = "isRight")
    public String isRight;
    @Column(name = "Analysis")
    public String Analysis;
    @Column(name = "optionA")
    public String optionA;
    @Column(name = "optionB")
    public String optionB;
    @Column(name = "optionC")
    public String optionC;
    @Column(name = "optionD")
    public String optionD;
    @Column(name = "optionE")
    public String optionE;
    @Column(name = "optionType")
    public String optionType;

    public ErrorQuestionInfo() {}

    public ErrorQuestionInfo(String questionName, String questionType, String questionAnswer, String
            questionSelect, String isRight, String analysis, String optionA, String optionB, String optionC, String
                                     optionD, String optionE, String optionType) {
        this.questionName = questionName;
        this.questionType = questionType;
        this.questionAnswer = questionAnswer;
        this.questionSelect = questionSelect;
        this.isRight = isRight;
        this.Analysis = analysis;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.optionE = optionE;
        this.optionType = optionType;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
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

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getQuestionSelect() {
        return questionSelect;
    }

    public void setQuestionSelect(String questionSelect) {
        this.questionSelect = questionSelect;
    }

    public String getIsRight() {
        return isRight;
    }

    public void setIsRight(String isRight) {
        this.isRight = isRight;
    }

    public String getAnalysis() {
        return Analysis;
    }

    public void setAnalysis(String analysis) {
        Analysis = analysis;
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

    public String getOptionE() {
        return optionE;
    }

    public void setOptionE(String optionE) {
        this.optionE = optionE;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

}
