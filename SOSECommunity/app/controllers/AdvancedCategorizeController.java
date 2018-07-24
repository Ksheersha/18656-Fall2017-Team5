package controllers;

import play.mvc.Controller;

import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashSet;
import java.util.TreeMap;

/**
 * Created by bowenyang on 11/29/16.
 */
public class AdvancedCategorizeController extends Controller{

    private Driver driver;

    public AdvancedCategorizeController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result getAdvancedCategorize(String startYear, String endYear, String channel, String keywords) {
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");
        StatementResult result;
        TreeMap<String, String> ret = new TreeMap<>();
        String[] keywordsArray = keywords.split("_");
        // Then iterate over the keywords to send query
        for (String s: keywordsArray){
            if (s.isEmpty()){
                continue;
            }
            result = session.run("MATCH (paper:Paper) - [r: HAS_KEYWORD] -> (keywords: Keyword) WHERE paper.year >= '"
                    + startYear + "' AND paper.year <= '"+ endYear + "' AND paper.journal = '" + channel + "' AND keywords.keyword = '" + s + "' RETURN DISTINCT paper");
            System.out.println("MATCH (paper:Paper) - [r: HAS_KEYWORD] -> (keywords: Keyword) WHERE paper.year >= '"
                    + startYear + "' AND paper.year <= '"+ endYear + "' AND paper.journal = '" + channel + "' AND keywords.keyword = '" + s + "' RETURN DISTINCT paper");
            HashSet<String> uniqueTitles = new HashSet<>();
            while (result.hasNext()) {
                Record record = result.next();
                uniqueTitles.add(record.get("paper").get("title").toString().replaceAll("\"", ""));
            }
            ret.put(s, String.join(",", uniqueTitles));
            //System.out.println(s +","+String.join(",", uniqueTitles));
        }
        session.close();
        return ok(Json.toJson(ret));
    }
}
