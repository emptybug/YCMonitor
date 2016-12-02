package com.yc.ycmonitor.net;

import java.io.*;
import java.net.Socket;

/**
 * Created by Rabbee on 2016/11/6.
 *
 * 实现步骤：
 * 1、获取来自SocketThread的Socket引用
 * 2、获取Socket引用中的InputStream
 * 3、持续读入字符串
 * 4、将字符串传入回调函数CallBack
 */
public class ArmReceiverThread extends Thread {

    private Socket socket;
    private InputStream is;
    private StringBuilder sb;
    private RequestCallBack cb;
    private DataInputStream dis;

    private static final int MAX_SIZE_BUFFER = 24;

    public ArmReceiverThread(Socket socket, RequestCallBack requestCallBack)
    {
        this.socket = socket;
        this.cb = requestCallBack;
        sb = new StringBuilder();
    }

    @Override
    public void run() {
        try {
            is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf8"));
            int len;
            char[] buffer = new char[MAX_SIZE_BUFFER];
            while ((len = br.read(buffer, 0, MAX_SIZE_BUFFER)) > 0)
            {
                if (1 == len && buffer[0] == '\0') break;
                //System.out.println(len + ": ");
                for (int i = 0; i < len; ++i)
                {
                    //System.out.print((char)buffer[i]);
                    sb.append((char)buffer[i]);
                }
            }

            cb.solve(sb.toString());
            //is.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
