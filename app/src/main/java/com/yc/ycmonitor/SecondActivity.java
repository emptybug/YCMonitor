package com.yc.ycmonitor;

import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    private ArrayList<People> peopleList = new ArrayList<People>();


    private Button change; //标题按钮

    private PeopleAdapter adapter;
    private PullToRefreshListView listView;

    private ArrayList<Integer> IDList; //人物编号集合
    private ArrayList<Integer> imageList; //人物图片集合
    private ArrayList<String> timeList; //人物出现时间的集合
    private ArrayList<String> nameList; //人物姓名

    /*private class GetDataTask extends AsyncTask<String, Void, String[]>{

        @Override
        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        protected String doInBackground(String...strings){
            return "";
        }


        @Override
        protected void onPostExecute(String[] result) {
            // Call onRefreshComplete when the list has been refreshed.
            listView.onRefreshComplete();
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values){

        }
    }*/

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
                int ID = IDList.get(position);
                thirdIntent = new Intent(SecondActivity.this, ThirdActivity.class);
                thirdIntent.putExtra("ID", ID);
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
        imageList = new ArrayList<>();
        IDList = new ArrayList<>();
        timeList = new ArrayList<>();
        nameList = new ArrayList<>();
        searchDB();
        for(int i = 0, size = IDList.size(); i < size; i++){
            addListItem(nameList.get(i), R.drawable.new_feature_4, timeList.get(i));
        }
    }

    private void initListView(){
        adapter = new PeopleAdapter(SecondActivity.this,
                R.layout.people_item, peopleList);
        listView = (PullToRefreshListView)findViewById(R.id.list_view);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                ArrayList<Integer> imageList = new ArrayList<Integer>();
                ArrayList<String> timeList = new ArrayList<String>();
                ArrayList<String> nameList = new ArrayList<String>();
                ArrayList<Integer> IDList = new ArrayList<Integer>();
                ArrayList<String> professionalList = new ArrayList<String>();
                ArrayList<String> identityList = new ArrayList<String>();

                imageList.add(R.drawable.new_feature_1);
                imageList.add(R.drawable.new_feature_10);
                imageList.add(R.drawable.new_feature_4);
                imageList.add(R.drawable.new_feature_11);

                nameList.add("Mike");
                nameList.add("Tony");
                nameList.add("Lisa");
                nameList.add("Tony");

                timeList.add("2016-10-1 10:30");
                timeList.add("2016-10-1 11:30");
                timeList.add("2016-10-1 12:30");
                timeList.add("2016-10-1 21:20");

                IDList.add(1);
                IDList.add(2);
                IDList.add(3);
                IDList.add(2);

                identityList.add("110102197501101519");
                identityList.add("44010219750110151X");
                identityList.add("330102197511193680");
                identityList.add("44010219750110151X");

                professionalList.add("教授");
                professionalList.add("健身教练");
                professionalList.add("厨师");
                professionalList.add("健身教练");

                insertDB(timeList, nameList, IDList, identityList, professionalList); //插入数据项

                searchDB(); //遍历数据库，将数据添加到ListView
                adapter.clear();
                for(int size = IDList.size(), i = 0; i < size; i++){
                    addListItem(nameList.get(i), R.drawable.new_feature_1, timeList.get(i));
                }
                //new GetDataTask().execute();
            }
        });

    }


    public void addListItem(String name, int imageId, String time){ //添加列表项的方法
        People a = new People(name, imageId, time);
        peopleList.add(a);
    }



    private void insertDB(ArrayList<String> time, ArrayList<String> name, ArrayList<Integer> ID,
                          ArrayList<String> identity, ArrayList<String> professional){
        MySqlHelper dbHelper = new MySqlHelper(SecondActivity.this,"people_db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Integer> IDList = ID; //用来插入people_time表

        ContentValues cv1 = new ContentValues();
        for(int size = ID.size(), i = size-1; i > 0; i--){
            //将传来的数据插入到表中
            cv1.put("name", name.get(i));
            cv1.put("ID", ID.get(i));
            cv1.put("identity", identity.get(i));
            cv1.put("professional", professional.get(i));
            //调用insert方法，将数据插入数据库
            db.insert("people_info", null, cv1);
        }

        //插入到表people_time当中
        ContentValues cv2 = new ContentValues();
        for(int size = IDList.size(), i = size-1; i > 0; i--){
            //将传来的数据插入到表中
            cv2.put("time", time.get(i));
            cv2.put("ID", IDList.get(i));
            //调用insert方法，将数据插入数据库
            db.insert("people_time", null, cv2);
        }
        //关闭数据库
        db.close();
    }

    //数据库遍历操作
    private void searchDB(){
        MySqlHelper dbHelper = new MySqlHelper(SecondActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select name,people_info.ID,time from people_info,people_time " +
                "where people_info.ID=people_time.ID order by time DESC", null);
        while(cursor.moveToNext()){
            int ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            IDList.add(ID);
            nameList.add(name);
            timeList.add(time);
            System.out.println("query------->" + "姓名："+name+" "+"时间："+time+" "+"号码："+ID);
        }
        //关闭数据库
        db.close();
    }

}
