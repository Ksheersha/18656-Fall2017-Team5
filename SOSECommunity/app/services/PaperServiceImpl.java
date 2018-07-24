package services;

import domain.model.Paper;

import java.util.Collections;
import java.util.Map;

/**
 * Created by imrenagi on 11/19/17.
 */
public class PaperServiceImpl extends GenericService<Paper> {

    public Iterable<Map<String, Object>> getPaperNetwork() {
        String query = "MATCH (p: Paper)-[r:CITE]->(pc: Paper) RETURN p, pc, r";
        return this.session.query(query, Collections.EMPTY_MAP);
    }

    @Override
    Class<Paper> getEntityType() {
        return Paper.class;
    }
}
