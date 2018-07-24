package controllers;

import models.Author;
import models.AuthorNode;
import models.SpecialNode;
import org.neo4j.driver.internal.util.Iterables;
import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;


public class MultiCollaborationNetworkController extends Controller {

    private Driver driver;

    public MultiCollaborationNetworkController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result getcollaborationNetwork(String name) {
        String resColl = "";
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");
        StatementResult result = session.run( "Match (a:Author)-[:WRITES]->(p:Paper)<-[:WRITES]-(b:Author)-[:WRITES]->(v:Paper)<-[:WRITES]-(c:Author) where a.authorName='"+name+"' and " +
                "c.authorName<>'"+name+"' return a, b,c" );

        SpecialNode finalR = new SpecialNode();

        Map<Integer, HashSet<String>> res2 = new HashMap<Integer, HashSet<String>>();
        res2.put(0, new HashSet<String>());
        res2.put(1, new HashSet<String>());
        res2.put(2, new HashSet<String>());

        List<AuthorNode> res = new ArrayList<AuthorNode>();
        AuthorNode a = new AuthorNode();
        HashMap<String, AuthorNode> map = new HashMap<String, AuthorNode>();
        while (result.hasNext()){
            Record record = result.next();
            if (a.name.equals("")){
                String nameA = record.get("a").get("authorName").toString();
                a.name = nameA.substring(1, nameA.length() - 1);
                res.add(0, a);
                res2.get(0).add(a.name);
            }
            String nameB = record.get("b").get("authorName").toString();
            String nameC = record.get("c").get("authorName").toString();
            if (!map.containsKey(nameB)){
                AuthorNode b = new AuthorNode();
                b.name = nameB.substring(1, nameB.length() - 1);
                b.adjacentList.add(nameC.substring(1, nameC.length() - 1));
                map.put(nameB, b);
                a.adjacentList.add(b.name);
                res2.get(1).add(b.name);
                res2.get(2).add(nameC.substring(1, nameC.length() - 1));
            } else {
                map.get(nameB).adjacentList.add(nameC.substring(1, nameC.length() - 1));
                res2.get(2).add(nameC.substring(1, nameC.length() - 1));
            }
        }
        for (Iterator<String> e = res2.get(1).iterator(); e.hasNext();){
            res2.get(2).remove((String)e.next());
        }
        for (Map.Entry e : map.entrySet()){
            res.add((AuthorNode) e.getValue());
        }

        finalR.res1 = res;
        finalR.res2 = res2;
        //session.close();
        //driver.close();
        return ok(Json.toJson(finalR));
    }
}
