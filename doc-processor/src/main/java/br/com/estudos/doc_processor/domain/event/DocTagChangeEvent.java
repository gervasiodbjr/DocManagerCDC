package br.com.estudos.doc_processor.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocTagChangeEvent extends ChangeEvent<DocTagChangeEvent.DocTag> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DocTag {
        @JsonProperty("doc_id")
        public Long docId;
        @JsonProperty("tag_id")
        public Long tagId;
    }

}
