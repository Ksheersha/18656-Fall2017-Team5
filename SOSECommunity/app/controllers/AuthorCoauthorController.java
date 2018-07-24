package controllers;

import org.neo4j.driver.v1.*;

import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;


/**
 * Created by bowenyang on 11/4/16.
 */

public class AuthorCoauthorController extends Controller {

    private Driver driver;

    public AuthorCoauthorController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result getCoauthorInfo(String names) {
        Session session = driver.session();
        String res = "";
        response().setHeader("Access-Control-Allow-Origin", "*");
        ArrayList<String> paperList = new ArrayList<>();
        names = names.replaceAll("_", " ");
        String[] nameArray = names.split(";");

        StatementResult result = session.run( "match (a1 : Author {authorName:'"
                + nameArray[0] + "'})-[r1:WRITES]->(paper:Paper)<-[r2:WRITES]-(a2:Author {authorName:'"
                + nameArray[1] + "'}) return paper" );

        StringBuilder sb = new StringBuilder();
        while (result.hasNext())
        {
            Record record = result.next();
            //paperList.add(record.get("paper").get("title").toString().replace("\"", ""));
            sb.append(record.get("paper").get("title").toString().replace("\"", "")).append('\n');
        }

        session.close();
        return ok(sb.toString());
    }
}

