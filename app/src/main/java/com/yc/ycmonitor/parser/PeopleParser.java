package com.yc.ycmonitor.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.yc.ycmonitor.People;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rabbee on 2016/12/2.
 */
public class PeopleParser extends XmlParser {

    protected static final String TAG = "PeopleParser";

    public PeopleParser(InputStream is) throws Exception {
        super(is);
    }

    public List<People> parse()
    {
        List<People> peoples = new ArrayList<>();
        NodeList items = mRootElement.getElementsByTagName("USER");
        for (int i = 0; i < items.getLength(); ++i)
        {
            People people = new People();
            Node item = items.item(i);
            NodeList properties = item.getChildNodes();
            Log.d(TAG, "parse: 一个人的属性数量： " + properties.getLength());
            for (int j = 0; j < properties.getLength(); ++j)
            {
                Node property = properties.item(j);
                String nodeName = property.getNodeName();
                if (nodeName == null) {
                    Log.e(TAG, "parse: 存在一个NULL的People");
                    continue;
                }
                switch (nodeName)
                {
                    case "ID":
                        people.setId(Integer.parseInt(property.getFirstChild().getNodeValue()));
                        break;
                    case "NAME":
                        people.setName(property.getFirstChild().getNodeValue());
                        break;
                    case "IDENTITY":
                        people.setIdentity(property.getFirstChild().getNodeValue());
                        break;
                    case "IMAGEDATA":
                        String data = property.getFirstChild().getNodeValue();
                        String[] strings = data.split(" ");
                        byte[] b = new byte[data.length()];
                        for (int k = 0; k < strings.length; ++k)
                        {
                            int temp = Integer.parseInt(strings[k]);
                            b[k] = (byte) temp;
                        }
                        Bitmap bp = BitmapFactory.decodeByteArray(b, 0, b.length);
                        people.setImage(bp);
                        break;
                    default:
                        break;
                }
            }
            peoples.add(people);
        }
        return peoples;
    }
}
