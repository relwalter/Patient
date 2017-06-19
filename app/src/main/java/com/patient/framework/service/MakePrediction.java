package com.patient.framework.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.patient.framework.model.*;
import com.patient.framework.repository.QueueRepository;
import com.patient.framework.utils.Generator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class MakePrediction{
    private final String target="https://vottery.higherloft.com/pred?";
    private Patient patient;
    private PatientSign patientSign;
    private Queue queue;
    private QueueRepository queueService;
    private int patientId,patientAge,before,hourOfDay,dayOfWeek;
    private double patientSignIndex,startTime,spentWithDoctor;

    public MakePrediction(Context context,Patient patient,Queue queue){
        this.queueService=new QueueRepository(context);
        this.patient=patient;
        this.queue=queue;
        initiate();
    }

    private boolean initiate(){
        try {
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(queue.getTimeStamp());
            Generator generator=new Generator();
            this.patientId=patient.getId();
            this.patientAge=patient.getAge();
            this.before=queueService.getBefore(queue.getQId());
            this.hourOfDay=calendar.get(Calendar.HOUR_OF_DAY);
            this.dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
            this.patientSignIndex=(double) generator.getRand("ps",this.patientAge);
            this.startTime=queue.getStartTime();
            this.spentWithDoctor=(double) generator.getRand("swd",this.patientSignIndex);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public String getResult(){
        String postData="feat="+"["+patientId+","+patientAge+","+patientSignIndex+","+before+","+
                startTime+","+hourOfDay+","+dayOfWeek+","+spentWithDoctor+"]";
        Log.d("data",postData);
        HttpURLConnection connection=getConnection(postData);
        try{
            String result;
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader
                    (connection.getInputStream()));
            while((result=bufferedReader.readLine())!=null){
                Log.d("result: ",result);
                return result.replace("[","").replace("]","")
                        .replace("-","").replace(" ","");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private HttpURLConnection getConnection(String extra){
        try{
            URL url=new URL(this.target+extra);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if(connection.getResponseCode()==200){
                return connection;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "MakePrediction{" +
                "patient=" + patient +
                ", patientId=" + patientId +
                ", patientAge=" + patientAge +
                ", before=" + before +
                ", hourOfDay=" + hourOfDay +
                ", dayOfWeek=" + dayOfWeek +
                ", patientSignIndex=" + patientSignIndex +
                ", startTime=" + startTime +
                ", spentWithDoctor=" + spentWithDoctor +
                '}';
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PatientSign getPatientSign() {
        return patientSign;
    }

    public void setPatientSign(PatientSign patientSign) {
        this.patientSign = patientSign;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public QueueRepository getQueueService() {
        return queueService;
    }

    public void setQueueService(QueueRepository queueService) {
        this.queueService = queueService;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public int getBefore() {
        return before;
    }

    public void setBefore(int before) {
        this.before = before;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public double getPatientSignIndex() {
        return patientSignIndex;
    }

    public void setPatientSignIndex(double patientSignIndex) {
        this.patientSignIndex = patientSignIndex;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getSpentWithDoctor() {
        return spentWithDoctor;
    }

    public void setSpentWithDoctor(double spentWithDoctor) {
        this.spentWithDoctor = spentWithDoctor;
    }
}
