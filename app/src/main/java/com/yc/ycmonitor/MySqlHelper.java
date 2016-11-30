package com.yc.ycmonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 86799 on 2016/11/10.
 * MySQLHelper
 */
public class MySqlHelper extends SQLiteOpenHelper{

    public MySqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db){ //创建表
        //创建表people_info的sql语句
        String sql_info = "create table people_info(name varchar(20),ID int primary key,identity char(20),professional varchar(20))";
        String sql_time = "create table people_time(ID int,time datetime,primary key(ID,time))";
        db.execSQL(sql_info); //执行sql语句
        db.execSQL(sql_time);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){ //更新版本

    }
}
