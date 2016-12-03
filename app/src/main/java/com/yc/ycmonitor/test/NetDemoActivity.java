package com.yc.ycmonitor.test;

import android.app.Activity;
import android.os.Bundle;

import com.yc.ycmonitor.People;
import com.yc.ycmonitor.net.ArmSocketThread;
import com.yc.ycmonitor.net.RequestCallBack;
import com.yc.ycmonitor.parser.HistoryParser;
import com.yc.ycmonitor.parser.PeopleParser;

import java.io.ByteArrayInputStream;
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
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };

    /**
     * 请求的人物信息数据完全读取完毕后的回调函数
     */
    protected RequestCallBack userinfosCallBack = new RequestCallBack() {
        @Override
        public void solve(String res) {
            try {
                PeopleParser pp = new PeopleParser(new ByteArrayInputStream(res.getBytes("UTF-8")));
                List<People> peoples = pp.parse();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };

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
