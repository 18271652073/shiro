package com.example.demo.dom.all.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * time
 * @author 
 */
public class Time implements Serializable {
    private Date timestamp;

    private Date datetime;

    private Date date;

    private static final long serialVersionUID = 1L;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}