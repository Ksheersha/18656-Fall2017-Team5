package controllers;


import models.ContributorsByJournal;
import models.InterestedPaper;
import models.Paper;
import models.TopKRelatedPaper;
import org.neo4j.driver.internal.value.IntegerValue;
import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

/**
 * Created by wangziming on 11/25/16.
 */

public class CollRelatedPapersController extends Controller {

    private Driver driver;

    public CollRelatedPapersController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result getCollRelatedPapers(String keywords, String readername) {
        if (keywords == null || keywords.length() == 0) {
            return ok("No Experts in this criteria. Please retry other keywords.");
        }

        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");


        // 1. get reader's paper-keywords list



        // reader [paper-keywords]
        HashMap<String, HashSet<String>> reader = new HashMap<String, HashSet<String>>();

        // reader [keywords-paper]
        HashMap<String, HashSet<String>> readerinvertex = new HashMap<String, HashSet<String>>();

        // reader all keywords
        HashSet<String> readerKeywords = new  HashSet<String>();


        //reader contained paper set part1
        HashSet<String> part1ReaderPaperNodes = new  HashSet<String>();


        //reader contained paper set part2
        HashSet<String> part1NewPaperNodes = new HashSet<String>();


        StatementResult result = session.run("MATCH (n:Author)-[:WRITES]->(p:Paper)-[:HAS_KEYWORD]->(k:Keyword) where n.authorName='"+readername+"' return p, k");
        while (result.hasNext()) {
            Record record = result.next();

            String paperTitle = record.get("p").get("title").toString();
            paperTitle = paperTitle.substring(1, paperTitle.length() - 1);

            String keyword = record.get("k").get("keyword").toString();
            keyword = keyword.substring(1, keyword.length() - 1);
            // add reader all keywords
            readerKeywords.add(keyword);

            // reader [paper-keywords]
            if (reader.containsKey(paperTitle)){
                reader.get(paperTitle).add(keyword);
            } else {
                HashSet<String> keywordset = new HashSet<String>();
                keywordset.add(keyword);
                reader.put(paperTitle, keywordset);
            }

            // reader [key-papers]
            if (readerinvertex.containsKey(keyword)){
                readerinvertex.get(keyword).add(paperTitle);
            } else {
                HashSet<String> paperset = new HashSet<String>();
                paperset.add(paperTitle);
                readerinvertex.put(keyword, paperset);
            }
        }



        String[] keywordsArray = keywords.split(",");


        //2. contained keywords and not contained keywords
        List<String> containedwords = new ArrayList<String>();
        List<String> notcontainedwords = new ArrayList<String>();

        for (String e : keywordsArray){
            if (readerKeywords.contains(e)){
                containedwords.add(e);
            } else {
                notcontainedwords.add(e);
            }
        }

        //System.out.println(readerKeywords);
        //System.out.println(reader.entrySet());
        //System.out.println(readerinvertex.entrySet());

        //3. To get half part of contained keywords links
        List<List<String>> linksRe1 = new ArrayList<List<String>>();
        List<List<String>> linksRe2 = new ArrayList<List<String>>();
        for (String key : containedwords){
            List<String> links = new ArrayList<String>();
            links.add(0, key);
            StatementResult result1 = session.run("MATCH (p:Paper)-[:HAS_KEYWORD]->(k:Keyword {keyword:\""
                    + key + "\"}) RETURN p limit 10");
            while (result1.hasNext()) {
                Record record = result1.next();
                String title = record.get("p").get("title").toString();
                title = title.substring(1, title.length() - 1);
                if (!reader.containsKey(title)){
                    part1NewPaperNodes.add(title);
                    links.add(title);
                }
            }
            linksRe1.add(links);
            List<String> linkorigin = new ArrayList<String>(readerinvertex.get(key));
            linkorigin.add(0, key);
            linksRe2.add(linkorigin);
            part1ReaderPaperNodes.addAll(readerinvertex.get(key));
        }
        //System.out.println(containedwords);
        //System.out.println(linksRe2);
        //System.out.println(linksRe1);

        // unrelated keywords, count in the reader's keywords
        if (false){
            HashMap<String, Integer> countss = new HashMap<String, Integer>();

            for (String e : notcontainedwords){
                StatementResult result2 = session.run("MATCH (p:Paper)-[:HAS_KEYWORD]->(k:Keyword {keyword:\""
                        + e + "\"}) RETURN p");
                while (result2.hasNext()){
                    Record record = result2.next();
                    String title = record.get("p").get("title").toString();
                    StatementResult result3 = session.run("MATCH (p:Paper)-[:HAS_KEYWORD]->(k:Keyword) where p.title = '"+title+"' RETURN k");
                    while (result3.hasNext()){
                        Record records = result3.next();
                        String kk = records.get("k").get("keyword").toString();
                        if (readerinvertex.containsKey(kk)){
                            title = title.substring(1, title.length() - 1);
                            countss.put(title, countss.getOrDefault(title, 1));
                        }
                    }
                }
            }
        }

        InterestedPaper finalR = new InterestedPaper();
        finalR.linksRe1 = linksRe1;
        finalR.linksRe2 = linksRe2;
        finalR.containedwords = containedwords;
        finalR.part1ReaderPaperNodes = new ArrayList<String>(part1ReaderPaperNodes);
        finalR.part1NewPaperNodes = new ArrayList<String>(part1NewPaperNodes);

        ////// ////// ////// ////// ////// ////// ////// ////// ////// ////// //////

        session.close();
        return ok(Json.toJson(finalR));
    }
}






