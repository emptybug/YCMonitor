package com.yc.ycmonitor.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Rabbee on 2016/12/2.
 */
public abstract class XmlParser {

    protected DocumentBuilderFactory mBuilderFactory;
    protected DocumentBuilder mBuilder;
    protected Document mDoc;
    protected Element mRootElement;

    public XmlParser(InputStream is)
    {
        try {
            mBuilderFactory = DocumentBuilderFactory.newInstance();
            mBuilder = mBuilderFactory.newDocumentBuilder();
            mDoc = mBuilder.parse(is);
            mRootElement = mDoc.getDocumentElement();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
