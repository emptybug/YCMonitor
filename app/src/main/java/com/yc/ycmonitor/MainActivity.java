package com.yc.ycmonitor;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;

/**
 * 左右滑动demo
 * @author xzw
 *没解决的问题：
 * 1.用现成的库来实现浏览图片
 * 2.得到数据之后写入数据库
 * 3.怎么获取XML文件里的数组数据
 * 4.还有identity、professional和imageData数据没有写入数据库中（未修改数据库）
 */
public class MainActivity extends Activity implements OnGestureListener { //打开软件的页面

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
    private Intent secondIntent; //大视图切换成列表视图
    private Intent thirdIntent; //点击之后进入个人详细列表

    private ArrayList<Integer> imageList; //人物图片集合
    private ArrayList<String> timeList; //人物出现时间集合
    private ArrayList<String> nameList; //人物姓名集合
    private ArrayList<Integer> IDList; //人物的ID，即每个人的标识
    private ArrayList<String> professionalList; //人物的职业
    private ArrayList<String> identityList; //人物的身份证号

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        imageList = new ArrayList<Integer>(); //将图片的ID（URL）放入集合中
        nameList = new ArrayList<String>();
        timeList = new ArrayList<String>();
        IDList = new ArrayList<Integer>();
        professionalList = new ArrayList<String>();
        identityList = new ArrayList<String>();

        initViewFlipper(); //初始化图片移动效果
        addPeopleImage(); //往viewFlipper添加View
        addNameText(); //添加姓名name
        addTimeText(); //添加时间text
        initTitleButton(); //初始化退出和切换视图按钮

    }

    private void initTitleButton() { //初始化按钮
        change = (Button)findViewById(R.id.button_change);
        update = (Button)findViewById(R.id.button_update);
        secondIntent = new Intent(MainActivity.this, SecondActivity.class);
        secondIntent.putStringArrayListExtra("name", nameList);
        secondIntent.putStringArrayListExtra("time", timeList);
        secondIntent.putIntegerArrayListExtra("image", imageList);
        secondIntent.putIntegerArrayListExtra("ID", IDList);
        secondIntent.putStringArrayListExtra("identity", identityList);
        secondIntent.putStringArrayListExtra("professional", professionalList);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(secondIntent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getInfo();调用后台getInfo方法,该方法返回一个集合（或者数组），里面装有相关信息
                //将获取到的记录的图片添加进ViewFlipper，时间则添加到TimeText
                imageList.add(R.drawable.new_feature_1);
                imageList.add(R.drawable.new_feature_10);
                imageList.add(R.drawable.new_feature_4);
                imageList.add(R.drawable.new_feature_11);
                addPeopleImage();
                viewFlipper.refreshDrawableState();

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
                IDList.add(4);

                identityList.add("110102197501101519");
                identityList.add("44010219750110151X");
                identityList.add("330102197511193680");
                identityList.add("44010219750110151X");

                professionalList.add("教授");
                professionalList.add("健身教练");
                professionalList.add("厨师");
                professionalList.add("健身教练");

                insertDB(timeList, nameList, IDList, identityList, professionalList); //插入数据项
                nameList.clear();
                timeList.clear();
                IDList.clear();
                identityList.clear();
                professionalList.clear();
                imageList.clear();

                searchDB(); //遍历数据库
            }
        });
    }

    private void initViewFlipper() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        detector = new GestureDetector(this);

        View.OnClickListener listener = new View.OnClickListener() { //点击事件
            @Override
            public void onClick(View v) { //在点击之后检索出所有该人物信息，组织好后放到Intent中传过去
                ArrayList<Integer> newIDList = new ArrayList<Integer>();
                ArrayList<Integer> newImageList = new ArrayList<Integer>();
                ArrayList<String> newTimeList = new ArrayList<String>();
                ArrayList<String> newNameList = new ArrayList<String>();
                if(IDList.size() > 0){
                    int position = viewFlipper.getDisplayedChild(); //找到点击的图片的相关信息的位置
                    int id = IDList.get(position);
                    for(int i = 0, size = IDList.size(); i < size; i++){ //把检索出来的数据放到新的集合中
                        if(IDList.get(i) == id){
                            newNameList.add(nameList.get(i));
                            newTimeList.add(timeList.get(i));
                            newIDList.add(IDList.get(i));
                        }
                    }

                    thirdIntent = new Intent(MainActivity.this, ThirdActivity.class);
                    thirdIntent.putStringArrayListExtra("name", newNameList); //将新的集合传入Intent
                    thirdIntent.putStringArrayListExtra("time", newTimeList);
                    thirdIntent.putIntegerArrayListExtra("image", newImageList);
                    thirdIntent.putIntegerArrayListExtra("ID", newIDList);
                    startActivity(thirdIntent);
                }
            }
        };

        //动画效果
        leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
        leftOutAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
        rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
        rightOutAnimation = AnimationUtils.loadAnimation(this, R.anim.right_out);
        viewFlipper.setOnClickListener(listener);
    }

    private void addPeopleImage() { //往viewFlipper添加View
        for(int i = 0; i < imageList.size(); i++) { //将集合里的数据放到viewFlipper里
            viewFlipper.addView(getImageView(imageList.get(i)));
        }
    }

    private void addTimeText() {
        time = (TextView) findViewById(R.id.time_main_activity);
    }

    private void addNameText() {
        name = (TextView)findViewById(R.id.name);
    }

    private ImageView getImageView(int id) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(id);
        return imageView;
    }

    //数据库插入操作
    private void insertDB(ArrayList<String> time, ArrayList<String> name, ArrayList<Integer> ID,
                          ArrayList<String> identity, ArrayList<String> professional){
        MySqlHelper dbHelper = new MySqlHelper(MainActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Integer> IDList = ID; //用来插入people_time表

        //判断数据库中是否已经存在了这些数据
        Cursor cursor = db.rawQuery("select ID from people_info", null);
        while(cursor.moveToNext()){
            int IDData = cursor.getInt(cursor.getColumnIndex("ID"));
            for(int size = ID.size(), i = size-1; i > 0; i--){
                if(ID.get(i) == IDData){ //数据库中已经有了这些数据
                    ID.remove(i);
                    name.remove(i);
                    identity.remove(i);
                    professional.remove(i);
                }
            }
        }

        //生成ContentValues对象
        ContentValues cv1 = new ContentValues();
        //往ContentValues对象存放数据，键-值对模式，插入到表people_info当中
        for(int i = 0, size = ID.size(); i < size; i++){
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
        for(int i = 0, size = ID.size(); i < size; i++){
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
        MySqlHelper dbHelper = new MySqlHelper(MainActivity.this,"people_db",null,1);
        //得到一个可写的数据库
        SQLiteDatabase db =dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select name,people_info.ID,time from people_info,people_time where people_info.ID=people_time.ID", null);
        while(cursor.moveToNext()){
            int ID = cursor.getInt(cursor.getColumnIndex("ID"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            IDList.add(ID);
            nameList.add(name);
            timeList.add(time);
            //System.out.println("query------->" + "姓名："+name+" "+"时间："+time+" "+"号码："+ID);
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

}


