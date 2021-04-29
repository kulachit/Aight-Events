package com.example.aightevents;

import java.io.Serializable;

public class Filter implements Serializable {
    private String time;
    private String college;
    private String category;
    private Integer flag;

    public Filter(){

    }

    public Filter(String time, String category, String college){
        this.time = time;
        this.category = category;
        this.college = college;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private static Filter instance;

    public static Filter getInstance(){
        if(instance == null){
            instance = new Filter();
        }
        return instance;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
