package domain.model;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

/**
 * Created by imrenagi on 10/31/17.
 */
@NodeEntity
public class Author {

    public final static String TYPE = "Author";

    @GraphId
    private Long id;

//    @Id
    private String index;
    private String authorName;

    @Relationship(type = "COAUTHOR", direction = Relationship.OUTGOING)
    private Set<Author> coauthors;

    @Relationship(type = "WRITES", direction = Relationship.OUTGOING)
    private Set<Paper> papers;

    protected Author() {}

    public String getIndex() {
        return index;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Set<Author> getCoauthors() {
        return this.coauthors;
    }

    public Long getId() {
        return id;
    }

    public Set<Paper> getPapers() {
        return papers;
    }
}
