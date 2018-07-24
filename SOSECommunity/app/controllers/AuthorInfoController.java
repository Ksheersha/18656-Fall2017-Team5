package controllers;

import models.Author;

import org.neo4j.driver.v1.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class AuthorInfoController extends Controller {
	
	private Driver driver;
	
	public AuthorInfoController() {
        driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
	}
	
	public Result getAuthorInfo(String name) {
		Session session = driver.session();
		response().setHeader("Access-Control-Allow-Origin", "*"); 
		name = name.replaceAll("_", " ");
		
        StatementResult result = session.run( "match (a1 : Author) where a1.authorName = '"
        						+ name + "' return a1" );
                
        Author author = new Author();
        
        if (result.hasNext())
        {
            Record record = result.next();
            author.index = record.get("a1").get("index").toString().replaceAll("\"", "");
            author.name = record.get("a1").get("authorName").toString().replaceAll("\"", "");
            author.homepage = record.get("a1").get("homepage").toString().replaceAll("\"", "");
            author.picture = record.get("a1").get("picture").toString().replaceAll("\"", "");
            author.interests = record.get("a1").get("interests").toString().replaceAll("\"", "");
            author.email = record.get("a1").get("email").toString().replaceAll("\"", "");
            author.affiliation = record.get("a1").get("affiliation").toString().replaceAll("\"", "");
        }
        
		session.close();
		return ok(Json.toJson(author));
	}
	
}
