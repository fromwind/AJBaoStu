package com.udit.aijiabao.entitys;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class Result {
    private Sk sk;

    private Today today;

    private List<Future> future ;

    public void setSk(Sk sk){
        this.sk = sk;
    }
    public Sk getSk(){
        return this.sk;
    }
    public void setToday(Today today){
        this.today = today;
    }
    public Today getToday(){
        return this.today;
    }
    public void setFuture(List<Future> future){
        this.future = future;
    }
    public List<Future> getFuture(){
        return this.future;
    }

    @Override
    public String toString() {
        return "Result{" +
                "sk=" + sk +
                ", today=" + today +
                ", future=" + future +
                '}';
    }

}
