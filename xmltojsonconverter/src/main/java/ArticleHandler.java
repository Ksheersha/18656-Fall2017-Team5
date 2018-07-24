

import org.json.simple.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ArticleHandler extends DefaultHandler {

    boolean bauthor = false;
    boolean btitle = false;
    boolean bpages = false;
    boolean byear = false;
    boolean bvolume = false;
    boolean bjournal = false;
    boolean bnumber = false;
    boolean burl = false;
    boolean bee = false;
    JSONObject xmlJSONObj = new JSONObject();
    int count = 0;

    @Override
    public void startElement(String uri,
                             String localName, String qName, Attributes attributes)
            throws SAXException {

        if (qName.equalsIgnoreCase("article")) {
            String mdate = attributes.getValue("mdate");
            //System.out.println("mdate : " + mdate);
            xmlJSONObj.put("mdate", mdate);
            String key = attributes.getValue("key");
            // System.out.println("key : " + key);
            xmlJSONObj.put("key", key);
        } else if (qName.equalsIgnoreCase("author")) {
            bauthor = true;
        } else if (qName.equalsIgnoreCase("title")) {
            btitle = true;
        } else if (qName.equalsIgnoreCase("year")) {

            byear = true;
        } else if (qName.equalsIgnoreCase("pages")) {
            bpages = true;
        } else if (qName.equalsIgnoreCase("volume")) {
            bvolume = true;
        } else if (qName.equalsIgnoreCase("journal")) {
            bjournal = true;
        } else if (qName.equalsIgnoreCase("number")) {
            bnumber = true;
        } else if (qName.equalsIgnoreCase("url")) {
            burl = true;
        } else if (qName.equalsIgnoreCase("ee")) {
            bee = true;
        }

    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("article")) {

            if (xmlJSONObj.containsKey("year") && Integer.parseInt(xmlJSONObj.get("year").toString()) > 2003){
                try (FileWriter file = new FileWriter("output.txt", true)) {
                    count++;
                    BufferedWriter bufferWritter = new BufferedWriter(file);
                    bufferWritter.write(xmlJSONObj.toJSONString() + "\n");
                    bufferWritter.close();
                    System.out.println(count);
                } catch (Exception e){}
            }
            xmlJSONObj = null;
            xmlJSONObj = new JSONObject();
        }
    }



    @Override
    public void characters(char ch[],
                           int start, int length) throws SAXException {

        if (btitle) {
            String e = new String(ch, start, length);
            //System.out.println("btitle: " + e);
            xmlJSONObj.put("title", e);
            btitle = false;
        } else if (bauthor) {
            String e = new String(ch, start, length);
            //System.out.println("bauthor: " + e);
            xmlJSONObj.put("author", e);
            bauthor = false;
        } else if (bpages) {
            String e = new String(ch, start, length);
            //System.out.println("bpages: " + e);
            xmlJSONObj.put("pages", e);
            bpages = false;
        } else if (byear) {
            String e = new String(ch, start, length);
            //System.out.println("byear: " + e);
            xmlJSONObj.put("year", e);
            byear = false;
            if (Integer.parseInt(e) >= 2003){
                return;
            }
        } else if (bvolume) {
            String e = new String(ch, start, length);
            //System.out.println("bvolume: " + e);
            xmlJSONObj.put("volume", e);
            bvolume = false;
        } else if (bjournal) {
            String e = new String(ch, start, length);
            // System.out.println("bjournal: " + e);
            xmlJSONObj.put("journal", e);
            bjournal = false;
        } else if (bnumber) {
            String e = new String(ch, start, length);
            //System.out.println("bnumber: " + e);
            xmlJSONObj.put("number", e);
            bnumber = false;
        } else if (burl) {
            String e = new String(ch, start, length);
            //System.out.println("burl: " + e);
            xmlJSONObj.put("url", e);
            burl = false;
        } else if (bee) {
            String e = new String(ch, start, length);
            //System.out.println("bee: " + e);
            xmlJSONObj.put("ee", e);
            bee = false;
        }
    }
}
