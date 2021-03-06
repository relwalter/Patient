package com.patient.framework.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.patient.framework.model.User;
import com.patient.framework.service.DBConnector;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static UserRepository userRepository;
    private SQLiteDatabase db;

    public UserRepository(Context context){
        DBConnector connector=new DBConnector(context);
        db=connector.getWritableDatabase();
    }

    public synchronized static UserRepository getInstance(Context context) {
        if (userRepository == null) {
            userRepository = new UserRepository(context);
        }
        return userRepository;
    }

    public boolean checkUser(User user){
        if(user!=null) {
            Cursor cursor = db.rawQuery("SELECT * FROM user WHERE eml=?", new String[]{user.getEml()});
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
        }
        return false;
    }

    public int addUser(User user){
        if(user!=null){
            Cursor cursor=db.rawQuery("SELECT * FROM user WHERE eml=?",new String[]{user.getEml()});
            if (cursor.getCount()>0){
                cursor.close();
                return -1;
            }else{
                cursor.close();
                try{
                    db.execSQL("INSERT INTO user(card,eml,psw,phone) values(?,?,?,?)",user.getAll());
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

    public int isValid(User user){
        Cursor cursor1 = db.rawQuery("SELECT * FROM user WHERE eml=?",new String[]{user.getEml()});
        if(cursor1.getCount()>0){
            cursor1.close();
            Cursor cursor2 = db.rawQuery("SELECT * FROM user WHERE eml=? AND psw=?",new String[]{user.getEml(),user.getPsw()});
            if(cursor2.getCount()>0){
                cursor2.close();
                return 1;
            }else{
                cursor2.close();
                return 0;
            }
        }else{
            cursor1.close();
            return -1;
        }

    }

    public boolean updateUserInfo(User user){
        try{
            db.execSQL("UPDATE user SET card=?,phone=? WHERE eml=?",
                    new String[]{user.getCard(),user.getPhone(),user.getEml()});
            return true;
        }catch (Exception e) {
            Log.d("错误", e.getMessage().toString());
            return false;
        }
    }

    public boolean updateUserPsw(User user,String newPsw){
        try{
            db.execSQL("UPDATE user SET psw=? WHERE eml=? AND psw=?",new String[]{newPsw,user.getEml(),user.getPsw()});
            return true;
        }catch (Exception e) {
            Log.d("错误", e.getMessage().toString());
            return false;
        }
    }

    public int resetUserPsw(User user){
        Cursor cursor =db.rawQuery("SELECT * FROM user WHERE eml=? AND card=?",new String[]{user.getEml(),user.getCard()});
        if(cursor.getCount()>0){
            cursor.close();
            try{
                db.execSQL("UPDATE user SET psw=? WHERE eml=?",new String[]{user.getPsw(),user.getEml()});
                return 1;
            }catch (Exception e) {
                Log.d("错误", e.getMessage().toString());
                return 0;
            }
        }else{
            return -1;
        }
    }

    public boolean deleteUser(User user){
        try{
            db.execSQL("DELETE FROM user WHERE eml=? AND psw=?",new String[]{user.getEml(),user.getPsw()});
            return true;
        }catch (Exception e) {
            Log.d("错误", e.getMessage().toString());
            return false;
        }
    }

    public User getUser(String eml){
        Cursor cursor =db.rawQuery("SELECT * FROM user WHERE eml=?",new String[]{eml});
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            return new User(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
        }else{
            cursor.close();
            return null;
        }
    }
}
