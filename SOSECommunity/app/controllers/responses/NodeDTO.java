package controllers.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by imrenagi on 11/3/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeDTO {

    @JsonProperty("id")
    public Long id;

    @JsonProperty("type")
    public String type;

    @JsonProperty("metadata")
    public Object metadata;

    public NodeDTO(Long id, String type, Object metadata) {
        this.id = id;
        this.type = type;
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            NodeDTO typedObject = (NodeDTO) anObject;
            equalObjects = typedObject.id.equals(this.id);
        }
        return equalObjects;
    }

    @Override
    public int hashCode() {
        int hashCodeValue =
                + (57853 * 31)
                        + this.id.hashCode();
        return hashCodeValue;
    }

}
