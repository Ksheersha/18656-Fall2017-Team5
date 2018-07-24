package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by Wenlu on 11/22/16.
 */

public class RecommendationController extends Controller {
    private Driver driver;

    public RecommendationController () {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result addRelatedRelationship(String relatedItem, String category) {
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");

        String[] items = relatedItem.split(",");
//       String Node = "";
        String field = "";

        if (category.equals("author")) {
//            Node = "Author";
            field = "authorName";
        } else if (category.equals("paper")) {
//            Node = "Paper";
            field = "title";
        } else if (category.equals("keyword")) {
//            Node = "Keyword";
            field = "keyword";
        }

        for (int i = 0 ; i < items.length; i++) {
            String item1 = items[i];
            for (int j = i + 1; j < items.length; j++) {
                String item2 = items[j];
                StatementResult addRelatedRelationshipResult = session.run(" MATCH (item1 ) " +
                                                                           " MATCH (item2 ) " +
                                                                           " WHERE item1." + field + " = '" + item1 + "'" +
                                                                           " AND item2." + field + " = '" + item2 + "'" +
                                                                           " CREATE (item1) -[:INTERESTED_IN]-> (item2)" +
                                                                           " CREATE (item2) -[:INTERESTED_IN]-> (item1)");
            }

        }

        session.close();
        return ok("Success");
    }

    public Result getRecommendation(String name, String category) {

        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");

        String field = "";
        if (category.equals("author")) {
            field = "authorName";
        } else if (category.equals("paper")) {
            field = "title";
        } else if (category.equals("keyword")) {
            field = "keyword";
        }

        StatementResult result = session.run(" MATCH (item1) -[:INTERESTED_IN]-> (item2) " +
                                             " WHERE item1." + field + " = '" + name + "'" +
                                             " RETURN item2 ");

        //StringBuilder sb = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();

        while (result.hasNext()) {
            Record record = result.next();

            String content = record.get("item2").get(field).toString().replace("\"","");
            //sb.append(content);
            //sb.append(",");

            ObjectNode singleItem= Json.newObject();
            singleItem.put("name", content);
            array.add(singleItem);
        }

        session.close();
        //String res = sb.substring(0, sb.length() - 1);
        //System.out.print(res);

        return ok(array);
    }

    public Result getShortestPath(String name1, String name2) {
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");

        StatementResult result = session.run("MATCH r = (a1:Author{authorName:'" + name1 +"'}),"+
                                             "(a2:Author{authorName:'" + name2 +"'})" +
                                             "MATCH p=shortestPath((a1)-[WRITES*]-(a2))" +
                                             "RETURN [ n in nodes(p) | n.authorName ]");

        StringBuilder sb = new StringBuilder();
        while (result.hasNext()) {
            Record record = result.next();
//            Value value = record.get("n");
            String name = record.toString();
//            if (value.containsKey("authorName")) {
//                name = value.get("authorName").toString();
//            } else if (value.containsKey("keyword")) {
//                name = value.get("keyword").toString();
//            } else if (value.containsKey("title")) {
//                name = value.get("title").toString();
//            }

            System.out.println(name);
            sb.append(name);
        }
        session.close();
        return ok(sb.toString());
    }
}
