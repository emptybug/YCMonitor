package com.yc.ycmonitor.parser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rabbee on 2016/12/2.
 */
public class HistoryParser extends XmlParser {

    public HistoryParser(InputStream is) throws Exception {
        super(is);
    }

    public List<HistoryItem> parse()
    {
        List<HistoryItem> historys = new ArrayList<>();
        NodeList items = mRootElement.getElementsByTagName("HISTORY");
        for (int i = 0; i < items.getLength(); ++i)
        {
            HistoryItem history = new HistoryItem();
            Node item = items.item(i);
            NodeList properties = item.getChildNodes();
            for (int j = 0; j < properties.getLength(); ++j)
            {
                Node property = properties.item(j);
                String nodeName = property.getNodeName();
                switch (nodeName)
                {
                    case "TIME":
                        String time = property.getFirstChild().getNodeValue();
                        int year = Integer.parseInt(time.substring(0, 4));
                        int month = Integer.parseInt(time.substring(4, 6));
                        int day = Integer.parseInt(time.substring(6, 8));
                        int hour = Integer.parseInt(time.substring(8, 10));
                        int minute = Integer.parseInt(time.substring(10, 12));
                        int second = Integer.parseInt(time.substring(12, 14));
                        Date date = new Date(year, month, day, hour, minute, second);
                        history.setDate(date);
                        break;
                    case "ID":
                        history.setId(Integer.parseInt(property.getFirstChild().getNodeValue()));
                        break;
                }
            }
            historys.add(history);
        }
        return historys;
    }

    public class HistoryItem
    {
        protected Date date;
        protected int id;

        public void setId(int id) {
            this.id = id;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public int getId() {
            return id;
        }

        public Date getDate() {
            return date;
        }
    }
}
