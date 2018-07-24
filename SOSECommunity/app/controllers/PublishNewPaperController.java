package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Paper;
import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Wenlu on 11/19/16.
 */
public class PublishNewPaperController extends Controller {
    private Driver driver;

    public PublishNewPaperController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result addNewPapers(String title, String year, String authors, String journal, String volume) {
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");

        //Create Paper Node
        StatementResult addPaperNodeResult = session.run(" Create (paper: Paper{title : '" + title + "', " +
                                                                                "year: '" + year + "', " +
                                                                                "journal : '" + journal + "', " +
                                                                                "volume : '" + volume + "'})");

        String[] authorNames = authors.split(",");

        for (String authorName : authorNames) {
            System.out.println(authorName);
            //Create Relationship between Paper and Author
            StatementResult addPaperAuthorResult = session.run(" MATCH (author: Author{authorName: '" + authorName + "'})" +
                                                 " MATCH (paper: Paper{title: '" + title + "'})" +
                                                 " CREATE (author)-[:WRITES]->(paper)");

            //Create Relationship between Paper and Follower
            StatementResult addAuthorFollowerRelationship = session.run(" MATCH (follower) -[:FOLLOW]->(author: Author{authorName: '" + authorName + "'})" +
                                                                        " MATCH (paper: Paper{title: '" + title + "'})" +
                                                                        " CREATE (paper)-[:NOTIFY]->(follower)");
        }


        session.close();
        return ok("Success");
    }

    public Result getNotifications(String name) {
//        MATCH (n:Company {companyName:"Amagi Media Labs"})-[r:HAS_CUSTOMER]-(m:Company {companyName:"IBN7"})
//        DELETE r
//        RETURN n,r;
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");

        StatementResult result = session.run(" MATCH (paper)-[:NOTIFY]->(you: Author{authorName: '" + name + "'})" +
                                             "  RETURN paper");

        String res = "";
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        HashSet<Paper> set = new HashSet<Paper>();

        while (result.hasNext()) {
            Record record = result.next();
            Paper paper = new Paper();

            String title = record.get("paper").get("title").toString().replace("\"","");
            String journal = record.get("paper").get("journal").toString().replace("\"","");
            String year = record.get("paper").get("year").toString().replace("\"","");
            String volume = record.get("paper").get("volume").toString().replace("\"","");

            paper.setYear(year);
            paper.setJournal(journal);
            paper.setTitle(title);
            paper.setVolume(volume);

            res = res + paper.getTitle();
            set.add(paper);
//            System.out.println("In");
        }

        System.out.println(res);

        for (Paper paper: set) {
            String paperTitle = paper.getTitle();
            System.out.println(paperTitle);
            StatementResult authorResult = session.run( "  MATCH (author)-[:WRITES]->(paper: Paper{title: '" + paperTitle + "'})" +
                                                        "  RETURN author");
            ArrayList<String> authors = new ArrayList<>();
            while (authorResult.hasNext()) {
                Record record = authorResult.next();
                String authorName =  record.get("author").get("authorName").toString().replace("\"","");
                System.out.println(authorName);

                authors.add(authorName);
            }
            paper.setAuthors(authors);
            JsonNode node = Json.toJson(paper);
            array.add(node);
        }

//        MATCH (:Artist {Name: "Strapping Young Lad"})-[r:RELEASED]-(:Album {Name: "Heavy as a Really Heavy Thing"})
//        DELETE r

        StatementResult deleteRelationshipResult = session.run(" MATCH (paper)-[r:NOTIFY]->(you: Author{authorName: '" + name + "'})"+
                                                               " DELETE r ");

        session.close();
        System.out.println(array);
        return ok(array);

    }
}
