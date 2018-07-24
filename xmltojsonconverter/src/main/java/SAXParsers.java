import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SAXParsers {
    public static void main(String[] args){
        try {
            File inputFile = new File("dblp.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser saxParser = factory.newSAXParser();
            // saxParser.setProperty("ENTITY_EXPANSION_LIMIT", "yes");
            ArticleHandler articleHandler = new ArticleHandler();
            saxParser.parse(inputFile, articleHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


