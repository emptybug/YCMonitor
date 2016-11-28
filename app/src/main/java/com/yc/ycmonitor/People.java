package com.yc.ycmonitor;

/**
 * Created by 86799 on 2016/9/20.
 */
public class People { //People类
    String name;
    int imageId;
    String time;

    public People(String name, int imageId, String time) {
        this.name = name;
        this.imageId = imageId;
        this.time = time;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getImageId(){
        return imageId;
    }

    public void setImageId(int imageId){
        this.imageId = imageId;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String Time){
        this.time = time;
    }
}

