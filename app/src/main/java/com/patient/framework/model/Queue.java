package com.patient.framework.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Queue {
    private int qId;
    private int pId;
    private int deId;
    private int rId;
    private int drId;
    private Date timeStamp;
    private double startTime;
    private double endTime;
    private int stayed;

    public Queue() {
    }

    public Queue(int qId, int pId, int deId, int rId, int drId, Date date, double startTime, double endTime, int stayed) {
        this.qId = qId;
        this.pId = pId;
        this.deId = deId;
        this.rId = rId;
        this.drId = drId;
        this.timeStamp = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.stayed = stayed;
    }

    public Queue(int pId, int deId, int rId, int drId, Date date, double startTime, double endTime, int stayed) {
        this.pId = pId;
        this.deId = deId;
        this.rId = rId;
        this.drId = drId;
        this.timeStamp = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.stayed = stayed;
    }

    public int getQId() {
        return qId;
    }

    public void setQId(int qid) {
        this.qId = qid;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getDeId() {
        return deId;
    }

    public void setDeId(int deId) {
        this.deId = deId;
    }

    public int getrId() {
        return rId;
    }

    public void setrId(int rId) {
        this.rId = rId;
    }

    public int getDrId() {
        return drId;
    }

    public void setDrId(int drId) {
        this.drId = drId;
    }

    public int getqId() {
        return qId;
    }

    public void setqId(int qId) {
        this.qId = qId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date date) {
        this.timeStamp = date;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public int getStayed() {
        return stayed;
    }

    public void setStayed(int stayed) {
        this.stayed = stayed;
    }

    @Override
    public String toString() {
        return "Queue{" +
                "qId=" + qId +
                ", pId=" + pId +
                ", deId=" + deId +
                ", rId=" + rId +
                ", drId=" + drId +
                ", date=" + timeStamp +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", stayed=" + stayed +
                '}';
    }

    public String[] getAll(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new String[]{Integer.toString(pId),Integer.toString(deId),Integer.toString(rId),Integer.toString(drId),dateFormat.format(timeStamp),Double.toString(startTime),Double.toString(endTime),Integer.toString(stayed)};
    }

    public String[] getInfo(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new String[]{Integer.toString(qId),Integer.toString(pId),Integer.toString(deId),Integer.toString(rId),Integer.toString(drId),dateFormat.format(timeStamp),Double.toString(startTime),Double.toString(endTime),Integer.toString(stayed)};
    }
}
