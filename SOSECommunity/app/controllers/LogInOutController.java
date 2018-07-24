package controllers;

import java.util.Date;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import play.mvc.Controller;
import play.mvc.Result;

public class LogInOutController extends Controller {
	private Driver driver;
	
	public LogInOutController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
	}
	
	public Result authenticate(String name, String password) {
		Session session = driver.session();
		response().setHeader("Access-Control-Allow-Origin", "*"); 
		name = name.replaceAll("_", " ");
		
        StatementResult result = session.run( "match (a1 : Author) where a1.authorName = '"
        						+ name + "' return a1" );
                        
        if (result.hasNext())
        {
            Record record = result.next();
            String pass = record.get("a1").get("password").toString().replaceAll("\"", "");
            if (pass.equals("NULL")) {
            	session.close();
            	return ok("New");
            }
            String visited = record.get("a1").get("visited").toString().replaceAll("\"", "");
            if (pass.equals(password)) {
            	session.close();
            	return ok(visited);
            } else {
            	session.close();
            	return ok("Fail");
            }
        }
        
		session.close();
		return ok("No");
	}
	
	public Result login(String name) {
		Session session = driver.session();
		response().setHeader("Access-Control-Allow-Origin", "*"); 
		name = name.replaceAll("_", " ");
		
		session.run( "match (a1 : Author) where a1.authorName = '"
				+ name + "' set a1.visited='" + new Date().toString() + "'");
        
		session.close();
		
		return ok("OK");
	}
	
	public Result register(String name, String password) {
		Session session = driver.session();
		response().setHeader("Access-Control-Allow-Origin", "*"); 
		name = name.replaceAll("_", " ");
		
		session.run( "match (a1 : Author) where a1.authorName = '"
				+ name + "' set a1.password='" + password
				+ "', a1.visited='" + new Date().toString() + "'");
		
		session.close();
		return ok("OK");
	}
	
}
