package controllers;

import play.mvc.*;
import org.neo4j.driver.v1.*;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        String res = "hello, ";
        String temp = "";
        Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
        Session session = driver.session();
        StatementResult result = session.run( "match (n:Author) return n limit 10" );
        while (result.hasNext())
        {
            Record record = result.next();
            temp = record.get("n").get("authorName").toString().replace("\"", "");
            res += temp + ", ";
            System.out.println(res);
        }
        session.close();
        driver.close();
        return ok(res);
    }

}