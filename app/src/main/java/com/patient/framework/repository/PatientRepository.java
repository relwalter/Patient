package com.patient.framework.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.patient.framework.model.Patient;
import com.patient.framework.service.DBConnector;

public class PatientRepository {
    private SQLiteDatabase db;

    public PatientRepository(Context context){
        db=new DBConnector(context).getWritableDatabase();
    }

    public int addPatient(Patient patient){
        if(patient!=null){
            Cursor cursor=db.rawQuery("SELECT * FROM patient WHERE card=?",new String[]{patient.getCard()});
            if (cursor.getCount()>0){
                cursor.close();
                return -1;
            }else{
                cursor.close();
                try{
                    db.execSQL("INSERT INTO patient(name,gender,age,card) values(?,?,?,?)",patient.getAll());
                    return 1;
                }catch (Exception e) {
                    Log.d("错误", e.getMessage().toString());
                    return 0;
                }
            }
        }else{
            return 0;
        }
    }


    public boolean updatePatientInfo(Patient patient,String oldCard){
        try{
            db.execSQL("UPDATE patient SET name=?,gender=?,age=?,card=? WHERE card=?",
                    new Object[]{patient.getName(),patient.getGender(),patient.getAge(),patient.getCard(),oldCard});
            return true;
        }catch (Exception e) {
            Log.d("错误", e.getMessage().toString());
            return false;
        }
    }


    public boolean deletePatient(Patient patient){
        try{
            db.execSQL("DELETE FROM patient WHERE card=?",new String[]{patient.getCard()});
            return true;
        }catch (Exception e) {
            Log.d("错误", e.getMessage().toString());
            return false;
        }
    }

    public boolean checkPatient(String card){
        if(card!=null){
            Cursor cursor=db.rawQuery("SELECT * FROM patient WHERE card=?",new String[]{card});
            if (cursor.getCount()>0){
                cursor.close();
                return true;
            }
        }
        return false;
    }

    public Patient getPatient(String card){
        Cursor cursor =db.rawQuery("SELECT * FROM patient WHERE card=?",new String[]{card});
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            return new Patient(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4));
        }else{
            cursor.close();
            return null;
        }
    }
}
