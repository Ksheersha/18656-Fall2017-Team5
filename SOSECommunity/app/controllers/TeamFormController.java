package controllers;

import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

/**
 * Created by sagejoyoox on 11/22/16.
 */
public class TeamFormController extends Controller {
    private Driver driver;

    public TeamFormController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    //http://localhost:9000/researchTeam/data
    public Result formTeam(String keyword) {
        if (keyword == null || keyword.length() == 0) {
            return ok("No Experts in this criteria. Please retry other keywords.");
        }

        keyword = keyword.toLowerCase();

        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");

        StatementResult result = session.run("MATCH (a:Author)-[:WRITES]->(p:Paper)-[:HAS_KEYWORD]->(k:Keyword {keyword:\""
                + keyword + "\"}) RETURN a");

        Map<String, Integer> authorCounts = new HashMap<String, Integer> ();

        while (result.hasNext()) {
            Record record = result.next();
            String author = record.get("a").get("authorName").toString();
            if (!authorCounts.containsKey(author)) {
                authorCounts.put(author, 0);
            }
            authorCounts.put(author, authorCounts.get(author) + 1);
        }

        Queue<AuthorCount> pq = new PriorityQueue<AuthorCount>(new Comparator<AuthorCount>() {
            @Override
            public int compare(AuthorCount o1, AuthorCount o2) {
                return o2.count - o1.count;
            }
        });

        for (String key : authorCounts.keySet()) {
            pq.offer(new AuthorCount(key, authorCounts.get(key)));
        }

        List<String> authors = new ArrayList<String>();
        Map<String, Integer> cooperateCount = new HashMap<String, Integer>();

        for (int i = 0; i < 50 && !pq.isEmpty(); i++) {
            String name = pq.poll().name;
            authors.add(name);
            cooperateCount.put(name, 100);
        }

        for (int i = 0; i < authors.size(); i++) {
            for (int j = i + 1; j < authors.size(); j++) {
                String name1 = authors.get(i);
                String name2 = authors.get(j);
                StatementResult relationResult = session.run("MATCH r=(a:Author {authorName:" + name1+ "})-[:WRITES]->" +
                        "(p:Paper)<-[:WRITES]-(o:Author {authorName:"
                        + name2 + "}) RETURN COUNT(r) AS count");

                while (relationResult.hasNext()) {
                    Record record = relationResult.next();
                    int num = Integer.parseInt(record.get("count").toString());
                    cooperateCount.put(name1, cooperateCount.get(name1) + num);
                    cooperateCount.put(name2, cooperateCount.get(name2) + num);
                }
            }
        }

        Queue<RelationCount> relationPq = new PriorityQueue<RelationCount>(new Comparator<RelationCount>() {
            @Override
            public int compare(RelationCount o1, RelationCount o2) {
                return o2.count - o1.count;
            }
        });

        for (String key : cooperateCount.keySet()) {
            relationPq.offer(new RelationCount(key, cooperateCount.get(key)));
        }

        List<String> group = new ArrayList<String>();
        for (int i = 0; i < 20 && !relationPq.isEmpty(); i++) {
            group.add(relationPq.poll().name.replaceAll("^\\\"", "").replaceAll("\\\"$", ""));
        }

        session.close();
        return ok(Json.toJson(group));
    }
}

class RelationCount {
    String name;
    int count;
    public RelationCount(String name, int count) {
        this.name = name;
        this.count = count;
    }
}
