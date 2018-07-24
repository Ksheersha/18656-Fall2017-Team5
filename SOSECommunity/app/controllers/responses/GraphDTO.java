package controllers.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by imrenagi on 11/3/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphDTO {

    @JsonProperty("nodes")
    public Set<NodeDTO> nodes;

    @JsonProperty("edges")
    public Set<EdgeDTO> edges;

    public GraphDTO() {
        this.nodes = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public void insertNode(NodeDTO node) {
        this.nodes.add(node);
    }

    public void insertEdge(EdgeDTO edge) {
        this.edges.add(edge);
    }
}
