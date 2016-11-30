package com.yc.ycmonitor;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 86799 on 2016/9/27.
 */
public class ThirdActivity extends Activity {

    private List<People> peopleList = new ArrayList<People>();
    private ListView listView;

    private Button changeButton; //返回大视图

    private ArrayList<Integer> IDList; //人物图片集合
    private ArrayList<String> timeList; //人物出现时间的集合
    private ArrayList<String> nameList; //人物姓名
    private ArrayList<String> identityList; //身份证号码
    private ArrayList<String> professionalList; //职业

    private ImageView thirdImage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_third);
        IDList = new ArrayList<>(); //人物图片集合
        timeList = new ArrayList<>(); //人物出现时间的集合
        nameList = new ArrayList<>(); //人物姓名
        identityList = new ArrayList<>(); //身份证号码
        professionalList = new ArrayList<>(); //职业

        int id;
        id = getIntent().getIntExtra("ID", 0);
        searchDB(id); //根据传来的ID遍历数据库

        initPeople(); //初始化people的数据
        initListView(); //初始化列表项
        initButton(); //初始化按钮
    }

    private void initPeople(){ //初始化people的数据
        for(int size = IDList.size(), i = size-1; i >= 0; i--){
            addListItem(nameList.get(i), R.drawable.new_feature_1, timeList.get(i));
        }
        thirdImage = (ImageView)findViewById(R.id.image_view);
        thirdImage.setImageResource(R.drawable.new_feature_1);
    }

    private void addListItem(String name, int imageId, String time){ //添加列表项的方法
        People a = new People(name, imageId, time);
        peopleList.add(a);
    }

    private void initListView(){ //初始化列表
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

    private void initButton(){ //初始化按钮
        changeButton = (Button)findViewById(R.id.button_change);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //数据库遍历操作
    private void searchDB(int id){
        MySqlHelper dbHelper = new MySqlHelper(ThirdActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select name,people_info.ID,time,identity,professional from people_info,people_time " +
                "where people_info.ID=people_time.ID and people_info.ID='" + id + "'", null);
        while(cursor.moveToNext()){
            int ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String identity = cursor.getString(cursor.getColumnIndex("identity"));
            String professional = cursor.getString(cursor.getColumnIndex("professional"));
            IDList.add(ID);
            nameList.add(name);
            timeList.add(time);
            identityList.add(identity);
            professionalList.add(professional);
            System.out.println("query------->" + "姓名："+name+" "+"时间："+time+" "+"号码："+ID);
            System.out.println("query------->" + "身份证号码：" + identity + "    职业：" + professional);
        }
        //关闭数据库
        db.close();
    }


}
