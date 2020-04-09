package com.example.demo.dom.all.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * captcha
 *
 * @author
 */
public class Captcha implements Serializable {
    private String uuid;

    private String code;

    private Date time;

    private static final long serialVersionUID = 1L;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}