package com.yc.ycmonitor.net;


import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Rabbee on 2016/11/6.
 *
 * 实现步骤：
 * 1、获取来自SocketThread的Socket引用
 * 2、获取Socket引用中的OutputStream
 * 3、发送验证字符‘A‘
 * 4、发送请求字符串
 * 5、关闭输出流
 */
public class ArmSenderThread extends Thread {

    private Socket socket;
    private OutputStream os;

    private final String IDENTIFY_STRING;
    private final String REQUEST_STRING;


    public ArmSenderThread(Socket socket, String identify, String request)
    {
        this.socket = socket;
        this.IDENTIFY_STRING = identify;
        this.REQUEST_STRING = request;
    }

    @Override
    public void run() {
        try {
            os = socket.getOutputStream();
            //  首次验证
            byte[] buffer = IDENTIFY_STRING.getBytes();
            os.write(buffer);
            os.flush();
            //  请求方式
            if (REQUEST_STRING != null)
            {
                buffer = REQUEST_STRING.getBytes();
                os.write(buffer);
                os.flush();
                //os.close();
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            if (os != null)
            {
                //os = null;
            }
        }
    }
}
