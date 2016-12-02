package com.yc.ycmonitor;

import android.graphics.Bitmap;

/**
 * Created by 86799 on 2016/9/20.
 */
public class People { //People类

    protected int id;
    protected Bitmap image;
    String name;
    int imageId;
    String time;
    protected String identity;

    public People()
    {

    }

    public People(String name, int imageId, String time) {
        this.name = name;
        this.imageId = imageId;
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
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

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }
}

