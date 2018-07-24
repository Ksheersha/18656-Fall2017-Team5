package controllers.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by imrenagi on 11/3/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorDTO {

    @JsonProperty("node_id")
    public Long nodeId;

    @JsonProperty("name")
    public String name;

    public AuthorDTO(Long nodeId, String name) {
        this.nodeId = nodeId;
        this.name = name;
    }
}
