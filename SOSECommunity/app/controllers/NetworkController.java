package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.responses.*;
import domain.model.Author;
import domain.model.Paper;
import domain.model.Path;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalPath;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.ogm.response.model.RelationshipModel;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.AuthorServiceImpl;
import services.PaperServiceImpl;

import java.util.*;

/**
 * Created by sagejoyoox on 11/22/16.
 */
public class NetworkController extends Controller {

    private Driver driver;
    private AuthorServiceImpl authorService;
    private PaperServiceImpl paperService;

    public NetworkController() {
        driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "123456"));

        this.authorService = new AuthorServiceImpl();
        this.paperService = new PaperServiceImpl();
    }

    public Result personToPersonNetwork() {
        response().setHeader("Access-Control-Allow-Origin", "*");

        Map<Long, Author> authorMap = new HashMap<>();
        Map<Long, Integer> authorIdx = new HashMap<>();
        int count = 0;

        List<String> names = new ArrayList<>();
        List<Path> paths = new ArrayList<>();

        Iterator<Map<String, Object>> iterator = authorService.getAuthorNetworks().iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue().getClass() == Author.class) {
                    Author a = (Author) entry.getValue();
                    authorMap.put(a.getId(), a);
                    if (!authorIdx.containsKey(a.getId())) {
                        authorIdx.put(a.getId(), count++);
                        names.add(a.getAuthorName());
                    }
                } else {
                    ArrayList<RelationshipModel> arr = (ArrayList<RelationshipModel>) entry.getValue();
                    for (RelationshipModel r : arr) {
                        Path path = new Path(authorIdx.get(r.getStartNode()), authorIdx.get(r.getEndNode()));
                        paths.add(path);
                    }
                }
            }
        }

        Map<String, Object> m = new HashMap<>();
        m.put("authors", names);
        m.put("paths", paths);
        JsonNode jsonNode = Json.toJson(m);

        return ok(jsonNode);
    }

    public Result paperToPersonNetwork() {
        response().setHeader("Access-Control-Allow-Origin", "*");


        GraphDTO graphDTO = new GraphDTO();

        Iterator<Map<String,Object>> iterator = authorService.getAuthorPaperNetwork().iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue().getClass() == Author.class) {
                    Author a = (Author) entry.getValue();

                    AuthorDTO authorDTO = new AuthorDTO(a.getId(), a.getAuthorName());
                    NodeDTO nodeDTO = new NodeDTO(authorDTO.nodeId, Author.TYPE, authorDTO);

                    graphDTO.insertNode(nodeDTO);

                } else if (entry.getValue().getClass() == Paper.class) {
                    Paper p = (Paper) entry.getValue();
                    PaperDTO paperDTO = new PaperDTO(p.graphId, p.title);
                    NodeDTO nodeDTO = new NodeDTO(paperDTO.nodeId, Paper.TYPE, paperDTO);

                    graphDTO.insertNode(nodeDTO);
                } else {
                    ArrayList<RelationshipModel> arr = (ArrayList<RelationshipModel>) entry.getValue();
                    for (RelationshipModel r : arr) {

                        EdgeDTO edgeDTO = new EdgeDTO(r.getStartNode(),
                                r.getEndNode(),
                                r.getType(),
                                null);
                        graphDTO.insertEdge(edgeDTO);
                    }
                }
            }
        }

        List<GraphDTO> graphs = new ArrayList<>();
        graphs.add(graphDTO);

        Map<String, Object> m = new HashMap<>();
        m.put("graphs", graphs);
        JsonNode jsonNode = Json.toJson(m);

        return ok(jsonNode);
    }

    public Result paperToPaperNetwork() {
        response().setHeader("Access-Control-Allow-Origin", "*");

        GraphDTO graphDTO = new GraphDTO();

        Iterator<Map<String,Object>> iterator = paperService.getPaperNetwork().iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                 if (entry.getValue().getClass() == Paper.class) {
                    Paper p = (Paper) entry.getValue();
                    PaperDTO paperDTO = new PaperDTO(p.graphId, p.title);
                    NodeDTO nodeDTO = new NodeDTO(paperDTO.nodeId, Paper.TYPE, paperDTO);

                    graphDTO.insertNode(nodeDTO);
                } else {
                    RelationshipModel r  = (RelationshipModel) entry.getValue();
                        EdgeDTO edgeDTO = new EdgeDTO(r.getStartNode(),
                                r.getEndNode(),
                                r.getType(),
                                null);
                        graphDTO.insertEdge(edgeDTO);
                }
            }
        }

        List<GraphDTO> graphs = new ArrayList<>();
        graphs.add(graphDTO);

        Map<String, Object> m = new HashMap<>();
        m.put("graphs", graphs);
        JsonNode jsonNode = Json.toJson(m);

        return ok(jsonNode);

    }

    public Result shortestPath(String firstAuthor, String secondAuthor) {
        response().setHeader("Access-Control-Allow-Origin", "*");
        System.out.println(firstAuthor + " " + secondAuthor);

        List<String> authorPath = new ArrayList<>();

        Iterator<Map<String,Object>> iterator = authorService.shortestPath(firstAuthor, secondAuthor).iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getValue().getClass());

                InternalPath path = (InternalPath) entry.getValue();

                Iterator<Node> nodes = path.nodes().iterator();
                while (nodes.hasNext()) {
                    InternalNode iNode = (InternalNode) nodes.next();
                    Map<String, Object> m = iNode.asMap();
                    authorPath.add(m.get("authorName").toString());
                }
            }
        }

        Map<String, Object> m = new HashMap<>();
        m.put("path", authorPath);
        JsonNode jsonNode = Json.toJson(m);

        return ok(jsonNode);

    }

    //http://localhost:9000/linkKeywords/cloud/data
    public Result getKeywordsNetwork(String keyword1, String keyword2) {
        if (keyword1 == null || keyword1.length() == 0 || keyword2 == null || keyword2.length() == 0) {
            return ok("Please retry valid keywords.");
        }

        keyword1 = keyword1.toLowerCase();
        keyword2 = keyword2.toLowerCase();

        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");

        StatementResult result = session.run("MATCH (k1:Keyword {keyword:\"" + keyword1 +
                "\"})<-[:HAS_KEYWORD]-(p:Paper)-[:HAS_KEYWORD]->(k2:Keyword " +
                "{keyword:\"" + keyword2 + "\"}) RETURN p");

        StringBuilder titles = new StringBuilder();
        while (result.hasNext()) {
            Record record = result.next();
            String title = record.get("p").get("title").toString().replaceAll("\"", "");
            titles.append(title);
            titles.append("\n");
        }

        session.close();
        return ok(titles.deleteCharAt(titles.length() - 1).toString());
    }

    //http://localhost:9000/researcherPath/Zhiyong%20Liu/Yanbo%20Han
    public Result getAuthorsController(String name1, String name2) {
        if (name1 == null || name1.length() == 0 || name2 == null || name2.length() == 0) {
            return ok("Please retry valid names.");
        }

        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");
//
//        StatementResult result = session.run("MATCH r=(a1:Author {authorName:\"" + name1 +
//                "\"})-[:WRITES*]-(a2:Author " +
//                "{authorName:\"" + name2 + "\"}) RETURN r, a2 LIMIT 1");

//        String relation = "";
//
//        while (result.hasNext()) {
//            Record record = result.next();
//            System.out.println(record.get("a2").get("authorName"));
//            relation = record.get("r").toString();
//        }

//        session.close();
//        return ok(relation.equals("") ? "No relationship" : relation);

        StatementResult result1 = session.run("MATCH (a1:Author {authorName:\"" + name1 +
                "\"})-[:WRITES]-(p:Paper)-[:WRITES]-(o1:Author) RETURN o1");

        Set<String> hs = new HashSet<String>();

        while (result1.hasNext()) {
            Record record = result1.next();
            hs.add(record.get("o1").get("authorName").toString());
        }

        StatementResult result2 = session.run("MATCH (a2:Author {authorName:\"" + name2 +
                "\"})-[:WRITES]-(p:Paper)-[:WRITES]-(o2:Author) RETURN o2");

        StringBuilder names = new StringBuilder();
        while (result2.hasNext()) {
            Record record = result2.next();
            String name = record.get("o2").get("authorName").toString();
            if (hs.remove(name)) {
                names.append(name.replaceAll("\"", ""));
                names.append("\n");
            }
        }

        session.close();
        return ok(names.length() > 0 ? names.deleteCharAt(names.length() - 1).toString() : "No path");
    }
}