package com.patient.framework.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnector extends SQLiteOpenHelper{

    private static final String name = "patient"; //数据库名称
    private static final int version = 1; //数据库版本

    public DBConnector(Context context) {
        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user(uid integer primary key autoincrement,card varchar(20),eml varchar(40),psw varchar(20),phone varchar(15))");
        db.execSQL("CREATE TABLE IF NOT EXISTS patient(pid integer primary key autoincrement,name varchar(20),gender varchar(10),age integer,card varchar(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS sign(sid integer primary key autoincrement,pid integer,height float,weight float,temp float,breath float,pulse float,pressure varchar(10),blsugar float,more text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS queue(qid integer primary key autoincrement,pid integer,deid integer,rid integer,drid integer,timestamp date,start double,end double,stayed integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
