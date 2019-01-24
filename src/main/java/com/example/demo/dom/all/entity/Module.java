package com.example.demo.dom.all.entity;

import java.io.Serializable;

/**
 * module
 * @author 
 */
public class Module implements Serializable {
    private Integer mid;

    private String mname;

    private static final long serialVersionUID = 1L;

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }
}