package com.patient.framework.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.patient.framework.model.Queue;
import com.patient.framework.service.DBConnector;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QueueRepository {

    private SQLiteDatabase db;

    public QueueRepository(Context context){
        db=new DBConnector(context).getWritableDatabase();
    }

    public int onQueue(Queue queue){
        if(queue!=null){
            Cursor cursor=db.rawQuery("SELECT * FROM queue WHERE qid=?",
                    new String[]{Integer.toString(queue.getQId())});
            if (cursor.getCount()>0){
                cursor.close();
                return -1;
            }else{
                cursor.close();
                try{
                    db.execSQL("INSERT INTO queue(pid,deid,rid,drid,start,stayed) values(?,?,?,?,?,?)",
                            new Object[]{queue.getpId(),queue.getDeId(),queue.getrId(),queue.getDrId(),
                                    queue.getStartTime(),queue.getStayed()});
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

    public boolean offQueue(Queue queue){
        try{
            db.execSQL("UPDATE queue SET stayed=?,end=? WHERE qid=?",
                    new Object[]{queue.getStayed(),queue.getEndTime(),queue.getQId()});
            return true;
        }catch (Exception e) {
            Log.d("错误", e.getMessage().toString());
            return false;
        }
    }

    public Queue getQueue(int pid,int stayed){
        Cursor cursor =db.rawQuery("SELECT * FROM queue WHERE pid=? AND stayed=?",new String[]{Integer.toString(pid),Integer.toString(stayed)});
        if(cursor.getCount()>0){
            try {
                cursor.moveToFirst();
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timeStamp=cursor.getString(5);
                if(timeStamp==null||"".equals(timeStamp)){
                    timeStamp=dateFormat.format(new Date());
                }
                return new Queue(cursor.getInt(0),cursor.getInt(1),
                        cursor.getInt(2),cursor.getInt(3),
                        cursor.getInt(4),dateFormat.parse(timeStamp),
                        cursor.getDouble(6),cursor.getDouble(7),
                        cursor.getInt(8));
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }else{
            cursor.close();
            return null;
        }
    }

    public int getBefore(int qid){
        Cursor cursor =db.rawQuery("SELECT COUNT(*) count FROM queue WHERE stayed=1 AND qid<? ORDER BY start",new String[]{Integer.toString(qid)});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }else{
            return 0;
        }
    }

    public int getAfter(int qid){
        Cursor cursor =db.rawQuery("SELECT COUNT(*) count FROM queue WHERE stayed=1 AND qid>? ORDER BY start",new String[]{Integer.toString(qid)});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }else{
            return 0;
        }
    }

}
