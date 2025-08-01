package br.com.estudos.doc_processor.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "documento_view")
public class DocumentoView extends PanacheEntityBase {

    @Id
    public Integer id;

    public String titulo;

    @Column(columnDefinition = "text")
    public String corpo;

    @Column(name = "criado_em")
    public OffsetDateTime criadoEm;

    @Column(name = "atualizado_em")
    public OffsetDateTime atualizadoEm;

    public String departamento;

    @Column(columnDefinition = "text")
    public List<String> categorias;

    @Column(columnDefinition = "text")
    public List<String> tags;

}
