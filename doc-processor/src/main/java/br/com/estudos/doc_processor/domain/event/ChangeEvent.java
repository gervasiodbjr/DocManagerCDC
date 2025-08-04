package br.com.estudos.doc_processor.domain.event;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

@Transactional
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ChangeEvent<T> {

    public T before;
    public T after;
    public Source source;
    public String op;
    @JsonProperty("ts_ms")
    public Long tsMs;
    public Object transaction; // pode ser um objeto ou null

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

