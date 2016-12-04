package com.yc.ycmonitor.test;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.yc.ycmonitor.MySqlHelper;
import com.yc.ycmonitor.People;
import com.yc.ycmonitor.net.ArmSocketThread;
import com.yc.ycmonitor.net.RequestCallBack;
import com.yc.ycmonitor.parser.HistoryParser;
import com.yc.ycmonitor.parser.PeopleParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rabbee on 2016/12/3.
 */
public class NetDemoActivity extends Activity {

    private String ip = "192.168.1.160";
    private int port = 8080;



    /**
     * 请求的历史纪录数据完全读取完毕后的回调函数
     */
    protected RequestCallBack historysCallBack = new RequestCallBack() {
        @Override
        public void solve(String res) {
            try {
                HistoryParser hp = new HistoryParser(new ByteArrayInputStream(res.getBytes("UTF-8")));
                List<HistoryParser.HistoryItem> historyItems = hp.parse();

                //将信息存到ArrayList里，然后插入数据库
                ArrayList<String> time = new ArrayList<>();
                ArrayList<Integer> ID = new ArrayList<>();
                for(int size = historyItems.size(), i = 0; i < size; i++){
                    time.add(historyItems.get(i).getDate().toString());
                    ID.add(historyItems.get(i).getId());
                }
                insertDB(time, ID);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };

    private void insertDB(ArrayList<String> time, ArrayList<Integer> ID){
        MySqlHelper dbHelper = new MySqlHelper(NetDemoActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //插入到表people_time当中
        ContentValues cv = new ContentValues();
        for(int size = ID.size(), i = size-1; i >= 0; i--){
            //将传来的数据插入到表中
            cv.put("time", time.get(i));
            cv.put("ID", ID.get(i));
            //调用insert方法，将数据插入数据库
            db.insert("people_time", null, cv);
        }
        //关闭数据库
        db.close();
    }


    /**
     * 请求的人物信息数据完全读取完毕后的回调函数
     */
    protected RequestCallBack userinfosCallBack = new RequestCallBack() {
        @Override
        public void solve(String res) {
            try {
                PeopleParser pp = new PeopleParser(new ByteArrayInputStream(res.getBytes("UTF-8")));
                List<People> peoples = pp.parse();

                //将信息存到ArrayList里，然后插入数据库
                ArrayList<Integer> ID = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> identity = new ArrayList<>();
                ArrayList<String> professional = new ArrayList<>();
                ArrayList<byte[]> image = new ArrayList<>();
                for(int size = peoples.size(), i = 0; i < size; i++){
                    name.add(peoples.get(i).getName());
                    ID.add(peoples.get(i).getId());
                    identity.add(peoples.get(i).getIdentity());
                    professional.add(peoples.get(i).getProfessional());
                    image.add(peoples.get(i).getImage());
                }
                insertDB(name, ID, identity, professional, image);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };

    private void insertDB(ArrayList<String> name, ArrayList<Integer> ID, ArrayList<String> identity,
                          ArrayList<String> professional, ArrayList<byte[]> image){
        MySqlHelper dbHelper = new MySqlHelper(NetDemoActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //生成ContentValues对象
        ContentValues cv = new ContentValues();
        //往ContentValues对象存放数据，键-值对模式，插入到表people_info当中
        for(int size = ID.size(), i = size-1; i >= 0; i--){
            //将传来的数据插入到表中
            cv.put("name", name.get(i));
            cv.put("ID", ID.get(i));
            cv.put("identity", identity.get(i));
            cv.put("professional", professional.get(i));
            cv.put("image", image.get(i));
            //调用insert方法，将数据插入数据库
            db.insert("people_info", null, cv);
        }
        //关闭数据库
        db.close();
    }

    /**
     * 请求一段时间区间的历史纪录
     * 时间数据类型为String
     * 格式：年 + 月 + 日 + 时 + 分 + 秒 + 00
     * 例如：2016110608231200
     * @param start 开始时间
     * @param end   结束时间
     */
    public void requestHistory(String start, String end)
    {
        ArmSocketThread ast = new ArmSocketThread(historysCallBack, ip, port);
        ast.setIdentify("A12345678\0");
        ast.setRequest(start + end);
        ast.start();
    }

    /**
     * 请求所有人物信息
     */
    public void requestUsersInfos()
    {
        ArmSocketThread ast = new ArmSocketThread(userinfosCallBack, ip, port);
        ast.setIdentify("B12345678\0");
        ast.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
