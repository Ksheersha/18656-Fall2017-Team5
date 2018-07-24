package controllers;

import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

/**
 * Created by bowenyang on 11/28/16.
 */
public class KeywordEvolutionController extends Controller {
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

    public KeywordEvolutionController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result getkeywordEvolution(String journal, String startYear, String endYear) {
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");
        Integer start = Integer.parseInt(startYear);
        Integer end = Integer.parseInt(endYear);
        ArrayList <String> retKeywords;
        StatementResult result;
        TreeMap <String, String> ret = new TreeMap<>();
        Integer topCount = 20;
        for(Integer year = start; year <= end; year ++){
            retKeywords = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            result = session.run("MATCH (paper:Paper) WHERE paper.journal = '"+journal+"' AND paper.year = '"
                    + year.toString()
                    + "' with paper as newinput MATCH (newinput) - [r:HAS_KEYWORD] -> (keywords: Keyword) " +
                    "RETURN keywords");
            while (result.hasNext()) {
                Record record = result.next();
                retKeywords.add(record.get("keywords").get("keyword").toString());
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
                if (cnt > topCount){
                    break;
                }
                String key = entry.getKey();
                sb.append(key);
                sb.append(", ");
                cnt ++;
            }
            if (sb.length() > 2) {
                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);
            }
            ret.put(year.toString(), sb.toString().replaceAll("\"", ""));
            //System.out.println(year.toString() + "; " + sb.toString().replaceAll("\"", ""));
        }

        session.close();

        return ok(Json.toJson(ret));
    }
}
