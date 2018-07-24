package controllers;

import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;


/**
 * Created by bowenyang on 11/29/16.
 */
public class CategorizeByTimestampController {

    private class ValueComparator implements Comparator<String> {
        Map<String, Integer> base;

        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }

    private Driver driver;

    public CategorizeByTimestampController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result getCategorizeByTime(String startYear, String endYear) {
        Session session = driver.session();
        Controller.response().setHeader("Access-Control-Allow-Origin", "*");
        ArrayList <String> retKeywords = new ArrayList<>();
        ArrayList <String> topKeywords = new ArrayList<>();
        StatementResult result;
        TreeMap <String, String> ret = new TreeMap<>();
        Integer topCount = 20;
        result = session.run("MATCH (paper:Paper) WHERE paper.year >= '"
                + startYear + "' AND paper.year <= '"
                + endYear + "' with paper as newinput MATCH (newinput) - [r:HAS_KEYWORD] -> (keywords: Keyword) " +
                "RETURN keywords");
        while (result.hasNext()) {
            Record record = result.next();
            retKeywords.add(record.get("keywords").get("keyword").toString().replaceAll("\"", ""));
        }
        HashSet<String> keywordSet = new HashSet<String>(retKeywords);
        TreeMap<String, Integer> unsorted_map = new TreeMap<String, Integer>();
        ValueComparator bvc = new ValueComparator(unsorted_map);
        for(String s: keywordSet) {
            unsorted_map.put(s, Collections.frequency(retKeywords, s));
        }
        TreeMap <String, Integer> sorted_map = new TreeMap <String, Integer> (bvc);
        sorted_map.putAll(unsorted_map);
        Integer cnt = 0;
        // Take the top out of the map
        for(Map.Entry<String,Integer> entry : sorted_map.entrySet()) {
            if (cnt >= topCount){
                break;
            }
            String key = entry.getKey();
            topKeywords.add(key);
            cnt ++;
        }
        //System.out.println(cnt);
        //System.out.println(topKeywords.size());
        // Then iterate over the keywords to send query
        for (String s: topKeywords){
            result = session.run("MATCH (paper:Paper) - [r: HAS_KEYWORD] -> (keywords: Keyword) WHERE paper.year >= '"
                    + startYear + "' AND paper.year <= '"+ endYear + "' AND keywords.keyword = '" + s + "' RETURN DISTINCT paper");
            HashSet <String> uniqueTitles = new HashSet<>();
            while (result.hasNext()) {
                Record record = result.next();
                uniqueTitles.add(record.get("paper").get("title").toString().replaceAll("\"", ""));
            }
            ret.put(s, String.join(",", uniqueTitles));
//            System.out.println(s +","+String.join(",", uniqueTitles));
        }
        session.close();

        return Controller.ok(Json.toJson(ret));
    }
}
