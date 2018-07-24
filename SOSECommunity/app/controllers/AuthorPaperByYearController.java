package controllers;

import org.neo4j.driver.v1.*;
import play.mvc.*;
import org.neo4j.driver.v1.*;

import play.mvc.Controller;
import play.mvc.Result;
import scala.util.parsing.json.JSONObject;
import views.html.graph;
import views.html.index;

import java.util.*;
import java.util.TreeMap;


public class AuthorPaperByYearController extends Controller {

    private Driver driver;

    public AuthorPaperByYearController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result getAuthorPaperByYear(String name, String yearS, String yearE) {
        String resY = "";
        String resP = "";
        System.out.println(yearS + yearE);
        response().setHeader("Access-Control-Allow-Origin", "*");
        TreeMap<String, Integer> author_paper = new TreeMap<String, Integer>();

        Session session = driver.session();
        StatementResult result = session.run( "Match (a:Author)-[:WRITES]->(p:Paper) where a.authorName='"+name+"' return p" );
        while (result.hasNext()){
            Record record = result.next();
            String yearTemp = record.get("p").get("year").toString();
            String year = yearTemp.substring(1,yearTemp.length() - 1);
            System.out.println(year);
            if ((Integer.parseInt(yearS) <= Integer.parseInt(year)) &&
                    (Integer.parseInt(year) <= Integer.parseInt(yearE))) {

                if (author_paper.containsKey(year)){
                    author_paper.put(year, author_paper.get(year) + 1);
                } else {
                    author_paper.put(year, 1);
                }
                //author_paper.put(year, author_paper.getOrDefault(year, 0) + 1);
            }
        }

        for (Map.Entry e : author_paper.entrySet()){
            resY = resY.concat(e.getKey().toString() + ",");
            resP = resP.concat(e.getValue().toString() + ",");
        }
        String finalRes = resY.substring(0, resY.length() - 1) + "*" + resP.substring(0, resP.length() - 1);
        //session.close();
        //driver.close();
        return ok(finalRes);
    }

}
