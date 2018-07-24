import org.neo4j.driver.v1.*;

public class Test {

    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
        Session session = driver.session();
        
        StatementResult result = session.run( "match (a1 : PCMember) return a1" );
        while (result.hasNext())
        {
            Record record = result.next();
            System.out.println(record.get("a1").get("name"));
        }

        session.close();
        driver.close();
    }

}
