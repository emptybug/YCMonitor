package com.yc.ycmonitor;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends Activity implements OnGestureListener{ //打开软件的页面

    private static final String TAG = "MainActivity";

    private ViewFlipper viewFlipper;
    private GestureDetector detector; //手势检测

    private Animation leftInAnimation; //图片的移动
    private Animation leftOutAnimation;
    private Animation rightInAnimation;
    private Animation rightOutAnimation;

    private Button change; //切换视图按钮
    private Button update; //更新按钮
    private TextView time; //出现的时间
    private TextView name; //出现人物的姓名

    private ArrayList<String> timeList; //人物出现时间集合
    private ArrayList<String> nameList; //人物姓名集合
    private ArrayList<Integer> IDList; //人物的ID，即每个人的标识
    private ArrayList<byte[]> imageList; //人物图片集合

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        imageList = new ArrayList<byte[]>();
        nameList = new ArrayList<String>();
        timeList = new ArrayList<String>();
        IDList = new ArrayList<Integer>();

        initViewFlipper(); //初始化图片移动效果
        initTitleButton(); //初始化退出和切换视图按钮

        init();

    }

    //初始化按钮
    private void initTitleButton() { //初始化按钮
        change = (Button)findViewById(R.id.button_change);
        update = (Button)findViewById(R.id.button_update);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secondIntent; //大视图切换成列表视图
                secondIntent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(secondIntent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 发送网络请求
                 */


                /**
                 * 模拟测试数据
                 */
                ArrayList<byte[]> imageL = new ArrayList<byte[]>();
                ArrayList<String> nameL = new ArrayList<String>();
                ArrayList<String> timeL = new ArrayList<String>();
                ArrayList<Integer> IDL = new ArrayList<Integer>();
                ArrayList<String> identityL = new ArrayList<String>();
                ArrayList<String> professionalL = new ArrayList<String>();
                byte[] a = {1, 1, 1, 1, 1};
                byte[] b = {1, 0, 1, 0, 1};
                imageL.add(a);
                imageL.add(b);
                nameL.add("A");
                nameL.add("B");
                identityL.add("441000");
                identityL.add("111000");
                professionalL.add("aaa");
                professionalL.add("bbb");
                IDL.add(1);
                IDL.add(2);
                timeL.add("2016-1-1 10:00");
                timeL.add("2000-1-1 10:00");
                insertDB(nameL, IDL, identityL, professionalL, imageL);
                insertDB(timeL, IDL);



                //遍历数据库
                searchDB();
                //清除view里所有的内容
                viewFlipper.removeAllViews();
                //将遍历的新的内容添加到view中,第一个显示的是离当前时间最近出现的人
                if(IDList.size() != 0){
                    addPeopleImage(imageList);
                    time.setText(timeList.get(0));
                    name.setText(nameList.get(0));
                }
            }
        });
    }

    //初始化图片滑动
    private void initViewFlipper() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        detector = new GestureDetector(this);

        //动画效果
        leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
        leftOutAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
        rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
        rightOutAnimation = AnimationUtils.loadAnimation(this, R.anim.right_out);

        //点击事件
        //在点击之后检索出所有该人物信息，组织好后放到Intent中传过去
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //找到点击的图片的相关信息的位置
                if(IDList.size() != 0){
                    int position = viewFlipper.getDisplayedChild();
                    int id = IDList.get(position);

                    //点击之后进入个人详细列表
                    Intent thirdIntent;
                    thirdIntent = new Intent(MainActivity.this, ThirdActivity.class);
                    thirdIntent.putExtra("ID", id);
                    startActivity(thirdIntent);
                }
            }
        };
        viewFlipper.setOnClickListener(listener);
    }

    //初始化时间TextView、姓名TextView和图片
    private void init(){
        name = (TextView)findViewById(R.id.name);
        time = (TextView) findViewById(R.id.time_main_activity);
        searchDB();
        if(IDList.size() != 0){
            name.setText(nameList.get(0));
            time.setText(timeList.get(0));
            addPeopleImage(imageList);
        }

    }

    //将图片添加到viewFlipper里
    private void addPeopleImage(ArrayList<byte[]> image) { //往viewFlipper添加View
        ImageView img;
        ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();

        //将byte转成bitmap
        for(int size = image.size(), i = 0; i < size; i++){
            Bitmap bitmap = BitmapFactory.decodeByteArray(image.get(i), 0, image.get(i).length);
            bitmapList.add(bitmap);
        }

        //将bitmap转成image
        for(int i = 0; i < imageList.size(); i++) {
            img = new ImageView(this);
            img.setImageBitmap(bitmapList.get(i));
            viewFlipper.addView(img);
        }
    }

    //数据库遍历操作，将数据放入到列表中
    private void searchDB(){
        MySqlHelper dbHelper = new MySqlHelper(MainActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();

        imageList.clear();
        IDList.clear();
        nameList.clear();
        timeList.clear();
        Cursor cursor = db.rawQuery("select name,people_info.ID,time,image from people_info,people_time " +
                "where people_info.ID=people_time.ID order by time DESC", null);
        while(cursor.moveToNext()){
            byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
            int ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            imageList.add(image);
            IDList.add(ID);
            nameList.add(name);
            timeList.add(time);
            System.out.println("query------->" + "姓名："+name+" "+"时间："+time+" "+"号码："+ID);
        }
        //关闭数据库
        db.close();
    }

    //删除数据库，用于修改重建数据库,！测试用！
    private void dropDB(){
        MySqlHelper dbHelper = new MySqlHelper(MainActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        this.deleteDatabase("people_db");
        db.close();
    }

    //查看数据库数据，！测试用！
    private void searchTable(){

        MySqlHelper dbHelper = new MySqlHelper(MainActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from people_info", null);
        while(cursor.moveToNext()){
            int ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String identity = cursor.getString(cursor.getColumnIndex("identity"));
            String professional = cursor.getString(cursor.getColumnIndex("professional"));
            System.out.println("query------->" + "姓名："+name+" "+"身份证号："+identity+" "+"号码："+ID+"职业: "+professional);
        }
        //关闭数据库
        db.close();
    }

    public boolean dispatchTouchEvent(MotionEvent ev){
        //先执行滑屏事件
        detector.onTouchEvent(ev);
        super.dispatchTouchEvent(ev);
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return this.detector.onTouchEvent(event); //touch事件交给手势处理。
    }

    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        this.viewFlipper.setClickable(true);
        return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) { //滑动实现移动图片
        //Log.i(TAG, "e1=" + e1.getX() + " e2=" + e2.getX() + " e1-e2=" + (e1.getX() - e2.getX()));
        //打印移动结果一遍观察
        int index = viewFlipper.getDisplayedChild();
        int size = IDList.size();
        this.viewFlipper.setClickable(false);

        if (e1.getX() - e2.getX() > 120 && size > 0) {
            viewFlipper.setInAnimation(leftInAnimation);
            viewFlipper.setOutAnimation(leftOutAnimation);
            viewFlipper.showNext();//向左滑动

            if(index == size-1) { //变换时间
                time.setText(timeList.get(0));
                name.setText(nameList.get(0));
            }
            else{
                time.setText(timeList.get(index+1));
                name.setText(nameList.get(index+1));
            }
            return true;

        } else if (e1.getX() - e2.getX() < -120 && size > 0) {
            viewFlipper.setInAnimation(rightInAnimation);
            viewFlipper.setOutAnimation(rightOutAnimation);
            viewFlipper.showPrevious();//向右滑动
            if(index == 0) { //变换时间
                time.setText(timeList.get(size-1));
                name.setText(nameList.get(size-1));
            }
            else{
                time.setText(timeList.get(index-1));
                name.setText(nameList.get(index-1));
            }
            return true;
        }
        return false;
    }

    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    private void insertDB(ArrayList<String> time, ArrayList<Integer> ID){
        MySqlHelper dbHelper = new MySqlHelper(MainActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //插入到表people_time当中
        ContentValues cv = new ContentValues();
        for(int size = ID.size(), i = size-1; i >= 0; i--){
            //将传来的数据插入到表中
            cv.put("time", time.get(i));
            cv.put("ID", ID.get(i));
            //调用insert方法，将数据插入数据库
            db.insert("people_time", null, cv);
        }
        //关闭数据库
        db.close();
    }

    private void insertDB(ArrayList<String> name, ArrayList<Integer> ID, ArrayList<String> identity,
                          ArrayList<String> professional, ArrayList<byte[]> image){
        MySqlHelper dbHelper = new MySqlHelper(MainActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //生成ContentValues对象
        ContentValues cv = new ContentValues();
        //往ContentValues对象存放数据，键-值对模式，插入到表people_info当中
        for(int size = ID.size(), i = size-1; i >= 0; i--){
            //将传来的数据插入到表中
            cv.put("name", name.get(i));
            cv.put("ID", ID.get(i));
            cv.put("identity", identity.get(i));
            cv.put("professional", professional.get(i));
            cv.put("image", image.get(i));
            //调用insert方法，将数据插入数据库
            db.insert("people_info", null, cv);
        }
        //关闭数据库
        db.close();
    }
}


