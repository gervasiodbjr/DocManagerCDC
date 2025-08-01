package br.com.estudos.doc_processor.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoChangeEvent {

    public Documento before;
    public Documento after;
    public Source source;
    public String op;
    @JsonProperty("ts_ms")
    public Long tsMs;
    public Object transaction; // pode ser um objeto ou null

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Source {
        public String version;
        public String connector;
        public String name;
        @JsonProperty("ts_ms")
        public Long tsMs;
        public String snapshot;
        public String db;
        public String sequence;
        public String schema;
        public String table;
        public Long txId;
        public Long lsn;
        public Long xmin;
    }
}
