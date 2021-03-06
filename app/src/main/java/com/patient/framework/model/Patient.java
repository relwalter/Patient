package com.patient.framework.model;

import java.util.Random;

public class Patient {
    int pid;
    String name;
    String gender;
    int age;
    String card;

    public Patient() {
    }

    public Patient(String name, String gender, int age, String card) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.card = card;
    }

    public Patient(int pid, String name, String gender, int age, String card) {
        this.pid = pid;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.card = card;
    }

    public int getId() {
        return pid;
    }

    public void setId(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "pid=" + pid +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", card='" + card + '\'' +
                '}';
    }

    public String[] getAll(){
        return new String[]{name,gender,Integer.toString(age),card};
    }

    public String[] getInfo(){
        return new String[]{Integer.toString(pid),name,gender,Integer.toString(age),card};
    }

    
}
