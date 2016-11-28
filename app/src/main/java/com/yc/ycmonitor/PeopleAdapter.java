package com.yc.ycmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 86799 on 2016/9/20.
 */
public class PeopleAdapter extends ArrayAdapter<People> { //ListView的

    private int resoutceId;

    public PeopleAdapter(Context context, int textViewResourceId, List<People> object) {
        super(context, textViewResourceId, object);
        resoutceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        People people = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resoutceId, null);
        ImageView peoplePicture = (ImageView)view.findViewById(R.id.people_picture); //人物图片
        TextView peopleName = (TextView)view.findViewById(R.id.people_name); //人物名称
        TextView time = (TextView)view.findViewById(R.id.time); //出现时间
        peoplePicture.setImageResource(people.getImageId());
        peopleName.setText(people.getName());
        time.setText(people.getTime());
        return view;
    }
}
