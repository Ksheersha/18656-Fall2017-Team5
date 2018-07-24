package controllers.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by imrenagi on 11/3/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EdgeDTO {

    @JsonProperty("source_id")
    public Long sourceId;

    @JsonProperty("target_id")
    public Long targetId;

    @JsonProperty("type")
    public String type;

    @JsonProperty("metadata")
    public Object metadata;

    public EdgeDTO(Long sourceId, Long targetId, String type, Object metadata) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.metadata = metadata;
        this.type = type;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            EdgeDTO typedObject = (EdgeDTO) anObject;

            equalObjects = typedObject.sourceId.equals(this.sourceId) &&
                    typedObject.targetId.equals(this.targetId);
        }
        return equalObjects;
    }

    @Override
    public int hashCode() {
        int hashCodeValue =
                + (57858 * 21)
                        + this.sourceId.hashCode() + this.targetId.hashCode();
        return hashCodeValue;
    }
}
