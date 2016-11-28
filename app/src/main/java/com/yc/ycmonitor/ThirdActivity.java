package com.yc.ycmonitor;

import android.app.Activity;
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

    private ArrayList<Integer> imageList; //人物图片集合
    private ArrayList<String> timeList; //人物出现时间的集合
    private ArrayList<String> nameList; //人物姓名

    private ImageView thirdImage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_third);

        initPeople(); //初始化people的数据
        initListView(); //初始化列表项
        initButton(); //初始化按钮
    }

    private void initPeople(){ //初始化people的数据
        imageList = getIntent().getIntegerArrayListExtra("image");
        nameList = getIntent().getStringArrayListExtra("name");
        timeList = getIntent().getStringArrayListExtra("time");
        for(int i = 0, size = imageList.size(); i < size; i++){
            addListItem(nameList.get(i), imageList.get(i), timeList.get(i));
        }
        thirdImage = (ImageView)findViewById(R.id.image_view);
        thirdImage.setImageResource(imageList.get(0));
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

}
