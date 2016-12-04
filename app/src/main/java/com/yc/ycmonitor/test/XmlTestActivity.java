package com.yc.ycmonitor.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.ycmonitor.People;
import com.yc.ycmonitor.R;
import com.yc.ycmonitor.parser.HistoryParser;
import com.yc.ycmonitor.parser.PeopleParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

/**
 * Created by Rabbee on 2016/12/2.
 */
public class XmlTestActivity extends Activity {

    private String TAG = "TextActivity";

    private String userinfoXml;

    private String historyXml;

    private Button reflashButton;

    private TextView idTextView, nameTextView, timeTextView, identityTextView;

    private ImageView picImageView;

    String appParDir = Environment.getExternalStorageDirectory().getPath();
    String appDir = appParDir + File.separator + "YCMonitor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initFolder();
        initParams();
    }

    protected void initFolder() {

        if(new File("/mnt/sdcard").exists()) {
            Log.d("patg", "sd卡目录存在");
        }
        else {

        }
        File fileRecv = new File(appDir);
        Log.d("path", "app目录：" + fileRecv.getPath());
        if(!fileRecv.exists())
        {
            Log.d("path", "app目录：" + fileRecv.getPath());
            if(fileRecv.mkdirs())
            {
                Log.d("path", "主文件夹创建成功");
            }
        }
        else {
            Log.d("path", "主目录已存在");
        }
    }

    protected void initParams()
    {
        idTextView = (TextView) findViewById(R.id.id_tv_userinfo_test);
        nameTextView = (TextView) findViewById(R.id.name_tv_userinfo_test);
        timeTextView = (TextView) findViewById(R.id.time_tv_userinfo_test);
        identityTextView = (TextView) findViewById(R.id.identity_tv_userinfo_test);
        picImageView = (ImageView) findViewById(R.id.test_image);

        try {
            File userinfo = new File(appDir + File.separator + "userinfos.xml");
            FileInputStream fis1 = new FileInputStream(userinfo);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis1, "utf8"));
            StringBuilder sb1 = new StringBuilder();
            char[] buffers = new char[1024];
            int len;
            while ((len = (br1.read(buffers, 0, 1024))) > 0)
            {
                for(int i = 0; i < len; ++i)
                {
                    sb1.append((char)buffers[i]);
                }
                //sb1.append(new String(buffers));
            }
            fis1.close();
            userinfoXml = sb1.toString();
            Log.e(TAG, "initParams: userinfoXml " + userinfoXml.length() + " : " + userinfoXml.substring(0, 21));

            File history = new File(appDir + File.separator + "data.xml");
            FileInputStream fis2 = new FileInputStream(history);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2, "utf8"));
            StringBuilder sb2 = new StringBuilder();
            while ((len = (br2.read(buffers, 0, 1024))) > 0)
            {
                for(int i = 0; i < len; ++i)
                {
                    sb2.append((char)buffers[i]);
                }
                //sb2.append(new String(buffers));
            }
            fis2.close();
            historyXml = sb2.toString();
            Log.e(TAG, "initParams: historyXml " + historyXml.length() + " : " + historyXml.substring(0, 21));
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        reflashButton = (Button) findViewById(R.id.reflash_button_test);
        reflashButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            reflashData();
                    }
                }
        );
    }

    protected void reflashData()
    {
        try {
            InputStream is_userinfo = new ByteArrayInputStream(userinfoXml.getBytes("UTF-8"));
            InputStream is_history = new ByteArrayInputStream(historyXml.getBytes("UTF-8"));
            PeopleParser pp = new PeopleParser(is_userinfo);
            HistoryParser hp = new HistoryParser(is_history);
            List<People> peopleList = pp.parse();
            List<HistoryParser.HistoryItem> historyItemList = hp.parse();
            if (peopleList.size() > 0)
            {
                People people = peopleList.get(0);
                idTextView.setText("id： " + people.getId());
                nameTextView.setText("姓名： " + people.getName());
                identityTextView.setText("身份： " + people.getIdentity());
                Bitmap bitmap = BitmapFactory.decodeByteArray(people.getImage(), 0, people.getImage().length);
                picImageView.setImageBitmap(bitmap);
            }
            if (historyItemList.size() > 0 )
            {
                HistoryParser.HistoryItem item = historyItemList.get(0);
                Date d = item.getDate();
                timeTextView.setText("出现时间： " + d.getYear() + "年" + d.getMonth() + "月" + d.getDate() + "日" + d.getHours() + "时" + d.getMinutes() + "分" + d.getSeconds() + "秒");
                //timeTextView.setText("出现时间： " + d.toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
