package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;

/**
 * Created by sagejoyoox on 11/4/16.
 */

/**
 * Created by Wenlu on 11/2/16.
 */
public class PaperInfoController extends Controller{
    private Driver driver;

    public PaperInfoController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result getAuthorByPaper (String title) {
        Session session = driver.session();

        response().setHeader("Access-Control-Allow-Origin", "*");

        StatementResult result = session.run(" MATCH (a:Author)-[:WRITES]->(p:Paper {title:\""
                + title + "\"}) RETURN a");

        StringBuilder sb = new StringBuilder();

        while (result.hasNext()) {
            Record record = result.next();
            sb.append(record.get("a").get("authorName").toString().replaceAll("\"", ""));
            sb.append("\n");
        }

        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        
        session.close();
        return ok(sb.toString());
    }
    public Result getPaperInfo(String name) {
        Session session = driver.session();
        
        response().setHeader("Access-Control-Allow-Origin", "*");

        name = name.replace("_", " ");
        //Time_in_Multidimensional_Databases.
        //
        StatementResult result = session.run( "match (paper)-[:REFERS_TO]->(citation) where paper.title = '"
                + name + "' return citation" );
        String res = "";
        ArrayList<String> citationList = new ArrayList<>();
        
        while (result.hasNext()) {
            Record record = result.next();
            String temp = record.get("citation").get("title").toString().replaceAll("\"", "");
            citationList.add(temp);
            res = res + temp + "\n";
            System.out.println(temp);
        }

        session.close();
        return ok(res);
        //return ok(views.html.paper.render(name, citationList));
    }

    public Result getTopCitedPaperByJournal (String journal) {
        Session session = driver.session();

        response().setHeader("Access-Control-Allow-Origin", "*");

        //StatementResult result = session.run(" MATCH (paper)-[:REFERS_TO]->(citedPaper:Paper {journal: '" + journal + "'})" +
        //                                     " WITH citedPaper, count(paper) as count" +
        //                                     " RETURN citedPaper.title, count" +
        //                                     " ORDER BY count DESC" +
        //                                     " LIMIT 20");

        StatementResult result = session.run(" MATCH (paper)-[:REFERS_TO]->(citedPaper)" +
                                                     " WHERE paper.journal = '" + journal + "' OR citedPaper.journal = '"+ journal + "'" +
                                                     " WITH citedPaper, count(paper) as count" +
                                                     " RETURN citedPaper.title, count" +
                                                     " ORDER BY count DESC" +
                                                     " LIMIT 20");
        System.out.println("Printing journal " +journal);


        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
//        StringBuilder sb = new StringBuilder();

        while (result.hasNext()) {
            Record record = result.next();
            //String title = record.get("citedPaper").get("title").toString().replace("\"","");
            String title = record.get("citedPaper.title").toString().replace("\"","");
            System.out.println("Printing title" +title);
            String count = record.get("count").toString();

            ObjectNode singleItem= Json.newObject();
            singleItem.put("title", title);
            singleItem.put("count", count);
            array.add(singleItem);
        }

        session.close();
        return ok(array);
    }

    public Result getTopCitedPaperByJournalAndYear(String journal, String year) {
        //String journal
        Session session = driver.session();

        response().setHeader("Access-Control-Allow-Origin", "*");

        String[] period = year.split(",");

        String start = period[0];
        String end = start;

        if (period.length > 1 ) {
            end = period[1];
        }
          //:Paper {journal: '" + journal + "'}


          StatementResult result = session.run(" MATCH (paper)-[:REFERS_TO]->(citedPaper)" +
                                                      //" WHERE citedPaper.journal = '"+ journal + "' OR paper.journal = '"+ journal + "' AND citedPaper.year >= '" + start + "'AND citedPaper.year <= '" + end + "'" +
                                                      " WHERE paper.journal = '"+ journal + "' AND citedPaper.year >= '" + start + "'AND citedPaper.year <= '" + end + "'" +
                                                      " WITH citedPaper, count(paper) as count" +
                                                      " RETURN citedPaper, count" +
                                                      " ORDER BY count DESC" +
                                                      " LIMIT 10");




        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();

        while (result.hasNext()) {
            Record record = result.next();
            //String paperTitle = record.get("citedPaper").get("title").toString().replace("\"","");
            String paperTitle = record.get("citedPaper").get("title").toString().replace("\"","");
            System.out.println("Printing title" +paperTitle);
            String paperYear = record.get("citedPaper").get("year").toString().replace("\"","");
            String paperJournal = record.get("citedPaper").get("journal").toString().replace("\"","");
            String paperCount = record.get("count").toString();

            ObjectNode singleItem= Json.newObject();
            singleItem.put("title", paperTitle);
            singleItem.put("journal", paperJournal);
            singleItem.put("year", paperYear);
            singleItem.put("count", paperCount);
            array.add(singleItem);
        }

        session.close();
        return ok(array);
    }
}
