package br.com.estudos.doc_processor.domain.dot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DocumentoDTO {

    public Long id;
    public String titulo;
    public String corpo;
    public OffsetDateTime criadoEm;
    public OffsetDateTime atualizadoEm;
    public Integer departamentoId;

}
