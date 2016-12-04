package com.yc.ycmonitor;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 86799 on 2016/9/27.
 */
public class ThirdActivity extends Activity {

    private List<PeopleItem> peopleList = new ArrayList<PeopleItem>();
    private ListView listView;
    private TextView nameText;
    private TextView infoText;

    private Button changeButton; //返回大视图

    private ArrayList<Integer> IDList; //人物图片集合
    private ArrayList<String> timeList; //人物出现时间的集合
    private ArrayList<String> nameList; //人物姓名
    private ArrayList<String> identityList; //身份证号码
    private ArrayList<String> professionalList; //职业
    private ArrayList<byte[]> imageList; //图片的二进制形式

    private ImageView thirdImage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_third);
        IDList = new ArrayList<>(); //人物ID集合
        timeList = new ArrayList<>(); //人物出现时间的集合
        nameList = new ArrayList<>(); //人物姓名
        identityList = new ArrayList<>(); //身份证号码
        professionalList = new ArrayList<>(); //职业
        imageList = new ArrayList<>(); //人物图片集合

        int id;
        id = getIntent().getIntExtra("ID", 0);
        searchDB(id); //根据传来的ID遍历数据库

        initPeople(); //初始化people的数据
        initListView(); //初始化列表项
        initView(); //初始化按钮
    }

    //初始化people的数据
    private void initPeople(){
        for(int size = IDList.size(), i = size-1; i >= 0; i--){
            addListItem(nameList.get(i),imageList.get(i), timeList.get(i));
        }
        thirdImage = (ImageView)findViewById(R.id.image_view);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageList.get(0), 0, imageList.get(0).length);
        thirdImage.setImageBitmap(bitmap);
    }

    //添加列表项的方法
    private void addListItem(String name, byte[] image, String time){
        //将byte转成bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        PeopleItem a = new PeopleItem(time, name, bitmap);
        peopleList.add(a);
    }

    //初始化列表
    private void initListView(){
        PeopleAdapter adapter = new PeopleAdapter(ThirdActivity.this,
                R.layout.people_item, peopleList);
        listView = (ListView)findViewById(R.id.one_person_list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ThirdActivity.this, "点击了ListView"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化View
    private void initView(){
        changeButton = (Button)findViewById(R.id.button_change);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nameText = (TextView)findViewById(R.id.name_third);
        infoText = (TextView)findViewById(R.id.people_info);
        nameText.setText(nameList.get(0));
        infoText.setText("职业:" + professionalList.get(0) + " 身份证" + identityList.get(0));
    }

    //数据库遍历操作
    private void searchDB(int id){
        MySqlHelper dbHelper = new MySqlHelper(ThirdActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();

        int ID;
        String name;
        String time;
        String identity;
        String professional;
        byte[] image;

        Cursor cursor = db.rawQuery("select name,people_info.ID,time,identity,professional,image from people_info,people_time " +
                "where people_info.ID=people_time.ID and people_info.ID='" + id + "'", null);
        while(cursor.moveToNext()){
            ID = cursor.getInt(cursor.getColumnIndex("ID"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            time = cursor.getString(cursor.getColumnIndex("time"));
            identity = cursor.getString(cursor.getColumnIndex("identity"));
            professional = cursor.getString(cursor.getColumnIndex("professional"));
            image = cursor.getBlob(cursor.getColumnIndex("image"));
            IDList.add(ID);
            nameList.add(name);
            timeList.add(time);
            identityList.add(identity);
            professionalList.add(professional);
            imageList.add(image);
        }
        //关闭数据库
        db.close();
    }

}
