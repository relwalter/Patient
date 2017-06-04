package com.patient.framework.model;

public class User {
    int uid;
    String card;
    String eml;
    String psw;
    String phone;

    public User() {
    }

    public User(int uid, String card, String eml, String psw, String phone) {
        this.uid = uid;
        this.card = card;
        this.eml = eml;
        this.psw = psw;
        this.phone = phone;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getEml() {
        return eml;
    }

    public void setEml(String eml) {
        this.eml = eml;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", card=" + card +
                ", eml='" + eml + '\'' +
                ", psw='" + psw + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String[] getAll(){
        return new String[]{card,eml,psw,phone};
    }
}
