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
                return -1;
            }else{
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


    public boolean updatePatientInfo(Patient patient){
        try{
            db.execSQL("UPDATE patient SET name=?,gender=?,age=? WHERE card=?",
                    new Object[]{patient.getName(),patient.getGender(),patient.getAge(),patient.getCard()});
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
}
