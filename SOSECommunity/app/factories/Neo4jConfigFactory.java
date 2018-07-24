package factories;

import org.neo4j.ogm.config.Configuration;

/**
 * Created by imrenagi on 10/31/17.
 */
public class Neo4jConfigFactory {

    private static Neo4jConfigFactory factory = new Neo4jConfigFactory();

    public static Neo4jConfigFactory getInstance() {
        return factory;
    }

    private Neo4jConfigFactory() {}

    public Configuration getConfig() {
        Configuration configuration = new Configuration.Builder()
                .uri("bolt://localhost")
                .credentials("neo4j", "123456")
                .build();
        return configuration;
    }

}
