package domain.model;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

/**
 * Created by imrenagi on 11/3/17.
 */
@NodeEntity
public class Paper {

    public final static String TYPE = "Paper";

    @GraphId
    public Long graphId;

//    @Id
    public String index;

    public String ee;
    public String journal;
    public String key;
//    public String mdate
    public String number;
    public String page;
    public String title;
    public String url;
    public String volume;
    public String year;
    public String pAbstract;

    protected Paper() {}

    @Relationship(type = "WRITES", direction = Relationship.INCOMING)
    public Set<Author> authors;

    @Relationship(type = "CITE")
    public Set<Paper> papers;

}
