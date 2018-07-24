package services;

import domain.model.Author;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by imrenagi on 10/31/17.
 */
public class AuthorServiceImpl extends GenericService<Author>   {

    public Iterable<Author> getAll() {
        return findAll();
    }

    public Iterable<Map<String, Object>> getAuthorNetworks() {
        String query = "MATCH (a:Author)-[r:COAUTHOR*]-(b:Author) return a,b,r limit 200";
        return this.session.query(query, Collections.EMPTY_MAP);
    }

    public Iterable<Map<String, Object>> getAuthorPaperNetwork() {
        String query = String.format("MATCH (n:Author) MATCH (n:Author)-[r:WRITES*]->(m)<-[:WRITES]-(n2),\n" +
                " (n2)-[x:WRITES*]->(z)\n" +
                " RETURN n,r,m,n2,x,z LIMIT 100");
        return this.session.query(query, Collections.EMPTY_MAP);
    }

    public Iterable<Map<String, Object>> shortestPath(String firstAuthorName, String secondAuthorName) {
        String query = String.format("MATCH p=shortestPath((a:Author {authorName: $author1 } )-[r1:COAUTHOR*1..20]-(b:Author {authorName: $author2 }))\n" +
                "return p");
        Map<String, String> m = new HashMap<>();
        m.put("author1", firstAuthorName);
        m.put("author2", secondAuthorName);
        return this.session.query(query, m);
    }

    @Override
    public Class<Author> getEntityType() {
        return Author.class;
    }
}
