package com.yc.ycmonitor.net;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Rabbee on 2016/11/6.
 *
 * 使用方法：
 * 1、实现CallBack接口
 * 2、新建ArmSocketThread的对象
 * 3、设置请求需要的字符串
 * 4、运行对象.start()
 *
 * 实现步骤：
 * 1、发起Socket连接
 * 2、将写入和接收分到不同线程
 */
public class ArmSocketThread extends Thread {

    private Socket socket;

    private String ipAddress;
    private int port;
    private ArmSenderThread ast;
    private ArmReceiverThread art;

    private RequestCallBack requestCallBack;
    private String request = null;
    private String identify = null;

    private final String DEFAULT_IP_ADDRESS = "192.168.1.1";
    private final int DEFAULT_PORT = 8000;

    public ArmSocketThread(RequestCallBack requestCallBack)
    {
        this.requestCallBack = requestCallBack;
        this.ipAddress = DEFAULT_IP_ADDRESS;
        this.port = DEFAULT_PORT;
    }

    public ArmSocketThread(RequestCallBack requestCallBack, String ip)
    {
        this.requestCallBack = requestCallBack;
        this.ipAddress = ip;
        this.port = DEFAULT_PORT;
    }

    public ArmSocketThread(RequestCallBack requestCallBack, int port)
    {
        this.requestCallBack = requestCallBack;
        this.ipAddress = DEFAULT_IP_ADDRESS;
        this.port = port;
    }

    public ArmSocketThread(RequestCallBack requestCallBack, String ip, int port)
    {
        this.requestCallBack = requestCallBack;
        this.ipAddress = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ipAddress, port);
            ast = new ArmSenderThread(socket, identify, request);
            ast.start();
            art = new ArmReceiverThread(socket, requestCallBack);
            art.start();
            System.out.println("1");
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    @Override
    public synchronized void start() {
        if (identify == null)
        {
            System.out.println("没有设置请求需要的字符串");
        }
        else
        {
            super.start();
        }
    }
}
