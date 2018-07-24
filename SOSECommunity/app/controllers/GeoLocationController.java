package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Wenlu on 12/6/16.
 */
public class GeoLocationController extends Controller {
    private Driver driver;

    public GeoLocationController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result countryContributorInTopic(String country, String keyword) {
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");

        StatementResult getContributorResult = session.run("match (author)-[:WRITES]->(paper)-[:HAS_KEYWORD]->" +
                "(key:Keyword{keyword:'"+keyword+"'})" +
                " where author.country = '"+ country +"' "+
                " with count(distinct(paper)) as cnt, paper return cnt, paper.title as title");

        String temp = "";
        int count = 0;
        List<String> papers = new ArrayList<>();
        while (getContributorResult.hasNext()) {
            Record record = getContributorResult.next();
            //temp = record.get("cnt").toString();
            papers.add(record.get("title").toString());
            count++;
        }

        String countString = String.valueOf(count);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();

        ArrayNode paperArr = mapper.createArrayNode();
        for (String title : papers) {
            paperArr.add(title.replaceAll("\"", ""));
        }

        ObjectNode singleItem= Json.newObject();
        singleItem.put("country", country);
        singleItem.put("count", countString);
        singleItem.put("papers", paperArr);
        array.add(singleItem);

        session.close();
        return ok(array);
    }

    public Result countryContributorInChannel(String channelName, String startYear, String endYear) {
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");

        //Get all distinct countries
        StatementResult getAllContries = session.run("match (author) " +
                "with distinct(author.country) as country " +
                "return country");

        HashMap<String,Integer> map = new HashMap<>();
        while(getAllContries.hasNext()) {
            Record record = getAllContries.next();
            String country = record.get("country").toString().replace("\"","");
            if (!country.equals("NULL") && !map.containsKey(country)) {
                map.put(country, 0);
            }
        }

        for (String country : map.keySet()) {
            int paperCount = 0;

            //Get paper from
            StatementResult getContributorResult = session.run("match (author)-[:WRITES]->(paper)" +
                    "where paper.journal = '" + channelName + "' AND paper.year >= '" + startYear + "' AND paper.year <= '" +endYear + "'" +
                    "AND author.country = '"+ country+"'" +
                    "return paper");

            while (getContributorResult.hasNext()) {
                Record record = getContributorResult.next();
                paperCount += 1;
            }

            map.put(country, paperCount);
        }


        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();

        for (String country : map.keySet()) {
            if(map.get(country) != 0) {
                ObjectNode singleItem = Json.newObject();
                singleItem.put("country", country);
                singleItem.put("count", map.get(country));
                array.add(singleItem);
            }
        }

        session.close();
        return ok(array);
    }
}
