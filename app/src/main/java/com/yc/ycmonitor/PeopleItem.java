package com.yc.ycmonitor;

import android.graphics.Bitmap;

/**
 * Created by 86799 on 2016/12/3.
 */
public class PeopleItem {
    protected String time;
    protected String name;
    protected Bitmap image;
    PeopleItem(String time, String name, Bitmap image){
        this.time = time;
        this.name = name;
        this.image = image;
    }

    public String getName(){
        return name;
    }

    public String getTime(){
        return time;
    }

    public Bitmap getImage(){
        return image;
    }

}
