package com.yc.ycmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * Created by 86799 on 2016/9/20.
 */
public class SecondActivity extends Activity { //ListView的页面

    private ArrayList<People> peopleList = new ArrayList<People>();


    private Button change; //标题按钮

    private Intent thirdIntent; //点击进入个人详细列表
    private PullToRefreshListView listView;

    private ArrayList<String> numberList; //人物编号集合
    private ArrayList<Integer> imageList; //人物图片集合
    private ArrayList<String> timeList; //人物出现时间的集合
    private ArrayList<String> nameList; //人物姓名

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_second);

        initListView();
        initTitleButton();
        initPeople();
        PeopleAdapter adapter = new PeopleAdapter(SecondActivity.this,
                R.layout.people_item, peopleList);
        listView = (PullToRefreshListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> newNumberList = new ArrayList<String>();
                ArrayList<Integer> newImageList = new ArrayList<Integer>();
                ArrayList<String> newTimeList = new ArrayList<String>();
                ArrayList<String> newNameList = new ArrayList<String>();
                String number = numberList.get(position);
                for(int i = 0, size = nameList.size(); i < size; i++){ //把检索出来的数据放到新的集合中
                    if(numberList.get(i) == number){
                        newNameList.add(nameList.get(i));
                        newTimeList.add(timeList.get(i));
                        newImageList.add(imageList.get(i));
                        newNumberList.add(numberList.get(i));
                    }
                }

                thirdIntent = new Intent(SecondActivity.this, ThirdActivity.class);
                thirdIntent.putStringArrayListExtra("name", newNameList); //将新的集合传入Intent
                thirdIntent.putStringArrayListExtra("time", newTimeList);
                thirdIntent.putIntegerArrayListExtra("image", newImageList);
                thirdIntent.putStringArrayListExtra("number", newNumberList);
                startActivity(thirdIntent);
                finish();
            }
        });
    }

    private void initTitleButton() { //初始化按钮
        change = (Button)findViewById(R.id.button_change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initPeople(){ //初始化ListView
        numberList = getIntent().getStringArrayListExtra("number");
        imageList = getIntent().getIntegerArrayListExtra("image");
        nameList = getIntent().getStringArrayListExtra("name");
        timeList = getIntent().getStringArrayListExtra("time");
        for(int i = 0, size = imageList.size(); i < size; i++){
            addListItem(nameList.get(i), imageList.get(i), timeList.get(i));
        }
    }

    private void initListView(){
        /*listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(SecondActivity.this, "下拉刷新", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void addListItem(String name, int imageId, String time){ //添加列表项的方法
        People a = new People(name, imageId, time);
        peopleList.add(a);
    }

}
