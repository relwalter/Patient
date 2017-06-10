package com.patient.framework.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.patient.framework.model.PatientSign;
import com.patient.framework.service.DBConnector;


public class PatientSignRepository {
    private SQLiteDatabase db;

    public PatientSignRepository(Context context){
        db=new DBConnector(context).getWritableDatabase();
    }

    public int addPatientSignSign(PatientSign patientSign){
        if(patientSign!=null){
            Cursor cursor=db.rawQuery("SELECT * FROM sign WHERE pid=?",new String[]{Integer.toString(patientSign.getPid())});
            if (cursor.getCount()>0){
                cursor.close();
                return -1;
            }else{
                cursor.close();
                try{
                    db.execSQL("INSERT INTO sign(pid,height,weight,temp,breath,pulse,pressure,blsugar,more) values(?,?,?,?,?,?,?,?,?)",patientSign.getAll());
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


    public boolean updatePatientSignSign(PatientSign patientSign){
        try{
            db.execSQL("UPDATE sign SET height=?,weight=?,temp=?,breath=?,pulse=?,pressure=?,blsugar=?,more=? WHERE pid=?", new Object[]{patientSign.getHeight(),patientSign.getWeight(),patientSign.getTemp(),
                    patientSign.getBreath(),patientSign.getPulse(),patientSign.getPressure(),
                    patientSign.getBlsugar(),patientSign.getMore(),patientSign.getPid()});
            // new Object[]{patientSign.getHeight(),patientSign.getWeight(),patientSign.getTemp(),patientSign.getBreath(),patientSign.getPulse(),patientSign.getPressure(),patientSign.getBlsugar(),patientSign.getMore()}
            return true;
        }catch (Exception e) {
            Log.d("错误", e.getMessage().toString());
            return false;
        }
    }


    public boolean deletePatientSign(PatientSign patientSign){
        try{
            db.execSQL("DELETE FROM sign WHERE pid=?",new Object[]{patientSign.getPid()});
            return true;
        }catch (Exception e) {
            Log.d("错误", e.getMessage().toString());
            return false;
        }
    }

    public boolean checkPatientSign(int pid){
        if(pid>=0){
            Cursor cursor=db.rawQuery("SELECT * FROM sign WHERE pid=?"
                    ,new String[]{Integer.toString(pid)});
            if (cursor.getCount()>0){
                cursor.close();
                return true;
            }
        }
        return false;
    }

    public PatientSign getPatientSign(int pid){
        Cursor cursor =db.rawQuery("SELECT * FROM sign WHERE pid=?",new String[]{Integer.toString(pid)});
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            return new PatientSign(cursor.getInt(0),cursor.getInt(1),cursor.getFloat(2),cursor.getFloat(3),cursor.getFloat(4),cursor.getFloat(5),cursor.getFloat(6),cursor.getString(7),cursor.getFloat(8),cursor.getString(9));
        }else{
            cursor.close();
            return null;
        }
    }
}


