package domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by imrenagi on 10/31/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Path {

    @JsonProperty("start_idx")
    private Integer startIdx;

    @JsonProperty("end_idx")
    private Integer endIdx;

    @JsonCreator
    public Path(Integer startIdx, Integer endIdx) {
        this.startIdx = startIdx;
        this.endIdx = endIdx;
    }
}
