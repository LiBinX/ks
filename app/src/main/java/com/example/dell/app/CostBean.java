package com.example.dell.app;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/28.
 */

public class CostBean implements Serializable{
    public String time;
    public String fee;

    public String gettime() {
        return time;
    }

    public void settime(String yunhao) {
        this.time = yunhao;
    }

    public String getfee() {
        return fee;
    }

    public void setfee(String huohao) {
        this.fee = huohao;
    }

}
