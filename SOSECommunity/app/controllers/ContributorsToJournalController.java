package controllers;

import models.ContributorsByJournal;
import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import org.neo4j.driver.v1.*;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;






public class ContributorsToJournalController extends Controller {

    private Driver driver;

    public ContributorsToJournalController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result getContributorsByJournal(String journal) {
        String resAuthor = "";
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");
        StatementResult result = session.run("MATCH (n:Author)-[:WRITES]->(p:Paper) " +
                "where p.journal = '"+journal+"' and p.volume <> 'Null'  RETURN n, p");
        TreeMap<String, HashSet<String>> resultMap = new TreeMap<String, HashSet<String>>();
        while (result.hasNext()){
            Record record = result.next();
            String contributors = record.get("n").get("authorName").toString();
            String volume = record.get("p").get("volume").toString();
            System.out.print(contributors);
            System.out.println(volume);
            if (resultMap.containsKey(volume)){
                resultMap.get(volume).add(contributors);
            } else {
                HashSet<String> sub = new HashSet<String>();
                sub.add(contributors);
                resultMap.put(volume, sub);
            }
        }
        ContributorsByJournal contri = new ContributorsByJournal();
        for (Map.Entry e : resultMap.entrySet()){
            String volume = e.getKey().toString();
            contri.volume.add(volume.substring(1, volume.length() - 1));
            contri.count.add(resultMap.get(volume).size());
        }
        //session.close();
        //driver.close();

        return ok(Json.toJson(contri));
    }
}

