package controllers;

import org.neo4j.driver.v1.*;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashSet;

/**
 * Created by Wenlu on 11/17/16.
 */
public class FollowingRelationshipController extends Controller {
    private Driver driver;

    public FollowingRelationshipController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
    }

    public Result addFollowers(String myName, String otherName) {
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");
        StatementResult result = session.run(" MATCH (you: Author{authorName: '" + myName + "'})" +
                " MATCH (other: Author{authorName:'" + otherName + "'}) CREATE (you)-[:FOLLOW]->(other)");

        session.close();
        return ok("Success");

    }

    public Result getFollowers(String name) {
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");
        StatementResult result = session.run(" MATCH (you: Author{authorName: '" + name + "'})" +
                " -[:FOLLOW]-> (other: Author) return other");

        String followers = "";
        HashSet<String> followersSet = new HashSet<String>();

        while (result.hasNext()) {
            Record record = result.next();
            String temp = record.get("other").get("authorName").toString();
            temp = temp.replaceAll("\"", "");
            followersSet.add(temp);
        }

        for (String temp : followersSet) {
            followers = followers + temp + ",";
        }

       // System.out.println(followers);
        if (followers.length() > 0) {
            // substract the last comma
            followers = followers.substring(0, followers.length() - 1);
        }

        System.out.println(followers);
        session.close();
        return ok(followers);

    }

    /**
     * Created by Sheersha on 11/02/17.
     */
    public Result getMyFollowers(String name) {
            Session session = driver.session();
            response().setHeader("Access-Control-Allow-Origin", "*");
            StatementResult result = session.run(" MATCH (you: Author)" +
                    " -[:FOLLOW]-> (other: Author{authorName: '" + name + "'}) return you");

            String myfollowers = "";
            HashSet<String> followersSet = new HashSet<String>();

            while (result.hasNext()) {
                Record record = result.next();
                String temp = record.get("you").get("authorName").toString();
                temp = temp.replaceAll("\"", "");
                followersSet.add(temp);
            }

            for (String temp : followersSet) {
                myfollowers = myfollowers + temp + ",";
            }

           // System.out.println(myfollowers);
            if (myfollowers.length() > 0) {
                // substract the last comma
                myfollowers = myfollowers.substring(0, myfollowers.length() - 1);
            }

            System.out.println(myfollowers);
            session.close();
            return ok(myfollowers);

        }
}
