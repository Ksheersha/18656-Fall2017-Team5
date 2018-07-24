package controllers.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by imrenagi on 11/3/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaperDTO {

    @JsonProperty("node_id")
    public Long nodeId;

    @JsonProperty("title")
    public String title;

    public PaperDTO(Long nodeId, String title) {
        this.nodeId = nodeId;
        this.title = title;
    }
}
