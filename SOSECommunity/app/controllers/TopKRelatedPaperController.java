package controllers;


import models.ContributorsByJournal;
import models.Paper;
import models.TopKRelatedPaper;
import org.neo4j.driver.internal.value.IntegerValue;
import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;


public class TopKRelatedPaperController extends Controller {

    private Driver driver;

    public TopKRelatedPaperController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result getTopKRelatedPaper(String keywords) {
        if (keywords == null || keywords.length() == 0) {
            return ok("No Experts in this criteria. Please retry other keywords.");
        }

        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");

        String[] keywordsArray = keywords.split(",");
        HashMap<String, Integer> paperCount = new HashMap<String, Integer>();
        HashMap<String, List<String>> paperAuthor = new HashMap<String, List<String>>();
        HashMap<String, List<String>> paperAuthorFinal = new HashMap<String, List<String>>();
        List<List<String>> links = new ArrayList<List<String>>();
        TopKRelatedPaper finalR = new TopKRelatedPaper();

        for (String keyword : keywordsArray){
            StatementResult result = session.run("MATCH (a:Author)-[:WRITES]->(p:Paper)-[:HAS_KEYWORD]->(k:Keyword {keyword:\""
                    + keyword + "\"}) RETURN a, p");
            while (result.hasNext()) {
                Record record = result.next();
                String authorName = record.get("a").get("authorName").toString();
                String paperTitle = record.get("p").get("title").toString();
                authorName = authorName.substring(1, authorName.length() - 1);
                paperTitle = paperTitle.substring(1, paperTitle.length() - 1);
                if (paperCount.containsKey(paperTitle)){
                    paperCount.put(paperTitle, paperCount.get(paperTitle) + 1);
                } else {
                    paperCount.put(paperTitle, 1);
                }
                if (paperAuthor.containsKey(paperTitle)){
                    paperAuthor.get(paperTitle).add(authorName);
                } else {
                    List<String> temp = new ArrayList<String>();
                    temp.add(authorName);
                    paperAuthor.put(paperTitle, temp);
                }
                System.out.println(authorName + "\t" + paperTitle);
            }
        }
        PriorityQueue<Map.Entry<String, Integer>> maxH =
                new PriorityQueue<Map.Entry<String, Integer>>(new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                            return o2.getValue() - o1.getValue();
                    }
                });


        for(Map.Entry<String,Integer> entry: paperCount.entrySet()){
            maxH.add(entry);
        }

        List<String> resPaper = new ArrayList<String>();
        while(resPaper.size() <= 30){
            Map.Entry<String, Integer> entry = maxH.poll();
            paperAuthorFinal.put(entry.getKey().toString(),paperAuthor.get(entry.getKey()));
            resPaper.add(entry.getKey());
        }

        HashSet<String> resAuthor = new  HashSet<String>();
        for (String e : resPaper){
            for (String m: paperAuthor.get(e)){
                resAuthor.add(m);
            }
        }
        finalR.authorNode = new ArrayList<String>(resAuthor);
        finalR.paperNode = resPaper;

        for (Map.Entry e : paperAuthorFinal.entrySet()){
            List<String> lists = new ArrayList<String>();
            lists.add(0, e.getKey().toString());
            for (String m :(List<String>) e.getValue()){
                lists.add(m);
            }
            links.add(lists);
        }
        finalR.paperAuthorLinks = links;

        session.close();
        return ok(Json.toJson(finalR));
    }
}






