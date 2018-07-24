package controllers;

import models.Author;
import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class AuthorDirNetworkController extends Controller {

    private Driver driver;

    public AuthorDirNetworkController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result getAuthorNetworkByName(String name) {
        String resColl = "";
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");
        StatementResult result = session.run( "Match (a:Author)-[:WRITES]->(p:Paper)<-[:WRITES]-(b:Author) where a.authorName='"+name+"' return a, b" );
        HashSet<String> set = new HashSet<String>();
        Author a = new Author();
        List<Author> res = new ArrayList<Author>();
        while (result.hasNext()){
            Record record = result.next();
            if (a.name.equals("")){
                String nameA = record.get("a").get("authorName").toString();
                a.name = nameA.substring(1, nameA.length() - 1);
                set.add(nameA);
                res.add(0, a);
            }
            String nameB = record.get("b").get("authorName").toString();
            if (!set.contains(nameB)){
                Author b = new Author();
                b.name = nameB.substring(1, nameB.length() - 1);
                set.add(nameB);
                res.add(b);
            }
        }

        //session.close();
        //driver.close();
        return ok(Json.toJson(res));
    }
}
