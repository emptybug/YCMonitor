package com.yc.ycmonitor;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by 86799 on 2016/9/20.
 */

//People类
public class People {

    protected int id;
    protected Bitmap image;
    protected String name;
    protected String identity;
    protected String professional;

    public People()
    {

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

    public byte[] getImage() {
        //图片的二进制形式
        byte[] image_byte;
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        image.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        image.recycle();//自由选择是否进行回收
        image_byte = output.toByteArray();//转换成功了
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image_byte;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }

    public void setProfessional(String professional){
        this.professional = professional;
    }

    public String getProfessional(){
        return professional;
    }
}

