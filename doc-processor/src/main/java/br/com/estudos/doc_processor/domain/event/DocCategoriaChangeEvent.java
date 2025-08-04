package br.com.estudos.doc_processor.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocCategoriaChangeEvent extends ChangeEvent<DocCategoriaChangeEvent.DocCategoria> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DocCategoria {
        @JsonProperty("doc_id")
        public Long docId;
        @JsonProperty("categoria_id")
        public Long categoriaId;
    }
}
