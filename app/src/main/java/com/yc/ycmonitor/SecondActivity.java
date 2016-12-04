package com.yc.ycmonitor;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * Created by 86799 on 2016/9/20.
 */
public class SecondActivity extends Activity { //ListView的页面

    private ArrayList<PeopleItem> peopleList = new ArrayList<PeopleItem>();


    private Button change; //标题按钮

    private PeopleAdapter adapter;
    private PullToRefreshListView listView;

    private ArrayList<Integer> IDList; //人物编号集合
    private ArrayList<byte[]> imageList; //人物图片集合
    private ArrayList<String> timeList; //人物出现时间的集合
    private ArrayList<String> nameList; //人物姓名

    private class GetDataTask extends AsyncTask<String, Void, String> {

        @Override
        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        protected String doInBackground(String...strings){
            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            // Call onRefreshComplete when the list has been refreshed.
            listView.onRefreshComplete();
            super.onPostExecute(result);
        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_second);

        initListView();
        initTitleButton();
        initPeople();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent thirdIntent;
                int ID = IDList.get(position-1);
                thirdIntent = new Intent(SecondActivity.this, ThirdActivity.class);
                thirdIntent.putExtra("ID", ID);
                startActivity(thirdIntent);
                finish();
            }
        });
    }

    //初始化按钮
    private void initTitleButton() {
        change = (Button)findViewById(R.id.button_change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //初始化人物ID、名称、时间集合
    private void initPeople(){
        imageList = new ArrayList<>();
        IDList = new ArrayList<>();
        timeList = new ArrayList<>();
        nameList = new ArrayList<>();
        searchDB();
        for(int i = 0, size = IDList.size(); i < size; i++){
            addListItem(nameList.get(i), imageList.get(i), timeList.get(i));
        }
    }

    //初始化ListView
    private void initListView(){
        adapter = new PeopleAdapter(SecondActivity.this,
                R.layout.people_item, peopleList);
        listView = (PullToRefreshListView)findViewById(R.id.list_view);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                imageList.clear();
                IDList.clear();
                timeList.clear();
                nameList.clear();
                searchDB(); //遍历数据库，将数据添加到ListView
                adapter.clear();
                for(int size = IDList.size(), i = 0; i < size; i++){
                    addListItem(nameList.get(i), imageList.get(i), timeList.get(i));
                }
                new GetDataTask().execute();
            }
        });

    }

    //添加ListView项
    public void addListItem(String name, byte[] image, String time){
        //将byte转成bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        PeopleItem a = new PeopleItem(time, name, bitmap);
        peopleList.add(a);
    }

    //数据库遍历操作
    private void searchDB(){
        MySqlHelper dbHelper = new MySqlHelper(SecondActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select name,people_info.ID,time,image from people_info,people_time " +
                "where people_info.ID=people_time.ID order by time DESC", null);
        while(cursor.moveToNext()){
            byte[] image = cursor.getBlob((cursor.getColumnIndex("image")));
            int ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            imageList.add(image);
            IDList.add(ID);
            nameList.add(name);
            timeList.add(time);
        }
        //关闭数据库
        db.close();
    }

}
