    package controllers;

    import models.Paper;
    import play.libs.Json;
    import play.mvc.Controller;
    import play.mvc.Result;

    import org.neo4j.driver.v1.*;

    import javax.sound.midi.SysexMessage;
    import java.util.*;

    /**
     * Created by sagejoyoox on 11/17/16.
     */
    public class SearchController extends Controller {
        private Driver driver;

        public SearchController() {
            driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "123456" ) );
        }

        public Result searchExperts(String keywords) {

            Session session = driver.session();
            response().setHeader("Access-Control-Allow-Origin", "*");

            String[] keywordsArr = keywords.split(",");
            Map<String, Integer> hm = new HashMap<String, Integer>();

            for (String keyword : keywordsArr) {
                StatementResult result = session.run("MATCH (a:Author)-[:WRITES]->(p:Paper)-[:HAS_KEYWORD]->(k:Keyword {keyword:\""
                        + keyword + "\"}) RETURN a");

                while (result.hasNext()) {
                    Record record = result.next();
                    String name = record.get("a").get("authorName").toString().replaceAll("\"", "");;
                    if (!hm.containsKey(name)) {
                        hm.put(name, 0);
                    }
                    hm.put(name, hm.get(name) + 1);
                }
            }

            Queue<AuthorCount> pq = new PriorityQueue<AuthorCount>(11, new Comparator<AuthorCount>() {
                @Override
                public int compare(AuthorCount o1, AuthorCount o2) {
                    return o2.count - o1.count;
                }
            });

            for (String key : hm.keySet()) {
                pq.offer(new AuthorCount(key, hm.get(key)));
            }

            StringBuilder sb = new StringBuilder();


        for (int i = 0; i < 30; i++) {
            if (pq.isEmpty()) {
                break;
            }
            sb.append(pq.poll().name);
            sb.append("\n");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }


        session.close();
        return ok(sb.toString());
    }


    public Result searchExpertInterests(String keywords, String interest) {
        if (keywords == null || keywords.length() == 0) {
            return ok("No Experts in this criteria. Please retry other keywords.");
        }

        List<String> interests = Arrays.asList(interest.split(",[ ]*"));
        String neoInt ="";
        for(int i=0; i<interests.size(); i++)
            if( i != (interests.size())-1)
                neoInt = neoInt +"a.interests contains \"" + interests.get(i) + "\" OR ";
            else
                neoInt = neoInt +"a.interests contains \"" + interests.get(i) + "\"";
        //a.interests contains \"" + interests + "\"
        Session session = driver.session();
        response().setHeader("Access-Control-Allow-Origin", "*");

        String[] keywordsArr = keywords.split(",");
        Map<String, Integer> hm = new HashMap<String, Integer>();

        for (String keyword : keywordsArr) {
            StatementResult result = session.run("MATCH (a:Author)-[:WRITES]->(p:Paper)-[:HAS_KEYWORD]->(k:Keyword {keyword:\""
                    + keyword + "\"}) WHERE "+ neoInt + " RETURN a");

            while (result.hasNext()) {
                Record record = result.next();
                String name = record.get("a").get("authorName").toString().replaceAll("\"", "");;
                if (!hm.containsKey(name)) {
                    hm.put(name, 0);
                }
                hm.put(name, hm.get(name) + 1);
            }
        }

        Queue<AuthorCount> pq = new PriorityQueue<AuthorCount>(11, new Comparator<AuthorCount>() {
            @Override
            public int compare(AuthorCount o1, AuthorCount o2) {
                return o2.count - o1.count;
            }
        });

        for (String key : hm.keySet()) {
            pq.offer(new AuthorCount(key, hm.get(key)));
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 30; i++) {
            if (pq.isEmpty()) {
                break;
                }
                sb.append(pq.poll().name);
                sb.append("\n");
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);

            }
            session.close();
            return ok(sb.toString());

        }

        /**
         * Created by sagejoyoox on 11/17/16.
         */

        public Result searchTopics(String channel, String year) {
            if (channel == null || channel.length() == 0 || year == null || year.length() == 0) {
                return ok("Please use valid input");
            }

            Session session = driver.session();
            response().setHeader("Access-Control-Allow-Origin", "*");

            StatementResult result = session.run("MATCH (p:Paper {year:\""
                    + year + "\", journal:\"" + channel + "\"})-[:HAS_KEYWORD]->(k:Keyword) RETURN k");

            Map<String, Integer> hm = new HashMap<String, Integer> ();

            while (result.hasNext()) {
                Record record = result.next();
                String name = record.get("k").get("keyword").toString().replaceAll("\"", "");;
                if (!hm.containsKey(name)) {
                    hm.put(name, 0);
                }
                hm.put(name, hm.get(name) + 1);
            }

            Queue<TopicCount> pq = new PriorityQueue<TopicCount>(11, new Comparator<TopicCount>() {
                @Override
                public int compare(TopicCount o1, TopicCount o2) {
                    return o2.count - o1.count;
                }
            });

            for (String key : hm.keySet()) {
                pq.offer(new TopicCount(key, hm.get(key)));
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 10; i++) {
                if (pq.isEmpty()) {
                    break;
                }
                sb.append(pq.poll().topic);
                sb.append("\n");
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }

            session.close();
            return ok(sb.toString());
        }

       /**
        * Updated by sheersha on 11/3/17.
        */
       public Result searchPaper(String title) {
           System.out.println("title:"+title);
               if (title == null || title.length() == 0) {
                   return ok("Please type in a valid title");
               }

               Session session = driver.session();
               response().setHeader("Access-Control-Allow-Origin", "*");

               StatementResult result = session.run("MATCH (a:Author)-[:WRITES]->(p:Paper {title:'" + title +"'})-[:HAS_KEYWORD]->(k:Keyword) "+
                                                              "RETURN p,k,a limit 1");

               Paper paper = new Paper();
               String titleName = "";
               List<String> authors = new ArrayList<String>();
               List<String> citations = new ArrayList<String>();
               String journal = "";
               String abs = "";
               List<String> keywords = new ArrayList<String>();
               String year = "";

               while (result.hasNext()) {
                   Record record = result.next();
                   titleName = record.get("p").get("title").toString().replaceAll("^\\\"", "").replaceAll("\\\"$", "");

                   String author = record.get("a").get("authorName").toString().replaceAll("^\\\"", "").replaceAll("\\\"$", "");
                   if (!authors.contains(author)) {
                       authors.add(author);
                   }

                   String citation = record.get("p").get("title").toString().replaceAll("^\\\"", "").replaceAll("\\\"$", "");
                   if (!citations.contains(citation)) {
                       citations.add(citation);
                   }

                   journal = record.get("p").get("journal").toString().replaceAll("^\\\"", "").replaceAll("\\\"$", "");

                   abs = record.get("p").get("abstract").toString().replaceAll("^\\\"", "").replaceAll("\\\"$", "");

                   String keyword = record.get("k").get("keyword").toString().replaceAll("^\\\"", "").replaceAll("\\\"$", "");
                   if (!keywords.contains(keyword)) {
                       keywords.add(keyword);
                   }

                   year = record.get("p").get("year").toString().replaceAll("^\\\"", "").replaceAll("\\\"$", "");
               }

               paper.setYear(year);
               paper.setKeywords(keywords);
               paper.setJournal(journal);
               paper.setAbs(abs);
               paper.setCitations(citations);
               paper.setAuthors(authors);
               paper.setTitle(titleName);
               System.out.println("paper : "+paper);

               session.close();
               return ok(Json.toJson(paper));
           }
       }


    class AuthorCount {
        String name;
        int count;
        public AuthorCount(String name, int count) {
            this.name = name;
            this.count = count;
        }
    }

    class TopicCount {
        String topic;
        int count;
        public TopicCount(String topic, int count) {
            this.topic = topic;
            this.count = count;
        }
    }
