package br.com.estudos.doc_processor.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoChangeEvent extends ChangeEvent<DocumentoChangeEvent.Documento> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Documento {
        public Long id;
        public String titulo;
        public String corpo;
        @JsonProperty("criado_em")
        public OffsetDateTime criadoEm;
        @JsonProperty("atualizado_em")
        public OffsetDateTime atualizadoEm;
        @JsonProperty("departamento_id")
        public Long departamentoId;
    }

}
